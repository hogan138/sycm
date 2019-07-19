package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/21.
 * 反馈建议
 */
@Data
public class FeedBackSuggestBean {

    private String name;//用户姓名
    private String phone;//手机号码
    private String content;//建议内容


    @Override
    public String toString() {
        return "FeedBackSuggestBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
