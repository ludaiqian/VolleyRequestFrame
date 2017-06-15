package com.zitech.test.api.datasource;

import com.zitech.test.api.request.PagedRequest;

import org.fans.http.frame.toolbox.packet.ApiResponse;

public class PagedRequestProxy extends PagedRequest {
    private PagedRequest request;
    private int pageCount = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    private boolean pageCountSet = false;
    private boolean reachEnd = false;

    public PagedRequestProxy(PagedRequest request) {
        this.request = request;
        if (request.getCurrentPage() < 1) {
            this.reset();
        }
    }

    public void reset() {
        this.setCurrentPage(1);
        this.pageCountSet = false;
        this.reachEnd = false;
        this.pageCount = 0;
    }


    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        this.pageCountSet = true;
    }

    public boolean hasNextPage() {
        return this.reachEnd ? false : !this.pageCountSet || this.request.getCurrentPage() <= this.pageCount;
    }

    public void setReachEnd(boolean reachEnd) {
        this.reachEnd = reachEnd;
    }

    public void setPageSize(int pageSize) {
        this.request.setPageSize(pageSize);
    }

    public int getPageSize() {
        return this.request.getPageSize()> 0 ? request.getPageSize() : DEFAULT_PAGE_SIZE;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 0) {
            throw new IllegalArgumentException();
        } else {
            this.request.setCurrentPage(currentPage);
        }
    }

    public boolean isFirstPage() {
        return this.request.getCurrentPage() == 1;
    }

    public int getCurrentPage() {
        return request.getCurrentPage() ;
    }

    public void toNextPage() {
        this.request.setCurrentPage(this.request.getCurrentPage() + 1);
    }


    public final PagedRequest getRequest() {
        return this.request;
    }



    public ApiResponse parse(String result) {
        return this.request.parse(result);
    }

    public boolean isPageCountSet() {
        return pageCountSet;
    }



}
