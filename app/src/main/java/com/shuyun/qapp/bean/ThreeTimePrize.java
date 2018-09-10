package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/19.
 */

public class ThreeTimePrize {
    private int  wons;//用户在本活动中赢取的答题次数
    /**
     * 状态
     * 0——不满足扩展规则条件
     * 1——已经满足，但是未抽奖
     * 2——已经抽奖完毕
     */
    private int status;
    private String boxId;//宝箱id  如果status=2，则返回抽奖的宝箱id

    public int getWons() {
        return wons;
    }

    public int getStatus() {
        return status;
    }

    public String getBoxId() {
        return boxId;
    }

    @Override
    public String toString() {
        return "ThreeTimePrize{" +
                "wons=" + wons +
                ", status=" + status +
                ", boxId='" + boxId + '\'' +
                '}';
    }
}
