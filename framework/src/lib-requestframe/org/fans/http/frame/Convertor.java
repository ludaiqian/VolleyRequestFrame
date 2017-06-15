package org.fans.http.frame;

import org.fans.http.frame.packet.ApiRequest;

/**
 * Created by lu on 2015/6/20.
 */
public interface Convertor {
    public HttpParams convert(ApiRequest request);
}
