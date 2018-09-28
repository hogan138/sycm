package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/5.
 * 我的奖品
 */

public class MinePrize implements Parcelable {


    /**
     * name : 宝箱
     * type : 101
     * description : 您答题成功之后获得的奖品宝箱，请在规定时间内打开获取奖励哦
     * mode : 0
     * picture : http://img-syksc.25876.com/syksc/app/prize/ico/bx.png
     * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/baoxiang.png
     * longImage : 1
     * id : b41bf51dd64d485cac5d86c23d908a68
     * ruleId : 1
     * expireTime : 1528508929096
     * validTime : 0
     * status : 1
     * upcomming : 1
     * amount : 1
     * bulletin : 1、每轮答题共8道，每题答题时间为20秒，在规定时间内答对5题以上获得宝箱。</br>
     * 2、打开宝箱就有机会获得随机赠送的现金及实物奖励、全民积分、虚拟道具等。</br>
     * 3、中奖金额超过800元（含）需缴纳中奖全额的20%个人所得税，从奖金中扣除。</br>
     * 4、本平台的抽奖活动，发起方为“全民共进”平台，与其他第三方无关。</br>
     * 4、最终解释权归浙江舒云互联网传媒有限公司所有，未尽说明事项保留法律权利。
     * prizes : [{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.1,"worthUpper":0.5},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.5,"worthUpper":0.9},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.9,"worthUpper":1.1},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":1.1,"worthUpper":3},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":3.1,"worthUpper":5},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":2,"worthUpper":2},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":3,"worthUpper":3},{"name":"增次卡","type":5,"mode":6,"purpose":"增加一次答题次数，仅限当日使用","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","id":1003,"worthLower":1,"worthUpper":1},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":20,"worthUpper":20}]
     */

    private String name;
    private int type;
    private String description;
    private int mode;
    private String picture;
    private String mainImage;
    private String longImage;
    private String id;
    private int ruleId;
    private String expireTime;
    private String validTime;
    private int status;
    private int upcomming;
    private String amount;
    private String bulletin;
    private List<PrizesBean> prizes;
    private long beat0;//正确击败人次
    private double beatRate;//击败人次的比例
    private double accuracy;//正确率
    private int source;//奖品来源
    private String substatusName;
    private String h5Url;
    private int orginal;
    private long scheduleId;
    private long orderId;
    public boolean selected = false;
    private String content;
    private String openPicture;//开奖使用的图片
    private String actionType; //动作值
    private String actionTypeLabel;//按钮名称
    private List<ChildMinePrize> groups; //红包集合

    public MinePrize(){

    }
    protected MinePrize(Parcel in) {
        name = in.readString();
        type = in.readInt();
        description = in.readString();
        mode = in.readInt();
        picture = in.readString();
        mainImage = in.readString();
        longImage = in.readString();
        id = in.readString();
        ruleId = in.readInt();
        expireTime = in.readString();
        validTime = in.readString();
        status = in.readInt();
        upcomming = in.readInt();
        amount = in.readString();
        bulletin = in.readString();
        prizes = in.createTypedArrayList(PrizesBean.CREATOR);
        beat0 = in.readLong();
        beatRate = in.readDouble();
        accuracy = in.readDouble();
        source = in.readInt();
        substatusName = in.readString();
        h5Url = in.readString();
        orginal = in.readInt();
        scheduleId = in.readLong();
        orderId = in.readLong();
        selected = in.readByte() != 0;
        content = in.readString();
        openPicture = in.readString();
        actionType = in.readString();
        actionTypeLabel = in.readString();
        groups = in.createTypedArrayList(ChildMinePrize.CREATOR);
    }

    public static final Creator<MinePrize> CREATOR = new Creator<MinePrize>() {
        @Override
        public MinePrize createFromParcel(Parcel in) {
            return new MinePrize(in);
        }

        @Override
        public MinePrize[] newArray(int size) {
            return new MinePrize[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getLongImage() {
        return longImage;
    }

    public void setLongImage(String longImage) {
        this.longImage = longImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUpcomming() {
        return upcomming;
    }

    public void setUpcomming(int upcomming) {
        this.upcomming = upcomming;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public List<PrizesBean> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrizesBean> prizes) {
        this.prizes = prizes;
    }

    public long getBeat0() {
        return beat0;
    }

    public void setBeat0(long beat0) {
        this.beat0 = beat0;
    }

    public double getBeatRate() {
        return beatRate;
    }

    public void setBeatRate(double beatRate) {
        this.beatRate = beatRate;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSubstatusName() {
        return substatusName;
    }

    public void setSubstatusName(String substatusName) {
        this.substatusName = substatusName;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getOrginal() {
        return orginal;
    }

    public void setOrginal(int orginal) {
        this.orginal = orginal;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpenPicture() {
        return openPicture;
    }

    public void setOpenPicture(String openPicture) {
        this.openPicture = openPicture;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionTypeLabel() {
        return actionTypeLabel;
    }

    public void setActionTypeLabel(String actionTypeLabel) {
        this.actionTypeLabel = actionTypeLabel;
    }

    public List<ChildMinePrize> getMinePrizes() {
        return groups;
    }

    public void setMinePrizes(List<ChildMinePrize> ChildMinePrizes) {
        this.groups = ChildMinePrizes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeInt(mode);
        dest.writeString(picture);
        dest.writeString(mainImage);
        dest.writeString(longImage);
        dest.writeString(id);
        dest.writeInt(ruleId);
        dest.writeString(expireTime);
        dest.writeString(validTime);
        dest.writeInt(status);
        dest.writeInt(upcomming);
        dest.writeString(amount);
        dest.writeString(bulletin);
        dest.writeTypedList(prizes);
        dest.writeLong(beat0);
        dest.writeDouble(beatRate);
        dest.writeDouble(accuracy);
        dest.writeInt(source);
        dest.writeString(substatusName);
        dest.writeString(h5Url);
        dest.writeInt(orginal);
        dest.writeLong(scheduleId);
        dest.writeLong(orderId);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(content);
        dest.writeString(openPicture);
        dest.writeString(actionType);
        dest.writeString(actionTypeLabel);
        dest.writeTypedList(groups);
    }


    public static class PrizesBean implements Parcelable {
        /**
         * showName : 现金
         * type : 1
         * mode : 1
         * purpose : 满50可提现至支付宝
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * longImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * id : 1001
         * worthLower : 0.1
         * worthUpper : 0.5
         */

        private String showName;
        private int type;
        private int mode;
        private String purpose;
        private String mainImage;
        private String longImage;
        private String shortImage;
        private int id;
        private double worthLower;
        private double worthUpper;

        public PrizesBean() {

        }

        protected PrizesBean(Parcel in) {
            showName = in.readString();
            type = in.readInt();
            mode = in.readInt();
            purpose = in.readString();
            mainImage = in.readString();
            longImage = in.readString();
            shortImage = in.readString();
            id = in.readInt();
            worthLower = in.readDouble();
            worthUpper = in.readDouble();
        }

        public static final Creator<PrizesBean> CREATOR = new Creator<PrizesBean>() {
            @Override
            public PrizesBean createFromParcel(Parcel in) {
                return new PrizesBean(in);
            }

            @Override
            public PrizesBean[] newArray(int size) {
                return new PrizesBean[size];
            }
        };

        public String getShowName() {
            return showName;
        }

        public void setShowName(String name) {
            this.showName = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getLongImage() {
            return longImage;
        }

        public void setLongImage(String longImage) {
            this.longImage = longImage;
        }

        public String getShortImage() {
            return shortImage;
        }

        public void setShortImage(String shortImage) {
            this.shortImage = shortImage;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getWorthLower() {
            return worthLower;
        }

        public void setWorthLower(double worthLower) {
            this.worthLower = worthLower;
        }

        public double getWorthUpper() {
            return worthUpper;
        }

        public void setWorthUpper(double worthUpper) {
            this.worthUpper = worthUpper;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(showName);
            dest.writeInt(type);
            dest.writeInt(mode);
            dest.writeString(purpose);
            dest.writeString(mainImage);
            dest.writeString(longImage);
            dest.writeString(shortImage);
            dest.writeInt(id);
            dest.writeDouble(worthLower);
            dest.writeDouble(worthUpper);
        }
    }

    public static class ChildMinePrize implements Parcelable {

        /**
         * name : 红包
         * type : 7
         * description : 123
         * mode : 3
         * picture : http://img-syksc.25876.com/syksc/app/prize/ico/xjhb.png
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png
         * longImage : http://img-syksc.25876.com/syksc/app/prize/img/hongbao1.png
         * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png
         * id : 102065a277bf4830851aab39ff307115
         * prizeId : 1004
         * ruleId : 2
         * expireTime : 0
         * validTime : 0
         * status : 1
         * substatus : 0
         * upcomming : 0
         * amount : 5
         * beat : 0
         * beatRate : 0
         * accuracy : 0
         * original : 3
         * source : 0
         * scheduleId : 0
         * orderId : 0
         */

        private String name;
        private int type;
        private String description;
        private int mode;
        private String picture;
        private String mainImage;
        private String longImage;
        private String shortImage;
        private String id;
        private int prizeId;
        private int ruleId;
        private String expireTime;
        private int validTime;
        private int status;
        private int substatus;
        private int upcomming;
        private String amount;
        private int beat;
        private int beatRate;
        private int accuracy;
        private int original;
        private int source;
        private int scheduleId;
        private int orderId;
        public boolean selected = false;
        private String actionType; //动作值
        private String actionTypeLabel;//按钮名称

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getActionTypeLabel() {
            return actionTypeLabel;
        }

        public void setActionTypeLabel(String actionTypeLabel) {
            this.actionTypeLabel = actionTypeLabel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getLongImage() {
            return longImage;
        }

        public void setLongImage(String longImage) {
            this.longImage = longImage;
        }

        public String getShortImage() {
            return shortImage;
        }

        public void setShortImage(String shortImage) {
            this.shortImage = shortImage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPrizeId() {
            return prizeId;
        }

        public void setPrizeId(int prizeId) {
            this.prizeId = prizeId;
        }

        public int getRuleId() {
            return ruleId;
        }

        public void setRuleId(int ruleId) {
            this.ruleId = ruleId;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getValidTime() {
            return validTime;
        }

        public void setValidTime(int validTime) {
            this.validTime = validTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubstatus() {
            return substatus;
        }

        public void setSubstatus(int substatus) {
            this.substatus = substatus;
        }

        public int getUpcomming() {
            return upcomming;
        }

        public void setUpcomming(int upcomming) {
            this.upcomming = upcomming;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getBeat() {
            return beat;
        }

        public void setBeat(int beat) {
            this.beat = beat;
        }

        public int getBeatRate() {
            return beatRate;
        }

        public void setBeatRate(int beatRate) {
            this.beatRate = beatRate;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(int accuracy) {
            this.accuracy = accuracy;
        }

        public int getOriginal() {
            return original;
        }

        public void setOriginal(int original) {
            this.original = original;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(int scheduleId) {
            this.scheduleId = scheduleId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public static Creator<ChildMinePrize> getCREATOR() {
            return CREATOR;
        }

        public ChildMinePrize(){

        }
        protected ChildMinePrize(Parcel in) {
            name = in.readString();
            type = in.readInt();
            description = in.readString();
            mode = in.readInt();
            picture = in.readString();
            mainImage = in.readString();
            longImage = in.readString();
            shortImage = in.readString();
            id = in.readString();
            prizeId = in.readInt();
            ruleId = in.readInt();
            expireTime = in.readString();
            validTime = in.readInt();
            status = in.readInt();
            substatus = in.readInt();
            upcomming = in.readInt();
            amount = in.readString();
            beat = in.readInt();
            beatRate = in.readInt();
            accuracy = in.readInt();
            original = in.readInt();
            source = in.readInt();
            scheduleId = in.readInt();
            orderId = in.readInt();
            selected = in.readByte() != 0;
            actionType = in.readString();
            actionTypeLabel = in.readString();
        }

        public static final Creator<ChildMinePrize> CREATOR = new Creator<ChildMinePrize>() {
            @Override
            public ChildMinePrize createFromParcel(Parcel in) {
                return new ChildMinePrize(in);
            }

            @Override
            public ChildMinePrize[] newArray(int size) {
                return new ChildMinePrize[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeInt(type);
            dest.writeString(description);
            dest.writeInt(mode);
            dest.writeString(picture);
            dest.writeString(mainImage);
            dest.writeString(longImage);
            dest.writeString(shortImage);
            dest.writeString(id);
            dest.writeInt(prizeId);
            dest.writeInt(ruleId);
            dest.writeString(expireTime);
            dest.writeInt(validTime);
            dest.writeInt(status);
            dest.writeInt(substatus);
            dest.writeInt(upcomming);
            dest.writeString(amount);
            dest.writeInt(beat);
            dest.writeInt(beatRate);
            dest.writeInt(accuracy);
            dest.writeInt(original);
            dest.writeInt(source);
            dest.writeInt(scheduleId);
            dest.writeInt(orderId);
            dest.writeByte((byte) (selected ? 1 : 0));
            dest.writeString(actionType);
            dest.writeString(actionTypeLabel);
        }
    }

}
