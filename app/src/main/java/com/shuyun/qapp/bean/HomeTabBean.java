package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeTabBean
 * @Description: homeTab
 * @Author: ganquan
 * @CreateDate: 2019/3/13 15:53
 */
public class HomeTabBean {


    /**
     * title : 建国70周年
     * picture : http://192.168.3.157:/icon/seventy_year.png
     * width : 68
     * height : 32
     * action : action.h5
     * color : #FFB500
     * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
     * mode : 1
     * status : 1
     */

    private String title;
    private String picture;
    private int width;
    private int height;
    private String action;
    private String color;
    private String h5Url;
    private Long mode;
    private Long status;
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Long getMode() {
        return mode;
    }

    public void setMode(Long mode) {
        this.mode = mode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
