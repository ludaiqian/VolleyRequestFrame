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

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


import com.android.volley.Request;
import com.android.volley.Request.FormFile;
import com.android.volley.Request.Method;
import com.android.volley.HttpError;
import com.android.volley.URLHttpResponse;

/**
 * An {@link HttpStack} based on {@link HttpURLConnection}.
 */
public class HurlStack implements HttpStack {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    /**
     * An interface for transforming URLs before use.
     */
    public interface UrlRewriter {
        /**
         * Returns a URL to use instead of the provided one, or null to indicate
         * this URL should not be used at all.
         */
        public String rewriteUrl(String originalUrl);
    }

    private final UrlRewriter mUrlRewriter;
    private final SSLSocketFactory mSslSocketFactory;

    public HurlStack() {
        this(null);
    }

    /**
     * @param urlRewriter Rewriter to use for request URLs
     */
    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    /**
     * @param urlRewriter      Rewriter to use for request URLs
     * @param sslSocketFactory SSL factory to use for HTTPS connections
     */
    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        mUrlRewriter = urlRewriter;
        mSslSocketFactory = sslSocketFactory;
    }

    @Override
    public URLHttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException,
            HttpError {
        String url = request.getUrl();
        HashMap<String, String> map = new HashMap<String, String>();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (mUrlRewriter != null) {
            String rewritten = mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }
        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl, request);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        return responseFromConnection(connection);
    }

    private URLHttpResponse responseFromConnection(HttpURLConnection connection)
            throws IOException {

        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException(
                    "Could not retrieve response code from HttpUrlConnection.");
        }
        URLHttpResponse response = new URLHttpResponse();
        //contentStream
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        response.setResponseCode(responseCode);
        response.setResponseMessage(connection.getResponseMessage());
        response.setContentStream(inputStream);
        response.setContentLength(connection.getContentLength());
        response.setContentEncoding(connection.getContentEncoding());
        response.setContentType(connection.getContentType());
        //header
        HashMap<String, String> headerMap = new HashMap<>();
        for (Entry<String, List<String>> header : connection.getHeaderFields()
                .entrySet()) {
            if (header.getKey() != null) {
                StringBuilder value = new StringBuilder();
                for (String v : header.getValue()) {
                    value.append(v).append(";");
                }
                headerMap.put(header.getKey(), value.toString());
            }
        }
        response.setHeaders(headerMap);
        return response;
    }


    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Opens an {@link HttpURLConnection} with parameters.
     *
     * @param url
     * @return an open connection
     * @throws IOException
     */
    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);

        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        // use caller-provided custom SslSocketFactory, if any, for HTTPS
        if ("https".equals(url.getProtocol())) {
            if (mSslSocketFactory != null) {
                ((HttpsURLConnection) connection)
                        .setSSLSocketFactory(mSslSocketFactory);
            } else {
                //信任所有证书
                HTTPSTrustManager.allowAllSSL();
            }
        }

        return connection;
    }

    @SuppressWarnings("deprecation")
    /* package */ static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request)
            throws IOException, HttpError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST:
                // This is the deprecated way that needs to be handled for backwards
                // compatibility.
                // If the request's post body is null, then the assumption is that
                // the request is
                // GET. Otherwise, it is assumed that the request is a POST.
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    // Prepare output. There is no need to set Content-Length
                    // explicitly,
                    // since this is handled by HttpURLConnection using the size of
                    // the prepared
                    // output stream.
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                } else {
                    connection.setRequestMethod("GET");
                    //setFormFilesIfExist(connection, request);
                }
                break;
            case Method.GET:
                // Not necessary to set the request method because connection
                // defaults to GET but
                // being explicit here.
                connection.setRequestMethod("GET");
                break;
            case Method.DELETE:
                connection.setRequestMethod("DELETE");
                break;
            case Method.POST:
                connection.setRequestMethod("POST");
                byte[] body = request.getBody();
                if (body != null) {
                    connection.setDoOutput(true);
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(body);
                    out.close();
                } else {
                    setFormFilesIfExist(connection, request);
                }
                break;
            case Method.PUT:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, HttpError {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private static void setFormFilesIfExist(HttpURLConnection connection, Request<?> request) throws IOException,
            FileNotFoundException {
        List<FormFile> formFiles = request.getPostFormFiles();
        if (formFiles != null && formFiles.size() > 0) {
            connection.setRequestProperty(HEADER_CONTENT_TYPE, request.getPostFormFileContentType());
            List<Request.FormParam> formParams= request.getPostFormParams();
            StringBuilder paramsBuffer = new StringBuilder();
            if (formParams != null && formParams.size() > 0) {
                for ( Request.FormParam param : formParams) {
                    String disposition = param.getParamDisposition();
                    paramsBuffer.append(disposition);
                }
            }
            // out.write(sb.)
            long contentLength = 0;
            if (paramsBuffer.length() > 0)
                contentLength += paramsBuffer.toString().getBytes().length;
            for (FormFile file : formFiles) {
                // System.out.println("current file length:" +
                // file.getFromFileLength());
                contentLength += file.getFromFileLength();
            }
            contentLength += request.getPostFormFileEndline().getBytes().length;
            //
            connection.setRequestProperty("Content-Length", String.valueOf(contentLength));
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            int writedLen = 0;
            if (paramsBuffer.length() > 0) {
                byte[] paramsBuff = paramsBuffer.toString().getBytes();
                out.write(paramsBuff);
                writedLen += paramsBuff.length;
            }
            for (FormFile file : formFiles) {
                byte[] dispositionBuff = file.getFileDisposition().getBytes();
                out.write(dispositionBuff);
                writedLen += dispositionBuff.length;
                if (file.getData() != null) {
                    byte[] dataBuff = file.getData();
                    out.write(dataBuff);
                    writedLen += dataBuff.length;
                    request.postFormFileUploadingProgress(contentLength, writedLen);
                } else {
                    FileInputStream fin = new FileInputStream(file.getFilePath());
                    byte buff[] = new byte[1024];
                    int len = -1;
                    while (-1 != (len = fin.read(buff))) {
                        writedLen += len;
                        out.write(buff, 0, len);
                        request.postFormFileUploadingProgress(contentLength, writedLen);
                    }
                    fin.close();
                }
                // System.out.println("success write one..."+file.getFilePath());
                out.write(file.getEndLine().getBytes());
                // out.close();
            }

            byte[] endData = request.getPostFormFileEndline().getBytes();
            out.write(endData);
            out.close();
            request.postFormFileUploadingProgress(contentLength, contentLength);
            // System.out.println("write end...");
        }
    }
}
