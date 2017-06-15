package org.fans.http.frame.toolbox;

import org.fans.http.frame.HttpParams;
import org.fans.http.frame.toolbox.packet.ApiRequest;

/**
 * Created by lu on 2015/6/20.
 */
public interface Convertor {
    public HttpParams convert(ApiRequest request);
}
