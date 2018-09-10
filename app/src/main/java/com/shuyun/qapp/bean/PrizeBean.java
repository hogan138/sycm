package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/4/28.
 * old
 */

public class PrizeBean {

    //    public static final int NOT_USE = 0;
//
//    public static final int ALREADY_USED = 1;
    private String title;
    private String outDataTime;
    private String almostExpired;
    private String content;
    private String num;
    private String use;
//    private int type;

//    public PrizeBean(String title, String outDataTime, String almostExpired, String content, String num, String use, int type) {
//        this.title = title;
//        this.outDataTime = outDataTime;
//        this.almostExpired = almostExpired;
//        this.content = content;
//        this.num = num;
//        this.use = use;
//        this.type = type;
//    }


    public PrizeBean(String title, String outDataTime, String almostExpired, String content, String num, String use) {
        this.title = title;
        this.outDataTime = outDataTime;
        this.almostExpired = almostExpired;
        this.content = content;
        this.num = num;
        this.use = use;
    }

    public String getTitle() {
        return title;
    }

    public String getOutDataTime() {
        return outDataTime;
    }

    public String getAlmostExpired() {
        return almostExpired;
    }

    public String getContent() {
        return content;
    }

    public String getNum() {
        return num;
    }

    public String getUse() {
        return use;
    }

//    public int getType() {
//        return type;
//    }
}
