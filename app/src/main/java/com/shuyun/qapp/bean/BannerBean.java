package com.shuyun.qapp.bean;

import java.util.List;

/**
 * Created by sunxiao on 2018/4/25.
 */

public class BannerBean {

    private Long id;
    private String name;//名称
    private String description;//描述
    private String remark;//描述
    private String picture;//主图
    private String url;//跳转的地址
    private Long type;//类型 1、外部链接;2、内部链接;3、内部功能
    private String h5Url; //答题url
    private String action;
    private String content;
    private Long isLogin;

    //任意位置logo配置
    private List<AdConfigs> adConfigs;

    public Long getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Long isLogin) {
        this.isLogin = isLogin;
    }

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public String getH5Url() {
        return h5Url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRemark() {
        return remark;
    }

    public String getPicture() {
        return picture;
    }

    public String getUrl() {
        return url;
    }

    public Long getType() {
        return type;
    }

    public List<AdConfigs> getAdConfigs() {
        return adConfigs;
    }

    public void setAdConfigs(List<AdConfigs> adConfigs) {
        this.adConfigs = adConfigs;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                '}';
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
