package com.cassandra.restaurant.core.beans;

import java.util.List;

public class PageListBean {
    private boolean isValidRequest;
    private List<String> pages;

    public boolean isValidRequest() {
        return isValidRequest;
    }

    public void setValidRequest(boolean validRequest) {
        isValidRequest = validRequest;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }
}
