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
    private Long devId;//开发者id
    private Long appId;//应用内部id
    private Long v;//接口版本
    private Long stamp;//当前时间戳
    private String code;//签名验证码

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public void setStamp(Long stamp) {
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

    public Long getDevId() {
        return devId;
    }

    public Long getAppId() {
        return appId;
    }

    public Long getV() {
        return v;
    }

    public Long getStamp() {
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
