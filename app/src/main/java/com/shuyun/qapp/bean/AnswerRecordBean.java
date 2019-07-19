package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/21.
 * 用户答题记录
 */
@Data
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
