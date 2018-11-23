package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/12 13:54
 */
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

    private int type; //场次
    private Long questionId; //上一题目题目id
    private int userScore; //用户分数
    private int robotScore; //机器人分数
    private String robotId; //机器人id
    private String userOptionId; //用户选项id
    private double userConst; //用户耗时
    private Long nextQuestionId; //当前题目id
    private String questionArry; //选项id
    private int isLast;
    private String robotOptionId; //机器人选项id
    private String examId;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getRobotOptionId() {
        return robotOptionId;
    }

    public void setRobotOptionId(String robotOptionId) {
        this.robotOptionId = robotOptionId;
    }

    public int getIsLast() {
        return isLast;
    }

    public void setIsLast(int isLast) {
        this.isLast = isLast;
    }

    public String getQuestionArry() {
        return questionArry;
    }

    public void setQuestionArry(String questionArry) {
        this.questionArry = questionArry;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getRobotScore() {
        return robotScore;
    }

    public void setRobotScore(int robotScore) {
        this.robotScore = robotScore;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getUserOptionId() {
        return userOptionId;
    }

    public void setUserOptionId(String userOptionId) {
        this.userOptionId = userOptionId;
    }

    public double getUserConst() {
        return userConst;
    }

    public void setUserConst(double userConst) {
        this.userConst = userConst;
    }

    public Long getNextQuestionId() {
        return nextQuestionId;
    }

    public void setNextQuestionId(Long nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }
}
