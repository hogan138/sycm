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

    private int scheduleId;
    private String prizeName;
    private String mainPic;
    private long endTime;
    private int schedule;
    private int participate;
    private int userTicketCount;
    private int scheduleStatus;
    private String h5Url;

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
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

    public int getUserTicketCount() {
        return userTicketCount;
    }

    public void setUserTicketCount(int userTicketCount) {
        this.userTicketCount = userTicketCount;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }
}
