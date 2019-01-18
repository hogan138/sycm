package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/9 9:55
 * 答题对战bean
 */
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getSorting() {
        return sorting;
    }

    public void setSorting(Long sorting) {
        this.sorting = sorting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
