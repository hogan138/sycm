package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/3 9:48
 * 宝贝详情
 */
@Data
public class PrizeDetailBean {


    /**
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * participate : 5
     * count : 1
     * prizePurpose : 满50可提现至支付宝
     * scheduleStatus : 0
     * userBP : 7885
     * bpcons : 200
     */

    private String prizeName;
    private String mainPic;
    private Long endTime;
    private Long schedule;
    private Long participate;
    private Long count;
    private String prizePurpose;
    private Long scheduleStatus;
    private Long userBP;
    private Long bpcons;


}
