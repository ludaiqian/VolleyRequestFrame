/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;

import android.os.SystemClock;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.HttpError;
import com.android.volley.URLHttpResponse;
import com.android.volley.VolleyLog;

/**
 * A network performing Volley requests over an {@link HttpStack}.
 */
public class BasicNetwork implements Network {
	protected static final boolean DEBUG = VolleyLog.DEBUG;

	private static int SLOW_REQUEST_THRESHOLD_MS = 3000;

	private static int DEFAULT_POOL_SIZE = 4096;

	protected final HttpStack mHttpStack;

	protected final ByteArrayPool mPool;

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 */
	public BasicNetwork(HttpStack httpStack) {
		// If a pool isn't passed in, then build a small default pool that will
		// give us a lot of
		// benefit and not use too much memory.
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 * @param pool
	 *            a buffer pool that improves GC performance in copy operations
	 */
	public BasicNetwork(HttpStack httpStack, ByteArrayPool pool) {
		mHttpStack = httpStack;
		mPool = pool;
	}

	@Override
	public NetworkResponse performRequest(Request<?> request) throws HttpError {
		long requestStart = SystemClock.elapsedRealtime();
		while (true) {
			URLHttpResponse httpResponse = null;
			byte[] responseContents = null;
			Map<String, String> responseHeaders = new HashMap<String, String>();
			try {
				// Gather headers.
				Map<String, String> headers = new HashMap<String, String>();
				addCacheHeaders(headers, request.getCacheEntry());
				httpResponse = mHttpStack.performRequest(request, headers);
				int statusCode = httpResponse.getResponseCode();
				responseHeaders = httpResponse.getHeaders();
				// Handle cache validation.
				if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
					return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED, request.getCacheEntry().data, responseHeaders, true);
				}

				// Some responses such as 204s do not have content. We must
				// check.
				if (httpResponse.getContentStream() != null) {
						responseContents = entityToBytes(httpResponse);
//                    responseContents = entityToBytes(httpResponse);
				} else {
					responseContents = new byte[0];
				}

				// if the request is slow, log it.
				long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
				logSlowRequests(requestLifetime, request, responseContents, statusCode);

				if (statusCode < 200 || statusCode > 299) {
					throw new IOException();
				}
				return new NetworkResponse(statusCode, responseContents, responseHeaders, false);
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				attemptRetryOnException("socket", request, new HttpError(HttpError.TIME_OUT));
			} catch (ConnectTimeoutException e) {
				e.printStackTrace();
				attemptRetryOnException("connection", request, new HttpError(HttpError.TIME_OUT));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException("Bad URL " + request.getUrl(), e);
			} catch (IOException e) {
				e.printStackTrace();
				int statusCode = 0;
				NetworkResponse networkResponse = null;
				if (httpResponse != null) {
					statusCode = httpResponse.getResponseCode();
				} else {
					throw new HttpError(HttpError.IO_ERROR);
				}
				VolleyLog.e("Unexpected response code %d for %s", statusCode, request.getUrl());
				if (responseContents != null) {
					networkResponse = new NetworkResponse(statusCode, responseContents, responseHeaders, false);
					if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) {
						attemptRetryOnException("auth", request, new HttpError(HttpError.AUTH_FAILURE_ERROR));
					} else {
						// TODO: Only throw ServerError for 5xx status codes.
						throw new HttpError(HttpError.IO_ERROR);
					}
				} else {
					throw new HttpError(HttpError.IO_ERROR);
				}
			}
		}
	}

	/**
	 * Logs requests that took over SLOW_REQUEST_THRESHOLD_MS to complete.
	 */
	private void logSlowRequests(long requestLifetime, Request<?> request, byte[] responseContents, int statusCode) {
		if (DEBUG || requestLifetime > SLOW_REQUEST_THRESHOLD_MS) {
			VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], " + "[rc=%d], [retryCount=%s]", request,
					requestLifetime, responseContents != null ? responseContents.length : "null", statusCode,
					request.getRetryPolicy().getCurrentRetryCount());
		}
	}

	/**
	 * Attempts to prepare the request for a retry. If there are no more
	 * attempts remaining in the request's retry policy, a timeout exception is
	 * thrown.
	 * 
	 * @param request
	 *            The request to use.
	 */
	private static void attemptRetryOnException(String logPrefix, Request<?> request, HttpError exception) throws HttpError {
		RetryPolicy retryPolicy = request.getRetryPolicy();
		int oldTimeout = request.getTimeoutMs();

		try {
			retryPolicy.retry(exception);
		} catch (HttpError e) {
			request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", logPrefix, oldTimeout));
			throw e;
		}
		request.addMarker(String.format("%s-retry [timeout=%s]", logPrefix, oldTimeout));
	}

	private void addCacheHeaders(Map<String,String> headers, Cache.Entry entry) {
		// If there's no cache entry, we're done.
		if (entry == null) {
			return;
		}

		if (entry.etag != null) {
			headers.put("If-None-Match", entry.etag);
		}

		if (entry.serverDate > 0) {
			Date refTime = new Date(entry.serverDate);
			headers.put("If-Modified-Since", DateUtils.formatDate(refTime));
		}
	}

	protected void logError(String what, String url, long start) {
		long now = SystemClock.elapsedRealtime();
		VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, (now - start), url);
	}

	/** Reads the contents of HttpEntity into a byte[]. */
	private byte[] entityToBytes(URLHttpResponse httpResponse) throws IOException, HttpError {
		PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(mPool, (int) httpResponse.getContentLength());
		byte[] buffer = null;
		try {
			InputStream in = httpResponse.getContentStream();
			if (in == null) {
				throw new HttpError(HttpError.SERVER_ERROR);
			}
			buffer = mPool.getBuf(1024);
			int count;
			while ((count = in.read(buffer)) != -1) {
				bytes.write(buffer, 0, count);
			}
			return bytes.toByteArray();
		} finally {
			try {
				// Close the InputStream and release the resources by
				// "consuming the content".
//				entity.consumeContent();
				httpResponse.getContentStream().close();
			} catch (IOException e) {
				// This can happen if there was an exception above that left the
				// entity in
				// an invalid state.
				VolleyLog.v("Error occured when calling consumingContent");
			}
			mPool.returnBuf(buffer);
			bytes.close();
		}


	}

}
