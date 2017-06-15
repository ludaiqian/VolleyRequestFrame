package com.zitech.test.api.response;

import java.util.List;

/**
 * Created by lu on 2016/3/12.
 */
public class NewsList extends Response implements PagedResponse<Response>{
    private String listId;//KJ123",
    private String type;//":list",
    private String expiredTime;//": 180000,
    private String currentPage;//": 3,
    private int totalPage;//": 56,

    private List<News> item;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<News> getItem() {
        return item;
    }

    public void setItem(List<News> item) {
        this.item = item;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }


    @Override
    public int getPageCount() {
        return totalPage;
    }

}
