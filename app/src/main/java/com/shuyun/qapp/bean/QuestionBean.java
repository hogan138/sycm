package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/4/28.
 * old
 */

public class QuestionBean {
    private int id;
    private String title;//标题
    private String accuracy;//正确率
    private String time;//时间

    public QuestionBean(int id, String title, String accuracy, String time) {
        this.id = id;
        this.title = title;
        this.accuracy = accuracy;
        this.time = time;
    }

    public QuestionBean(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "QuestionBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
