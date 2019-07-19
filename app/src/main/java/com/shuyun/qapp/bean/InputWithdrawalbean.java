package com.shuyun.qapp.bean;

import java.util.Arrays;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/7.
 * 以json的形式传给服务端
 * 涉及金额的用BigDecimal类型
 */
@Data
public class InputWithdrawalbean {

    private String amount;//提现金额
    private int type;//现金类型1:现金提现;2:红包提现
    private String cardNo;//支付宝账号
    private String realname;//支付宝账号名称
    private String bankId;
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
