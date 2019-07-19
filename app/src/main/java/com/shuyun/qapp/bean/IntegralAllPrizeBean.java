package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 14:46
 * 积分夺宝全部奖品
 */
@Data
public class IntegralAllPrizeBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * count : 1
     * scheduleStatus : 0
     * bpcons : 200
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long scheduleId; //档期id
    private String prizeName; //奖品名称
    private String mainPic; //主图
    private Long endTime; //结束时间
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long schedule; //期号
    private Long count; //奖品数量
    private Long scheduleStatus; //开奖状态
    private Long bpcons; //消耗积分
    private Long participate; //参与人数
    private String h5Url;


}
