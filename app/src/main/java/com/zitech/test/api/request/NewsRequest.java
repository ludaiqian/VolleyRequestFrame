package com.zitech.test.api.request;


import org.fans.http.frame.toolbox.packet.ApiResponse;

/**
 * Created by lu on 2016/3/12
 * http://api.iclient.ifeng.com/ClientNews?id=KJ123,FOCUSKJ123&page=1&gv=5.1.1&av=0&proid=ifengnews&os=ios_9.3.1.
 */
public class NewsRequest extends  PagedRequest {
    //http://api.iclient.ifeng.com/ClientNews?id=KJ123,FOCUSKJ123&page=3&gv=5.1.1&av=0&proid=ifengnews&os=ios_9.3.1

    private String id = "KJ123";
    private int page;
    private String gv = "5.1.1";
    private int av = 0;
    private String proid = "ifengnews";
    private String os = "ios_9.3.1";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    @Override
    public ApiResponse parse(String result) {
        result = result.substring(1, result.length() - 1);
        return super.parse(result);
    }

    @Override
    public int getCurrentPage() {
        return page;
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public void setCurrentPage(int i) {
        page = i;
    }

    @Override
    public void setPageSize(int i) {

    }
}
