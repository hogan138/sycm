package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/7/12 14:04
 * 机器人答题出参
 */
public class RobotShowBean {


    /**
     * winOrLose : true
     * answerId : 219788
     * answer : 3
     * timeConsuming : 3.6194072
     * robotId : 3717e0db7f9440e3a96be966b3f455bd
     * userScore : 110
     * robotScore : 110
     */

    private boolean winOrLose;
    private String answerId;
    private String answer;//机器人选择的答案0 1 2 3
    private double timeConsuming;//机器人答题耗时
    private String robotId;
    private int userScore;
    private int robotScore;

    public boolean isWinOrLose() {
        return winOrLose;
    }

    public void setWinOrLose(boolean winOrLose) {
        this.winOrLose = winOrLose;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(double timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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
}
