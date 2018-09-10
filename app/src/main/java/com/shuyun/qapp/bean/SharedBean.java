package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/10.
 * 答题分享
 */

public class SharedBean {

    /**
     * err : 00000
     * msg : null
     * ver : 1
     * dat : {"id":10,"title":"一起来答题吧","content":"我在全民共进答题，发现很有趣，一起来答题吧！","url":"http://wx.25876.com/share/h5/exam?p=%7B%22inviter%22%3A130%2C%22exam%22%3A%224d9cb6ccbf2d431294cee783c31c37fb%22%2C%22group%22%3A108%7D"}
     */

    /**
     * id : 10
     * title : 一起来答题吧
     * content : 我在全民共进答题，发现很有趣，一起来答题吧！
     * url : http://wx.25876.com/share/h5/exam?p=%7B%22inviter%22%3A130%2C%22exam%22%3A%224d9cb6ccbf2d431294cee783c31c37fb%22%2C%22group%22%3A108%7D
     */

    private int id;//分享id
    private String title;//分享文案标题
    private String content;//分享文案内容
    private String url;//分享的网页地址

    private String time;//衢州活动,时间戳
    private String picture;//背景图
    private String buttonCaption;//按钮标题

    public void setTime(String time) {
        this.time = time;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTime() {
        return time;
    }

    public String getPicture() {
        return picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setButtonCaption(String buttonCaption) {
        this.buttonCaption = buttonCaption;
    }

    public String getButtonCaption() {
        return buttonCaption;
    }

    @Override
    public String toString() {
        return "SharedBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", picture='" + picture + '\'' +
                ", buttonCaption='" + buttonCaption + '\'' +
                '}';
    }
}
