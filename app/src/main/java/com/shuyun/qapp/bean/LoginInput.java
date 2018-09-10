package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/15.
 */

public class LoginInput {
    /**
     * 登录模式1:账号+密码登录;2:手机号+验证码登录;3:微信登录
     */
    private int mode;

    private String account;//用户账号
    private double lng;//用户当前经度
    private double lat;//用户当前纬度
    private String tsn;//登录终端的序列号
    private String salt;//应用随机生成的字符串
    private int inviter;//邀请人id   如果用户通过分享注册，需要传递该参数
    private String appVersion;//App的版本号
    private int devId;//开发者id
    private int appId;//应用id
    private int v;//登录接口版本
    private long stamp;//当前的时间戳
    private String code;//签名计算的code
    private String deviceId;//设备id  数美sdk得到的设备id

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setMode(int mode) {
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

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setV(int v) {
        this.v = v;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public int getMode() {

        return mode;
    }

    public long getStamp() {
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

    public int getDevId() {
        return devId;
    }

    public int getAppId() {
        return appId;
    }

    public int getV() {
        return v;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getCode() {
        return code;
    }

    public void setInviter(int inviter) {
        this.inviter = inviter;
    }

    public int getInviter() {
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
