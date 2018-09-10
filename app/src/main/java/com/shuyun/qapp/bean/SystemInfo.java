package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/21.
 * 跑马灯消息
 */

public class SystemInfo {
    private int type;//消息类型  1——中奖消息
    private String msg;//消息内容
    private String time;//消息时间

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "SystemInfo{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
