package com.cassandra.restaurant.core.beans;

import java.util.List;

public class CassandraEmployeeData {

    private List<CassandraEmployee> data;

    public List<CassandraEmployee> getData() {
        return data;
    }

    public void setData(List<CassandraEmployee> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CassandraEmployeeData{" +
                "data=" + data +
                '}';
    }
}
