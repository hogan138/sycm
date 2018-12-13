package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 广告页面bean
 */

public class AdBean implements Parcelable {
    private Long timeout;//倒计时，秒  0表示需要手动跳过
    private List<AdInfo> ad;//广告信息

    private String boxUrl; //开宝箱的H5跳转地址
    private String examUrl;//答题的H5跳转地址

    protected AdBean(Parcel in) {
        if (in.readByte() == 0) {
            timeout = null;
        } else {
            timeout = in.readLong();
        }
        boxUrl = in.readString();
        examUrl = in.readString();
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel in) {
            return new AdBean(in);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (timeout == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timeout);
        }
        dest.writeString(boxUrl);
        dest.writeString(examUrl);
    }


    public static class AdInfo implements Parcelable {
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
        private Long isLogin;

        protected AdInfo(Parcel in) {
            if (in.readByte() == 0) {
                type = null;
            } else {
                type = in.readLong();
            }
            url = in.readString();
            if (in.readByte() == 0) {
                model = null;
            } else {
                model = in.readLong();
            }
            content = in.readString();
            if (in.readByte() == 0) {
                isLogin = null;
            } else {
                isLogin = in.readLong();
            }
        }

        public static final Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
            @Override
            public AdInfo createFromParcel(Parcel in) {
                return new AdInfo(in);
            }

            @Override
            public AdInfo[] newArray(int size) {
                return new AdInfo[size];
            }
        };

        public Long getIsLogin() {
            return isLogin;
        }

        public void setIsLogin(Long isLogin) {
            this.isLogin = isLogin;
        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (type == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(type);
            }
            dest.writeString(url);
            if (model == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(model);
            }
            dest.writeString(content);
            if (isLogin == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(isLogin);
            }
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
