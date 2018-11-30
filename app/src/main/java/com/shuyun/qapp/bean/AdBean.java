package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 广告页面bean
 */

public class AdBean {
    private Long timeout;//倒计时，秒  0表示需要手动跳过
    private List<AdInfo> ad;//广告信息

    private String boxUrl; //开宝箱的H5跳转地址
    private String examUrl;//答题的H5跳转地址

    public String getBoxUrl() {
        return boxUrl;
    }

    public void setBoxUrl(String boxUrl) {
        this.boxUrl = boxUrl;
    }

    public String getExamUrl() {
        return examUrl;
    }

    public void setExamUrl(String examUrl) {
        this.examUrl = examUrl;
    }


    public static class AdInfo {
        /**
         * 广告类型
         * 1——图片
         * 2——网页
         */
        private Long type;
        /**
         * 广告地址
         * 图片或者网页地址
         */
        private String url;
        /**
         * 点击模型
         * 1——外部链接
         * 2——内部链接
         * 3——题组跳转
         */
        private Long model;
        private String content;//跳转的地址

        public void setType(Long type) {
            this.type = type;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setModel(Long model) {
            this.model = model;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public Long getModel() {
            return model;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "AdInfo{" +
                    "type=" + type +
                    ", url='" + url + '\'' +
                    ", model=" + model +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setAd(List<AdInfo> ad) {
        this.ad = ad;
    }

    public Long getTimeout() {
        return timeout;
    }

    public List<AdInfo> getAd() {
        return ad;
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "timeout=" + timeout +
                ", ad=" + ad +
                ", boxUrl='" + boxUrl + '\'' +
                ", examUrl='" + examUrl + '\'' +
                '}';
    }
}
