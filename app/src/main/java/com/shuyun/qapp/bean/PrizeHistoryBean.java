package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 16:13
 * 开奖历史
 */
@Data
public class PrizeHistoryBean {

    /**
     * scheduleId : 1
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * ticketNum : er63wg35wys34gert
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long scheduleId;
    private String prizeName;
    private String mainPic;
    private Long endTime;
    private Long schedule;
    private String ticketNum;
    private String h5Url;


}
