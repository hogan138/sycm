package com.shuyun.qapp.bean;

import java.io.Serializable;

/**
 * 项目名称：QMGJ
 * 创建人：gq
 * 创建日期：2018/6/8 11:45
 * <p
 * 宝箱礼品
 */
public class ProductListBean implements Serializable {

    /**
     * showName : 积分
     * type : 2
     * mode : 2
     * purpose : 全民共进平台积分，可以进行抽奖等操作
     * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
     * longImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
     * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
     * id : 1002
     * worthLower : 2
     * worthUpper : 2
     */

    private String showName;
    private int type;
    private int mode;
    private String purpose;
    private String mainImage;
    private String longImage;
    private String shortImage;
    private int id;
    private double worthLower;
    private double worthUpper;

    public String getName() {
        return showName;
    }

    public void setName(String name) {
        this.showName = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getLongImage() {
        return longImage;
    }

    public void setLongImage(String longImage) {
        this.longImage = longImage;
    }

    public String getShortImage() {
        return shortImage;
    }

    public void setShortImage(String shortImage) {
        this.shortImage = shortImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWorthLower() {
        return worthLower;
    }

    public void setWorthLower(double worthLower) {
        this.worthLower = worthLower;
    }

    public double getWorthUpper() {
        return worthUpper;
    }

    public void setWorthUpper(double worthUpper) {
        this.worthUpper = worthUpper;
    }
}
