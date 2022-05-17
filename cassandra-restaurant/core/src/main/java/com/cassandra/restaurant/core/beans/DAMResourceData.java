package com.cassandra.restaurant.core.beans;

public class DAMResourceData {
    private String data;
    private boolean isValidRequest;

    public String getData() {
        return data;
    }

    public boolean isValidRequest() {
        return isValidRequest;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValidRequest(boolean validRequest) {
        isValidRequest = validRequest;
    }
}
