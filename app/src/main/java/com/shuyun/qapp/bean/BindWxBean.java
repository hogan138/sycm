package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/23.
 */

public class BindWxBean {
    private String phone;//绑定的手机号码
    private String code;//短信验证码
    private int inviter;//邀请人id 如果本次是通过邀请分享进来的，需要提交邀请人id

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }

    public int getInviter() {
        return inviter;
    }

    @Override
    public String toString() {
        return "BindWxBean{" +
                "phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", inviter=" + inviter +
                '}';
    }
}
