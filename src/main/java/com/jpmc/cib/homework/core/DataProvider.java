package com.jpmc.cib.homework.core;

public class DataProvider {
    private String name;
    private String url;


    public DataProvider(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public DataProvider() {
    }

    @Override
    public String toString() {
        return "DataProvider{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
