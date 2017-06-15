package org.fans.http.frame;

import com.android.volley.HttpError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ResponseListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.zitech.framework.utils.Logger;

import java.io.UnsupportedEncodingException;

/**
 *  实体对象请求
 * @param <T>
 */
public class PojoRequest<T> extends Request<T> {
    private Parser parser;
    private static final String CONTENT_TYPE = "Content-Type";

    public PojoRequest(int method, String url, Parser<T> parser, HttpParams params, ResponseListener<T> listener) {
        super(method, url, listener);
        setHeaders(params.getHeaders());
        if (method == Method.POST || method == Method.PUT) {
            setBodyContentType(params.getContentType());
            setParams(params.getPostParams());
            setFormParams(params.getFormParams());
            setBody(params.getRequestBody());
            setFormFiles(params.getFormFiles());
        } else {
            addHeader(CONTENT_TYPE, params.getContentType());
        }
        this.parser = parser;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        Logger.i("HTTP", parsed);
        try {
            T resp =  (T)parser.parse(parsed);
            return (Response) Response.success(resp, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error(new HttpError(HttpError.PARSE_ERROR));
    }

}
