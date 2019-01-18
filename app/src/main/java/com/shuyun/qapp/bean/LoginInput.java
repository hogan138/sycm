package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * Created by sunxiao on 2018/5/15.
 */

public class LoginInput {
    /**
     * 登录模式1:账号+密码登录;2:手机号+验证码登录;3:微信登录
     */
    private Long mode;

    private String account;//用户账号
    private double lng;//用户当前经度
    private double lat;//用户当前纬度
    private String tsn;//登录终端的序列号
    private String salt;//应用随机生成的字符串
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long inviter;//邀请人id   如果用户通过分享注册，需要传递该参数
    private String appVersion;//App的版本号
    private Long devId;//开发者id
    private Long appId;//应用id
    private Long v;//登录接口版本
    private Long stamp;//当前的时间戳
    private String code;//签名计算的code
    private String deviceId;//设备id  数美sdk得到的设备id
    private String examId;//答题免登录传入的答卷id

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setMode(Long mode) {
        this.mode = mode;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setTsn(String tsn) {
        this.tsn = tsn;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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


    public void setCode(String code) {
        this.code = code;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public Long getMode() {

        return mode;
    }

    public Long getStamp() {
        return stamp;
    }

    public String getAccount() {
        return account;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getTsn() {
        return tsn;
    }

    public String getSalt() {
        return salt;
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

    public String getAppVersion() {
        return appVersion;
    }

    public String getCode() {
        return code;
    }

    public void setInviter(Long inviter) {
        this.inviter = inviter;
    }

    public Long getInviter() {
        return inviter;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "LoginInput{" +
                "mode=" + mode +
                ", account='" + account + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", tsn='" + tsn + '\'' +
                ", salt='" + salt + '\'' +
                ", inviter=" + inviter +
                ", appVersion='" + appVersion + '\'' +
                ", devId=" + devId +
                ", appId=" + appId +
                ", v=" + v +
                ", stamp=" + stamp +
                ", code='" + code + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
