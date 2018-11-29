package com.shuyun.qapp.bean;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 14:46
 * 积分夺宝全部奖品
 */
public class IntegralAllPrizeBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * mainPic : http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png
     * endTime : 1535695570000
     * schedule : 20187311
     * count : 1
     * scheduleStatus : 0
     * bpcons : 200
     */

    private Long scheduleId; //档期id
    private String prizeName; //奖品名称
    private String mainPic; //主图
    private long endTime; //结束时间
    private Long schedule; //期号
    private Long count; //奖品数量
    private Long scheduleStatus; //开奖状态
    private Long bpcons; //消耗积分
    private Long participate; //参与人数
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Long scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public Long getBpcons() {
        return bpcons;
    }

    public void setBpcons(Long bpcons) {
        this.bpcons = bpcons;
    }

    public Long getParticipate() {
        return participate;
    }

    public void setParticipate(Long participate) {
        this.participate = participate;
    }
}
