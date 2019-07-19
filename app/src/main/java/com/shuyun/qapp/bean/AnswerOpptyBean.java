package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/21.
 * 答题机会领取剩余时长
 */

@Data
public class AnswerOpptyBean {

    private Long oppty;//用户当前的答题机会次数
    private Long remainder;//距离下次领取时长

    @Override
    public String toString() {
        return "AnswerOpptyBean{" +
                "oppty=" + oppty +
                ", remainder=" + remainder +
                '}';
    }
}
