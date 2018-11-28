package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: BoxRecordBean
 * @Description: 宝箱记录bean
 * @Author: ganquan
 * @CreateDate: 2018/11/28 10:19
 */
public class BoxRecordBean {


    /**
     * boxTime : 2018-11-26
     * remark : 该宝箱开启后，获得了 |积分5个
     * title : 您通过答题获得了宝箱
     */

    private String boxTime;
    private String remark;
    private String title;

    public String getBoxTime() {
        return boxTime;
    }

    public void setBoxTime(String boxTime) {
        this.boxTime = boxTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
