package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/3 9:48
 * 宝贝详情
 */
public class PrizeDetailBean {


    /**
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * participate : 5
     * count : 1
     * prizePurpose : 满50可提现至支付宝
     * scheduleStatus : 0
     * userBP : 7885
     * bpcons : 200
     */

    private String prizeName;
    private String mainPic;
    private long endTime;
    private int schedule;
    private int participate;
    private int count;
    private String prizePurpose;
    private int scheduleStatus;
    private int userBP;
    private int bpcons;

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public int getParticipate() {
        return participate;
    }

    public void setParticipate(int participate) {
        this.participate = participate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrizePurpose() {
        return prizePurpose;
    }

    public void setPrizePurpose(String prizePurpose) {
        this.prizePurpose = prizePurpose;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public int getUserBP() {
        return userBP;
    }

    public void setUserBP(int userBP) {
        this.userBP = userBP;
    }

    public int getBpcons() {
        return bpcons;
    }

    public void setBpcons(int bpcons) {
        this.bpcons = bpcons;
    }
}
