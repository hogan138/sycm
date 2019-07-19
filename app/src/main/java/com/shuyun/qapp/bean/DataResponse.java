package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 公用bean封装
 */

@Data
public class DataResponse<T> {
    private String err;//请求响应码
    private String msg;//错误描述
    private int ver;//接口版本
    private T dat;//请求的具体结果


    @Override
    public String toString() {
        return "DataResponse{" +
                "err='" + err + '\'' +
                ", msg='" + msg + '\'' +
                ", ver=" + ver +
                ", dat=" + dat +
                '}';
    }

    public boolean isSuccees() {
        return "00000".equals(err);
    }
}
