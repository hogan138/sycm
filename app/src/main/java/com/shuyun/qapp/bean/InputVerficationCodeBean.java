package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/15.
 * 获取验证码输入参数
 */
@Data
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
