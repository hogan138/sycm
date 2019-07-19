package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: BoxRecordBean
 * @Description: 宝箱记录bean
 * @Author: ganquan
 * @CreateDate: 2018/11/28 10:19
 */
@Data
public class BoxRecordBean {


    /**
     * boxTime : 2018-11-26
     * remark : 该宝箱开启后，获得了 |积分5个
     * title : 您通过答题获得了宝箱
     */

    private String boxTime;
    private String remark;
    private String title;



}
