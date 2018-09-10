package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/21.
 * 反馈建议
 */

public class FeedBackSuggestBean {

    private String name;//用户姓名
    private String phone;//手机号码
    private String content;//建议内容

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "FeedBackSuggestBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
