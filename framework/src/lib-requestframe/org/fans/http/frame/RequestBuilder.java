package org.fans.http.frame;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.Request.ProgressListener;
import com.android.volley.ResponseListener;
import com.zitech.framework.utils.Logger;

/**
 * 实体请求构造器
 * Created by lu on 2015/3/19.
 */
public class RequestBuilder {
    private HttpParams params;
    private String contentType;
    private ResponseListener callback;
    private ProgressListener progressListener;
    private int method = Method.DEPRECATED_GET_OR_POST;
    private String url;
    private Parser parser;
    private String uniqueKey;

    /**
     * Http请求参数
     */
    public RequestBuilder params(HttpParams params) {
        this.params = params;
        return this;
    }

    /**
     * 唯一标识,重复的key将不会被同时执行
     *
     * @param uniqueKey
     * @return
     */
    public RequestBuilder uniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
        return this;
    }

    public RequestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * 请求回调
     */
    public <T> RequestBuilder callback(ResponseListener<T> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 上传进度回调
     *
     * @param listener 进度监听器
     */
    public RequestBuilder progressListener(ProgressListener listener) {
        this.progressListener = listener;
        return this;
    }

    /**
     * 查看RequestConfig$Method
     * GET/POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
     */
    public RequestBuilder httpMethod(int httpMethod) {
        this.method = httpMethod;
        return this;
    }


    /**
     * 网络请求接口url
     */
    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder parser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public PojoRequest create() {
        if (params == null) {
            params = new HttpParams();
        } else {
            if (method == Method.GET)
                url += params.getUrlParams();
        }
        if (contentType != null) {
            params.setContentType(contentType);
        }
        if (TextUtils.isEmpty(url)) {
            throw new RuntimeException("Request url is empty");
        }
        Logger.i("HTTP", url);
        PojoRequest request = new PojoRequest(method, url, parser, params, callback);
        request.setKey(uniqueKey);
        request.setFormFileUploadingListener(progressListener);
        return request;
    }


}