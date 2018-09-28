package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunxiao on 2018/5/22.
 * 分享需要传给H5的签名
 */

public class AuthHeader {

    @JSONField(name = "Authorization")
    private String authorization;
    private String sycm;

    private String id;

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setSycm(String sycm) {
        this.sycm = sycm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getSycm() {
        return sycm;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AuthHeader{" +
                "Authorization='" + authorization + '\'' +
                ", sycm='" + sycm + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
