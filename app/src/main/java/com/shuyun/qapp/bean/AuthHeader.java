package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/22.
 * 分享需要传给H5的签名
 */
@Data
public class AuthHeader {

    @JSONField(name = "Authorization")
    private String authorization;
    private String sycm;

    private String id;



    @Override
    public String toString() {
        return "AuthHeader{" +
                "Authorization='" + authorization + '\'' +
                ", sycm='" + sycm + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
