package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 微信信息bean
 * 创建日期：2018/7/3 9:49
 */
public class UserWxInfo {

    private String nickname; // 昵称
    private Long wxBind; // 是否已经绑定微信
    private String wxHeader; // 微信头像

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getWxBind() {
        return wxBind;
    }

    public void setWxBind(Long wxBind) {
        this.wxBind = wxBind;
    }

    public String getWxHeader() {
        return wxHeader;
    }

    public void setWxHeader(String wxHeader) {
        this.wxHeader = wxHeader;
    }

}
