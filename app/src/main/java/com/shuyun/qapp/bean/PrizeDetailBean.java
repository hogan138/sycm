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
    private Long endTime;
    private Long schedule;
    private Long participate;
    private Long count;
    private String prizePurpose;
    private Long scheduleStatus;
    private Long userBP;
    private Long bpcons;

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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getSchedule() {
        return schedule;
    }

    public void setSchedule(Long schedule) {
        this.schedule = schedule;
    }

    public Long getParticipate() {
        return participate;
    }

    public void setParticipate(Long participate) {
        this.participate = participate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getPrizePurpose() {
        return prizePurpose;
    }

    public void setPrizePurpose(String prizePurpose) {
        this.prizePurpose = prizePurpose;
    }

    public Long getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Long scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public Long getUserBP() {
        return userBP;
    }

    public void setUserBP(Long userBP) {
        this.userBP = userBP;
    }

    public Long getBpcons() {
        return bpcons;
    }

    public void setBpcons(Long bpcons) {
        this.bpcons = bpcons;
    }
}
