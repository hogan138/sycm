package com.shuyun.qapp.bean;


import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/7 15:51
 * 答题对战首页
 */
@Data
public class MainAgainstBean {

    /**
     * battleRule : 测试测试测试规则规则
     * battleUserBPBase : 9866
     * novice : 1
     * intermediate : 1
     * advanced : 1
     */

    private String battleRule;//对战规则
    private Long battleUserBP;//用户积分
    private Long novice;//新手场次积分
    private Long intermediate;//中级场次积分
    private Long advanced;//高级场次积分


}
