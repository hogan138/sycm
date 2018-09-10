package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/22.
 * 分享需要传给H5的签名
 */

public class AuthHeader {

    private String Authorization;
    private String sycm;

    private String id;

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public void setSycm(String sycm) {
        this.sycm = sycm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorization() {
        return Authorization;
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
                "Authorization='" + Authorization + '\'' +
                ", sycm='" + sycm + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
