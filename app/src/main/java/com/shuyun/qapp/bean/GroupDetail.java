package com.shuyun.qapp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/4.
 * 题组详情
 */

public class GroupDetail implements Parcelable{

    /**
     * id : 108
     * name : 中国诗词大全
     * description : <p style="text-align:center">赏中华诗词 &nbsp;寻文化基因 &nbsp;品生活之美</p>
     * <p>
     * remark : <p style="text-align:center">正确答题随机获得现金或其它奖励</p>
     * <p>
     * <p style="text-align:center">最高有机会获得5000元现金奖</p>
     * <p>
     * picture : null
     * mainImage : null
     * examImage : null
     * shareImage : null
     * bindImage : null
     * shareTitle : null
     * shareContent : null
     * guide : null
     * activity : {"name":"系统默认活动","merchantName":"舒云传媒","showRule":1,"showGuide":0,"rule":{"bulletin":null},"xrule":[{"id":1,"name":"连续3天答题正确抽奖","description":null,"remark":null}]}
     */

    private int id;//题组id
    private String name;//题组名称
    private String description;//题组描述
    private String remark;//题组备注
    private String picture;//题组在app首页和分类页上展示的图片
    private String mainImage;//首页底图
    private String examImage;//答题页底图
    private String shareImage;//分享页底图
    private String bindImage;//微信绑定底图
    private String shareTitle;//分享文案标题
    private String shareContent;//分享文案内容
    /**
     * 题组的当前状态
     * 10-——草稿;
     * 20——审核;
     * 50——正常;
     * 99——下线;
     * -1——废除
     */
    private int status;
    /**
     * 用户剩余的答题次数
     * 0表示没有答题次数了。如果是限制了每日答题次数的,
     * 则该值不能超过每日答题次数限制数量
     */
    private int opportunity;
    private int won;//用户今日答对次数
    /**
     * 用户实名认证标志
     * 0——未实名认证
     * 1——已实名认证
     * 2——审核中
     * 3——未通过
     * 4——拉黑
     */
    private int certification;
    private ActivityBean activity;//该题组参加的活动中当前运营中的活动
    private Guide guide;//题组指南

    public GroupDetail(){

    }

    protected GroupDetail(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        remark = in.readString();
        picture = in.readString();
        mainImage = in.readString();
        examImage = in.readString();
        shareImage = in.readString();
        bindImage = in.readString();
        shareTitle = in.readString();
        shareContent = in.readString();
        status = in.readInt();
        opportunity = in.readInt();
        won = in.readInt();
        certification = in.readInt();
        activity = in.readParcelable(ActivityBean.class.getClassLoader());
        guide = in.readParcelable(Guide.class.getClassLoader());
    }

    public static final Creator<GroupDetail> CREATOR = new Creator<GroupDetail>() {
        @Override
        public GroupDetail createFromParcel(Parcel in) {
            return new GroupDetail(in);
        }

        @Override
        public GroupDetail[] newArray(int size) {
            return new GroupDetail[size];
        }
    };

    public int getCertification() {
        return certification;
    }

    public int getStatus() {
        return status;
    }

    public int getOpportunity() {
        return opportunity;
    }

    public int getWon() {
        return won;
    }

    public int getId() {
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

    public String getMainImage() {
        return mainImage;
    }

    public String getExamImage() {
        return examImage;
    }

    public String getShareImage() {
        return shareImage;
    }

    public String getBindImage() {
        return bindImage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public Guide getGuide() {
        return guide;
    }

    public ActivityBean getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return "GroupDetail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", picture='" + picture + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", examImage='" + examImage + '\'' +
                ", shareImage='" + shareImage + '\'' +
                ", bindImage='" + bindImage + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", status=" + status +
                ", opportunity=" + opportunity +
                ", won=" + won +
                ", activity=" + activity +
                ", guide=" + guide +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(remark);
        dest.writeString(picture);
        dest.writeString(mainImage);
        dest.writeString(examImage);
        dest.writeString(shareImage);
        dest.writeString(bindImage);
        dest.writeString(shareTitle);
        dest.writeString(shareContent);
        dest.writeInt(status);
        dest.writeInt(opportunity);
        dest.writeInt(won);
        dest.writeInt(certification);
        dest.writeParcelable(activity, flags);
        dest.writeParcelable(guide, flags);
    }


    public static class ActivityBean implements Parcelable{
        /**
         * name : 系统默认活动
         * merchantName : 舒云传媒
         * showRule : 1
         * showGuide : 0
         * rule : {"bulletin":null}
         * xrule : [{"id":1,"name":"连续3天答题正确抽奖","description":null,"remark":null}]
         */

        private String name;//活动名称
        private int id;//活动id
        private String merchantName;//商户名称
        private int showRule;//是否强化显示规则0:不显示;1:显示
        private int showGuide;//是否展现指南0:不显示;1:显示
        private RuleBean rule;//活动适用的规则
        private List<XruleBean> xrule;//扩展规则

        public ActivityBean(){

        }

        protected ActivityBean(Parcel in) {
            name = in.readString();
            id = in.readInt();
            merchantName = in.readString();
            showRule = in.readInt();
            showGuide = in.readInt();
            rule = in.readParcelable(RuleBean.class.getClassLoader());
            xrule = in.createTypedArrayList(XruleBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeInt(id);
            dest.writeString(merchantName);
            dest.writeInt(showRule);
            dest.writeInt(showGuide);
            dest.writeParcelable(rule, flags);
            dest.writeTypedList(xrule);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ActivityBean> CREATOR = new Creator<ActivityBean>() {
            @Override
            public ActivityBean createFromParcel(Parcel in) {
                return new ActivityBean(in);
            }

            @Override
            public ActivityBean[] newArray(int size) {
                return new ActivityBean[size];
            }
        };

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public int getShowRule() {
            return showRule;
        }

        public int getShowGuide() {
            return showGuide;
        }

        public RuleBean getRule() {
            return rule;
        }

        public List<XruleBean> getXrule() {
            return xrule;
        }

        @Override
        public String toString() {
            return "ActivityBean{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", merchantName='" + merchantName + '\'' +
                    ", showRule=" + showRule +
                    ", showGuide=" + showGuide +
                    ", rule=" + rule +
                    ", xrule=" + xrule +
                    '}';
        }

        public static class RuleBean implements Parcelable{
            /**
             * bulletin : null
             */

            private String bulletin;//规则公告
            /**
             * 是否消耗答题机会
             * 0表示不消耗，1表示消耗答题次数
             */
            private int opportunity;
            /**
             * 每日限答次数
             * 0表示不限制答题次数。其它值表示限制每日答题次数
             */
            private int limitation;

            public RuleBean(){

            }

            protected RuleBean(Parcel in) {
                bulletin = in.readString();
                opportunity = in.readInt();
                limitation = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(bulletin);
                dest.writeInt(opportunity);
                dest.writeInt(limitation);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<RuleBean> CREATOR = new Creator<RuleBean>() {
                @Override
                public RuleBean createFromParcel(Parcel in) {
                    return new RuleBean(in);
                }

                @Override
                public RuleBean[] newArray(int size) {
                    return new RuleBean[size];
                }
            };

            public String getBulletin() {
                return bulletin;
            }

            public int getOpportunity() {
                return opportunity;
            }

            public int getLimitation() {
                return limitation;
            }

            @Override
            public String toString() {
                return "RuleBean{" +
                        "bulletin='" + bulletin + '\'' +
                        ", opportunity=" + opportunity +
                        ", limitation=" + limitation +
                        '}';
            }


        }

        public static class XruleBean implements Parcelable{
            /**
             * id : 1
             * name : 连续3天答题正确抽奖
             * description : null
             * remark : null
             */

            private int id;//扩展规则id
            private String name;//扩展规则名称
            private String description;//扩展规则描述
            private String remark;//扩展规则备注
            private String type;//扩展规则类型

            public XruleBean(){

            }

            protected XruleBean(Parcel in) {
                id = in.readInt();
                name = in.readString();
                description = in.readString();
                remark = in.readString();
                type = in.readString();
            }

            public static final Creator<XruleBean> CREATOR = new Creator<XruleBean>() {
                @Override
                public XruleBean createFromParcel(Parcel in) {
                    return new XruleBean(in);
                }

                @Override
                public XruleBean[] newArray(int size) {
                    return new XruleBean[size];
                }
            };

            public int getId() {
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

            public String getType() {
                return type;
            }

            @Override
            public String toString() {
                return "XruleBean{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", remark='" + remark + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(name);
                dest.writeString(description);
                dest.writeString(remark);
                dest.writeString(type);
            }
        }

    }

    public static class Guide implements Parcelable{
        private String url;//指南链接网页
        private String content;//指南内容

        public Guide(){

        }

        protected Guide(Parcel in) {
            url = in.readString();
            content = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeString(content);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Guide> CREATOR = new Creator<Guide>() {
            @Override
            public Guide createFromParcel(Parcel in) {
                return new Guide(in);
            }

            @Override
            public Guide[] newArray(int size) {
                return new Guide[size];
            }
        };

        public String getUrl() {
            return url;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Guide{" +
                    "url='" + url + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
