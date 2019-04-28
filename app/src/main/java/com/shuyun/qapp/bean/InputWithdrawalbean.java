package com.shuyun.qapp.bean;

import java.util.Arrays;

/**
 * Created by sunxiao on 2018/5/7.
 * 以json的形式传给服务端
 * 涉及金额的用BigDecimal类型
 */

public class InputWithdrawalbean {

    private String amount;//提现金额
    private int type;//现金类型1:现金提现;2:红包提现
    private String cardNo;//支付宝账号
    private String realname;//支付宝账号名称
    private String bankId;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String[] getReds() {
        return reds;
    }

    public void setReds(String[] reds) {
        this.reds = reds;
    }

    private String[] reds;//针对哪几个红包提现,多个红包id用逗号分隔;Type=2时必须加密传输

    public InputWithdrawalbean() {
    }

    public InputWithdrawalbean(String amount, int type, String cardNo, String realname, String[] reds) {
        this.amount = amount;
        this.type = type;
        this.cardNo = cardNo;
        this.realname = realname;
        this.reds = reds;
    }

    public InputWithdrawalbean(String amount, int type, String[] reds, String bankId) {
        this.amount = amount;
        this.type = type;
        this.reds = reds;
        this.bankId = bankId;
    }

    @Override
    public String toString() {
        return "InputWithdrawalbean{" +
                "amount='" + amount + '\'' +
                ", type=" + type +
                ", cardNo='" + cardNo + '\'' +
                ", realname='" + realname + '\'' +
                ", reds=" + Arrays.toString(reds) +
                '}';
    }
}
