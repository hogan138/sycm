package com.shuyun.qapp.bean;


/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/7 15:51
 * 答题对战首页
 */
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

    public String getBattleRule() {
        return battleRule;
    }

    public void setBattleRule(String battleRule) {
        this.battleRule = battleRule;
    }

    public Long getBattleUserBP() {
        return battleUserBP;
    }

    public void setBattleUserBP(Long battleUserBP) {
        this.battleUserBP = battleUserBP;
    }

    public Long getNovice() {
        return novice;
    }

    public void setNovice(Long novice) {
        this.novice = novice;
    }

    public Long getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(Long intermediate) {
        this.intermediate = intermediate;
    }

    public Long getAdvanced() {
        return advanced;
    }

    public void setAdvanced(Long advanced) {
        this.advanced = advanced;
    }
}
