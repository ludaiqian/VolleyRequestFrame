package com.zitech.test.api.request;

import org.fans.http.frame.packet.ApiRequest;

/**
 * 代表一个分页请求
 * Created by lu on 2016/3/14.
 */
public interface PagedRequest extends ApiRequest {
    int getCurrentPage();

    int getPageSize();

    void setCurrentPage(int var1);

    void setPageSize(int var1);

}
