package com.shuyun.qapp.bean;

/**
 * 完善提现信息bean
 */
public class SubmitWithdrawInfoBean {

    private int type; //类型
    private String cardNo; //账号
    private String name; //姓名

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubmitWithdrawInfoBean(int type, String cardNo, String name) {
        this.type = type;
        this.cardNo = cardNo;
        this.name = name;
    }

}
