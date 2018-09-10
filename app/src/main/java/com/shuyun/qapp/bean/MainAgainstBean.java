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
    private int battleUserBP;//用户积分
    private int novice;//新手场次积分
    private int intermediate;//中级场次积分
    private int advanced;//高级场次积分

    public String getBattleRule() {
        return battleRule;
    }

    public void setBattleRule(String battleRule) {
        this.battleRule = battleRule;
    }

    public int getBattleUserBP() {
        return battleUserBP;
    }

    public void setBattleUserBP(int battleUserBP) {
        this.battleUserBP = battleUserBP;
    }

    public int getNovice() {
        return novice;
    }

    public void setNovice(int novice) {
        this.novice = novice;
    }

    public int getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(int intermediate) {
        this.intermediate = intermediate;
    }

    public int getAdvanced() {
        return advanced;
    }

    public void setAdvanced(int advanced) {
        this.advanced = advanced;
    }
}
