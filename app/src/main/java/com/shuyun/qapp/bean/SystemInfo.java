package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/21.
 * 跑马灯消息
 */
@Data
public class SystemInfo {
    private Long type;//消息类型  1——中奖消息
    private String msg;//消息内容
    private String time;//消息时间


    @Override
    public String toString() {
        return "SystemInfo{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
