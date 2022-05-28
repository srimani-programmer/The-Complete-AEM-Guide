package com.cassandra.restaurant.core.beans;

public class AgifyCustomerData {

    private String name;
    private int age;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AgifyCustomerData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", count=" + count +
                '}';
    }
}
