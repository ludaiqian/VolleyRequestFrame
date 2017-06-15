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

import com.android.volley.Request;
import com.android.volley.Request.FormFile;
import com.android.volley.URLHttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An HttpStack that performs request over an {@link OkHttpClient}.
 */
public class OkHttpStack implements HttpStack {
    private final OkHttpClient mClient;

    // public static final MediaType MEDIA_TYPE_MARKDOWN
    // = MediaType.parse("text/x-markdown; charset=utf-8");

    public OkHttpStack(OkHttpClient client) {
        this.mClient = client;
    }

    @Override
    public URLHttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException {
        OkHttpClient client = mClient;
        int timeoutMs = request.getTimeoutMs();
//        client.setConnectTimeout(timeoutMs, TimeUnit.MILLISECONDS);
//        client.setReadTimeout(timeoutMs, TimeUnit.MILLISECONDS);
//        client.setWriteTimeout(timeoutMs, TimeUnit.MILLISECONDS);

        okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();

        // okhttp 3.0以后的版本构建OkHttpClient使用Builder
        OkHttpClient.Builder builder = mClient.newBuilder();
        builder.connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        okHttpRequestBuilder.url(request.getUrl());

        Map<String, String> headers = request.getHeaders();
        addHeaders(okHttpRequestBuilder, headers);
        addAdditionalHeaders(okHttpRequestBuilder, additionalHeaders);
        setConnectionParametersForRequest(okHttpRequestBuilder, request);

        okhttp3.Request okHttpRequest = okHttpRequestBuilder.build();
        Call okHttpCall = client.newCall(okHttpRequest);
        Response okHttpResponse = okHttpCall.execute();
        return responseFromConnection(okHttpResponse);
    }
    private URLHttpResponse responseFromConnection(Response okHttpResponse)
            throws IOException {
        URLHttpResponse response = new URLHttpResponse();
        //contentStream
        int responseCode = okHttpResponse.code();
        if (responseCode == -1) {
            throw new IOException(
                    "Could not retrieve response code from HttpUrlConnection.");
        }
        response.setResponseCode(responseCode);
        response.setResponseMessage(okHttpResponse.message());

        response.setContentStream(okHttpResponse.body().byteStream());

        response.setContentLength(okHttpResponse.body().contentLength());
        response.setContentEncoding(okHttpResponse.header("Content-Encoding"));
        if (okHttpResponse.body().contentType() != null) {
            response.setContentType(okHttpResponse.body().contentType().type());
        }


        //header
        HashMap<String, String> headerMap = new HashMap<>();
        Headers responseHeaders = okHttpResponse.headers();
        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            final String name = responseHeaders.name(i), value = responseHeaders.value(i);
            if (name != null) {
                headerMap.put(name, value);
            }
        }
        response.setHeaders(headerMap);
        return response;
    }
    private void addHeaders(okhttp3.Request.Builder okHttpRequestBuilder, Map<String, String> headers) {
        for (final String name : headers.keySet()) {
            okHttpRequestBuilder.addHeader(name, headers.get(name));
        }
    }
    private void addAdditionalHeaders(okhttp3.Request.Builder okHttpRequestBuilder, Map<String, String> headers) {
        for (final String name : headers.keySet()) {
            // 这里用header方法，如果有重复的name，会覆盖，否则某些请求会被判定为非法
            okHttpRequestBuilder.header(name, headers.get(name));
        }
    }

    private static void setConnectionParametersForRequest(okhttp3.Request.Builder builder, Request<?> request)
            throws IOException {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                // Ensure backwards compatibility.
                // Volley assumes a request with a null body is a GET.
                RequestBody requestBody = createRequestBody(request);
                if (requestBody != null) {
                    builder.post(requestBody);
                } else {
                    builder.get();
                }
                break;

            case Request.Method.GET:
                builder.get();
                break;

            case Request.Method.DELETE:
                builder.delete();
                break;

            case Request.Method.POST:

                builder.post(createRequestBody(request));
                break;

            case Request.Method.PUT:
                builder.put(createRequestBody(request));
                break;

            case Request.Method.HEAD:
                builder.head();
                break;

            case Request.Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;

            case Request.Method.TRACE:
                builder.method("TRACE", null);
                break;

            case Request.Method.PATCH:
                builder.patch(createRequestBody(request));
                break;

            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }


    private static RequestBody createRequestBody(Request<?> r) {
        byte[] body = r.getBody();
        if (body == null && r.hasPostFormFiles()) {
            return createFormFilesBody(r);
        }
        if (body == null)
            body = new byte[0];
        return RequestBody.create(MediaType.parse(r.getBodyContentType()), body);
    }

    public static RequestBody createFormFilesBody(Request<?> request) {

        List<FormFile> formFiles = request.getPostFormFiles();
        MultipartBody.Builder partBuilder = new MultipartBody.Builder();
        partBuilder.setType(MultipartBody.FORM);
        List<Request.FormParam> formHeaders = request.getPostFormParams();

        if (formHeaders != null && formHeaders.size() > 0) {
            for (Request.FormParam param : formHeaders) {
                partBuilder.addFormDataPart(param.getName(), param.getValue());
            }

        }
        for (FormFile file : formFiles) {
            if (file.getData() != null) {
                partBuilder.addFormDataPart(file.getName(), file.getFileName(),
                        RequestBody.create(MediaType.parse(file.getMimeType()), file.getData()));
            } else {
                partBuilder.addFormDataPart(file.getName(), file.getFileName(),
                        RequestBody.create(MediaType.parse(file.getMimeType()), new File(file.getFilePath())));
            }
        }
        // 构建请求体
        return partBuilder.build();

    }
}