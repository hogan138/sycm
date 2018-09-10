package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/21.
 * 答题机会领取剩余时长
 */

public class AnswerOpptyBean {

    private int oppty;//用户当前的答题机会次数
    private long remainder;//距离下次领取时长

    public int getOppty() {
        return oppty;
    }

    public long getRemainder() {
        return remainder;
    }

    @Override
    public String toString() {
        return "AnswerOpptyBean{" +
                "oppty=" + oppty +
                ", remainder=" + remainder +
                '}';
    }
}
