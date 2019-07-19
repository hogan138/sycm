package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/12 14:04
 * 机器人答题出参
 */
@Data
public class RobotShowBean {


    /**
     * winOrLose : true
     * answerId : 219788
     * answer : 3
     * timeConsuming : 3.6194072
     * robotId : 3717e0db7f9440e3a96be966b3f455bd
     * userScore : 110
     * robotScore : 110
     */

    private Boolean winOrLose;
    private String answerId;
    private String answer;//机器人选择的答案0 1 2 3
    private Double timeConsuming;//机器人答题耗时
    private String robotId;
    private Integer userScore;
    private Integer robotScore;


}
