package com.shuyun.qapp.bean;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/19.
 */

public class XruleActivityPrizeBean {
    /**
     * 答题结果
     * 0——未中奖
     * 1——中奖
     */
    private int result;

    private String worth;//奖品的价值
    private List<MinePrize> prize;

    public int getResult() {
        return result;
    }

    public String getWorth() {
        return worth;
    }

    public List<MinePrize> getPrize() {
        return prize;
    }

    @Override
    public String toString() {
        return "XruleActivityPrizeBean{" +
                "result=" + result +
                ", worth='" + worth + '\'' +
                ", prize=" + prize +
                '}';
    }
}
