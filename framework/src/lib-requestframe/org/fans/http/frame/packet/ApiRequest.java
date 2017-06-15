package org.fans.http.frame.packet;

import org.fans.http.frame.HttpParams;
import org.fans.http.frame.Parser;
import org.fans.http.frame.PojoRequest;

/**
 * ///xxx
 * //xa
 *
 * @author Ludaiqian
 * @since 1.0
 */
public interface ApiRequest extends ApiPacket, Parser<ApiResponse> {

    public String getMethod();

    /**
     * 解析返回数据
     *
     * @param result
     * @return
     */
    @Override
    public ApiResponse parse(String result);

}
