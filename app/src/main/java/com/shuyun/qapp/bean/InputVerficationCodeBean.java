package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/15.
 * 获取验证码输入参数
 */

public class InputVerficationCodeBean {
    private String phone;//手机号码
    /**
     * 验证码类型
     * 1:登录;
     * 2:绑定手机
     * 3:绑定微信
     */
    private int type;
    private long devId;//开发者id
    private long appId;//应用内部id
    private int v;//接口版本
    private long stamp;//当前时间戳
    private String code;//签名验证码

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDevId(long devId) {
        this.devId = devId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public int getType() {
        return type;
    }

    public long getDevId() {
        return devId;
    }

    public long getAppId() {
        return appId;
    }

    public int getV() {
        return v;
    }

    public long getStamp() {
        return stamp;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "InputVerficationCodeBean{" +
                "phone='" + phone + '\'' +
                ", type=" + type +
                ", devId=" + devId +
                ", appId=" + appId +
                ", v=" + v +
                ", stamp=" + stamp +
                ", code='" + code + '\'' +
                '}';
    }
}
