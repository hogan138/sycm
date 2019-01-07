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

    private Long id;//题组id
    private String name;//题组名称
    private Long parentId;//上级题组
    private String picture;//题组主图
    private Long opportunity;//消耗答题机会次数
    private Long guideId;//指南id
    private String merchantName;//题组参与的活动商户名称
    private Long sorting;
    private String h5Url; //答题url
    private String opportunityLabel; //不消耗答题次数
    private String tag; //答题攻略
    private List<TagsBean> tags;  //积分、现金、准确率

    //任意位置logo配置
    private List<AdConfigs> adConfigs;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Long opportunity) {
        this.opportunity = opportunity;
    }

    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getSorting() {
        return sorting;
    }

    public void setSorting(Long sorting) {
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

    public List<AdConfigs> getAdConfigs() {
        return adConfigs;
    }

    public void setAdConfigs(List<AdConfigs> adConfigs) {
        this.adConfigs = adConfigs;
    }

    public static class TagsBean {
        /**
         * groupId : 611
         * remark : 10%
         * tagName : 获得积分
         */

        private Long groupId;
        private String remark;
        private String tagName;

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
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

    public static class AdConfigs {

        /**
         * type : 1
         * location : 8
         * width : 150
         * height : 39
         * padding : 8,8,8,8
         * shadow : 1
         * shadowColor : #000000
         * shadowAlpha : 0.2
         * shadowRadius : 8,8,0,0
         * imageUrl : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/pingan/pahys_logo2.png
         */

        private Long type;
        private Long location;
        private Long width;
        private Long height;
        private String padding;
        private String margin;
        private Long shadow;
        private String shadowColor;
        private String shadowAlpha;
        private String shadowRadius;
        private String imageUrl;

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public Long getLocation() {
            return location;
        }

        public void setLocation(Long location) {
            this.location = location;
        }

        public Long getWidth() {
            return width;
        }

        public void setWidth(Long width) {
            this.width = width;
        }

        public Long getHeight() {
            return height;
        }

        public void setHeight(Long height) {
            this.height = height;
        }

        public String getPadding() {
            return padding;
        }

        public void setPadding(String padding) {
            this.padding = padding;
        }

        public String getMargin() {
            return margin;
        }

        public void setMargin(String margin) {
            this.margin = margin;
        }

        public Long getShadow() {
            return shadow;
        }

        public void setShadow(Long shadow) {
            this.shadow = shadow;
        }

        public String getShadowColor() {
            return shadowColor;
        }

        public void setShadowColor(String shadowColor) {
            this.shadowColor = shadowColor;
        }

        public String getShadowAlpha() {
            return shadowAlpha;
        }

        public void setShadowAlpha(String shadowAlpha) {
            this.shadowAlpha = shadowAlpha;
        }

        public String getShadowRadius() {
            return shadowRadius;
        }

        public void setShadowRadius(String shadowRadius) {
            this.shadowRadius = shadowRadius;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

}
