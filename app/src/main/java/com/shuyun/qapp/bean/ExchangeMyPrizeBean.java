package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 16:43
 * 积分夺宝我的奖券
 */
public class ExchangeMyPrizeBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * participate : 3
     * userTicketCount : 1
     * scheduleStatus : 0
     */

    private Long scheduleId;
    private String prizeName;
    private String mainPic;
    private long endTime;
    private Long schedule;
    private Long participate;
    private Long userTicketCount;
    private Long scheduleStatus;
    private String h5Url;

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

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

    public Long getUserTicketCount() {
        return userTicketCount;
    }

    public void setUserTicketCount(Long userTicketCount) {
        this.userTicketCount = userTicketCount;
    }

    public Long getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Long scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }
}
