package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AnyPositionBean
 * @Description: 任意位置bean
 * @Author: ganquan
 * @CreateDate: 2019/1/7 11:04
 */
public class AnyPositionBean {

    /**
     * type : 1
     * location : 8
     * width : 150
     * height : 39
     * padding : 8,8,8,8
     * shadow : 1
     * shadowColor : #000000
     * shadowAlpha : 0.2
     * shadowRadius : 8,8,0,0
     * imageUrl : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/pingan/pahys_logo2.png
     */

    private Long type;
    private Long location;
    private Long width;
    private Long height;
    private String padding;
    private String margin; //广告四周间距
    private Long shadow;
    private String shadowColor;
    private String shadowAlpha;
    private String shadowRadius;
    private String imageUrl;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
