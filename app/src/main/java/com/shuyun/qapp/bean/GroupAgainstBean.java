package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/9 9:55
 * 答题对战bean
 */
@Data
public class GroupAgainstBean {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //题组id
    private String name; // 题组名称
    private String picture; // 题组的标签
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long guideId; // 答题指南id，null或者0表示没有答题指南
    private String merchantName; // 题组参与活动的发起方名称
    private Long sorting; // 排序
    private String description; //描述


}
