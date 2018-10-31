package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 推荐题组 大家都在答题组
 */

public class GroupBean {
    /**
     * id : 611
     * name : 南孔圣地 衢州有礼
     * parentId : 101
     * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/610/quzhou.png
     * opportunity : 0
     * guideId : 2
     * merchantName : 衢州广电传媒集团
     * sorting : 0
     * h5Url : http://192.168.3.137:8080/h5/index.html
     * tags : [{"groupId":611,"remark":"10%","tagName":"获得积分"},{"groupId":611,"remark":"60%","tagName":"获得现金"},{"groupId":611,"remark":"80%","tagName":"准确率"}]
     * opportunityLabel : 不消耗答题次数
     */

    private int id;//题组id
    private String name;//题组名称
    private int parentId;//上级题组
    private String picture;//题组主图
    private int opportunity;//消耗答题机会次数
    private int guideId;//指南id
    private String merchantName;//题组参与的活动商户名称
    private int sorting;
    private String h5Url; //答题url
    private String opportunityLabel; //不消耗答题次数
    private String tag; //答题攻略
    private List<TagsBean> tags;  //积分、现金、准确率


    //是否推荐
    private boolean recommend;
    private String remark;

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(int opportunity) {
        this.opportunity = opportunity;
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getOpportunityLabel() {
        return opportunityLabel;
    }

    public void setOpportunityLabel(String opportunityLabel) {
        this.opportunityLabel = opportunityLabel;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * groupId : 611
         * remark : 10%
         * tagName : 获得积分
         */

        private int groupId;
        private String remark;
        private String tagName;

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }


}
