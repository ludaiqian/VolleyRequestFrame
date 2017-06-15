package com.zitech.test.api.response;

import org.fans.http.frame.toolbox.packet.ApiResponse;

/**
 * Created by lu on 2016/3/14.
 */
public interface PagedResponse<T> extends ApiResponse<T> {
    //    public
    int getPageCount();
}

