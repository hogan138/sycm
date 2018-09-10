package com.shuyun.qapp.bean;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/8.
 * 积分抽奖
 */

public class IntegralPrizeBean {


    /**
     * bp : 100
     * result : 1
     * worth : 1.0
     * prize : [{"id":"7b8f3a126b334160bc194805c9826b86","name":"1元人民币","type":101}]
     */

    /**
     * 本次抽奖消耗的积分
     */
    private int bp;
    /**
     * 结果0:未中奖;1:中奖
     */
    private int result;
    /**
     * 奖品的价值
     */
    private double worth;
    /**
     * 中奖的奖品
     */
    private List<MinePrize> prize;

    public int getBp() {
        return bp;
    }

    public int getResult() {
        return result;
    }

    public double getWorth() {
        return worth;
    }

    public List<MinePrize> getPrize() {
        return prize;
    }

    @Override
    public String toString() {
        return "IntegralPrizeBean{" +
                "bp=" + bp +
                ", result=" + result +
                ", worth=" + worth +
                ", prize=" + prize +
                '}';
    }
}
