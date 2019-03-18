package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: FloatWindowBean
 * @Description: 浮窗数据
 * @Author: ganquan
 * @CreateDate: 2019/3/18 13:22
 */
public class FloatWindowBean {


    /**
     * picture : http://192.168.3.157:/icon/float_window.png
     * width : 46
     * height : 51
     * padding : 0,0,0,0
     * margin : 3,3,3,3
     * shadow : 0
     * action : action.h5
     * color :
     * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
     * status : 1
     */

    private String picture;
    private Long width;
    private Long height;
    private String padding;
    private String margin;
    private Long shadow;
    private String action;
    private String color;
    private String h5Url;
    private Long status;
    private String content;
    private String shadowColor;
    private String shadowAlpha;
    private String shadowRadius;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public Long getShadow() {
        return shadow;
    }

    public void setShadow(Long shadow) {
        this.shadow = shadow;
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

    public String getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(String shadowColor) {
        this.shadowColor = shadowColor;
    }

    public String getShadowAlpha() {
        return shadowAlpha;
    }

    public void setShadowAlpha(String shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
    }

    public String getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(String shadowRadius) {
        this.shadowRadius = shadowRadius;
    }
}
