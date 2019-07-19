package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 16:43
 * 积分夺宝我的奖券
 */
@Data
public class ExchangeMyPrizeBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * participate : 3
     * userTicketCount : 1
     * scheduleStatus : 0
     */

    private Long scheduleId;
    private String prizeName;
    private String mainPic;
    private Long endTime;
    private Long schedule;
    private Long participate;
    private Long userTicketCount;
    private Long scheduleStatus;
    private String h5Url;


}
