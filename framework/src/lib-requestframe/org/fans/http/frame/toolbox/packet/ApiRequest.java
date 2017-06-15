package org.fans.http.frame.toolbox.packet;

import org.fans.http.frame.Parser;

/**
 * ///xxx
 * //xa
 * 对请求接口的抽象
 * @author Ludaiqian
 * @since 1.0
 */
public interface ApiRequest extends ApiPacket, Parser<ApiResponse> {


    /**
     * 解析返回数据
     *
     * @param result
     * @return
     */
    @Override
    public ApiResponse parse(String result);

}
