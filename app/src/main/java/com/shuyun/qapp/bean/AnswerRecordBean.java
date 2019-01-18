package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * Created by sunxiao on 2018/5/21.
 * 用户答题记录
 */
public class AnswerRecordBean {

    private String id;//答题id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId;//题组id
    private String picture;//题组主图
    private String time;//答题时间
    private Double accuracy;//正确率
    private String title;//题组名称
    private String merchantName;//商户名称
    /**
     * 是否中奖
     * 0——未中奖
     * 1——中奖
     */
    private Long result;

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getPicture() {
        return picture;
    }

    public String getTime() {
        return time;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public Long getResult() {
        return result;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "AnswerRecordBean{" +
                "id='" + id + '\'' +
                ", groupId=" + groupId +
                ", picture='" + picture + '\'' +
                ", time='" + time + '\'' +
                ", accuracy=" + accuracy +
                ", title='" + title + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", result=" + result +
                '}';
    }
}
