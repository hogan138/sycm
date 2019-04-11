package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: ScoreExchangeResult
 * @Description: 道具兑换结果
 * @Author: ganquan
 * @CreateDate: 2019/3/26 14:48
 */
public class ScoreExchangeResult {

    /**
     * bp : 3608
     */

    private Long bp;
    private String title;
    private String picture;

    public Long getBp() {
        return bp;
    }

    public void setBp(Long bp) {
        this.bp = bp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
