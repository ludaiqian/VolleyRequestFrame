package org.fans.http.frame.toolbox;

import org.fans.http.frame.Convertor;
import org.fans.http.frame.HttpParams;
import org.fans.http.frame.packet.ApiRequest;
import org.fans.http.frame.toolbox.JsonSerializer;

import java.io.UnsupportedEncodingException;

/**
 * Created by lu on 2016/4/20.
 */
public class JsonConvertor implements Convertor {

    @Override
    public HttpParams convert(ApiRequest request) {
        HttpParams params = new HttpParams();
        try {
            params.setRequestBody(JsonSerializer.DEFAULT.serialize(request).getBytes(HttpParams.CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }
}
