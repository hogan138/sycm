package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/5/3.
 * 推荐题组 热门题组 子题组
 */

public class GroupBean {
    private int id;//题组id
    private String name;//题组名称
    private int parentId;//上级题组
    private String picture;//题组主图
    private int opportunity;//消耗答题机会次数
    private int guideId;//指南id
    private String merchantName;//题组参与的活动商户名称
    private String h5Url; //答题url

    public String getH5Url() {
        return h5Url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public String getPicture() {
        return picture;
    }

    public int getOpportunity() {
        return opportunity;
    }

    public int getGuideId() {
        return guideId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    @Override
    public String toString() {
        return "GroupBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", picture='" + picture + '\'' +
                ", opportunity=" + opportunity +
                ", guideId=" + guideId +
                ", merchantName='" + merchantName + '\'' +
                '}';
    }
}
