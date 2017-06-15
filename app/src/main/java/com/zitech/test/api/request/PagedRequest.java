package com.zitech.test.api.request;

/**
 * 代表一个分页请求
 * Created by lu on 2016/3/14.
 */
public abstract class PagedRequest extends Request {
   public abstract int getCurrentPage();

    public abstract int getPageSize();

    public abstract void setCurrentPage(int var1);

    public abstract void setPageSize(int var1);

}
