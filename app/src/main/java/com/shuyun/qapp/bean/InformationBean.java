package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/4/26.
 * old
 */

public class InformationBean {
    private String name;
    private String date;
    private String info;

    public InformationBean() {
    }

    public InformationBean(String name, String date, String info) {
        this.name = name;
        this.date = date;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "InformationBean{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
