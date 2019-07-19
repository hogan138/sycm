package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/15.
 */
@Data
public class LoginInput {
    /**
     * 登录模式1:账号+密码登录;2:手机号+验证码登录;3:微信登录
     */
    private Long mode;

    private String account;//用户账号
    private Double lng;//用户当前经度
    private Double lat;//用户当前纬度
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
