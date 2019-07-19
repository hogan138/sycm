package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 完善提现信息bean
 */
@Data
public class SubmitWithdrawInfoBean {

    private int type; //类型
    private String cardNo; //账号
    private String name; //姓名


    public SubmitWithdrawInfoBean(int type, String cardNo, String name) {
        this.type = type;
        this.cardNo = cardNo;
        this.name = name;
    }

}
