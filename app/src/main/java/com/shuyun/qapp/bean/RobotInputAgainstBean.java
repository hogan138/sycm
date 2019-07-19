package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/12 13:54
 */
@Data
public class RobotInputAgainstBean {


    /**
     * type : 1
     * isLast : 0
     * questionId : 100544
     * userScore : 0
     * robotScore : 110
     * robotId : cf002b825b2549d3bc1e8636ccdb73d1
     * userOptionId : 202127
     * userConst : 0.1
     * nextQuestionId : 105364
     */

    private Integer type; //场次
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long questionId; //上一题目题目id
    private Integer userScore; //用户分数
    private Integer robotScore; //机器人分数
    private String robotId; //机器人id
    private String userOptionId; //用户选项id
    private Double userConst; //用户耗时
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long nextQuestionId; //当前题目id
    private String questionArry; //选项id
    private Integer isLast;
    private String robotOptionId; //机器人选项id
    private String examId;


}
