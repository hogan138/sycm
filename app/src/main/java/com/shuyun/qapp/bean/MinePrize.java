package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/5.
 * 我的奖品
 */

@Data
public class MinePrize implements Parcelable {


    /**
     * name : 宝箱
     * type : 101
     * description : 您答题成功之后获得的奖品宝箱，请在规定时间内打开获取奖励哦
     * mode : 0
     * picture : http://img-syksc.25876.com/syksc/app/prize/ico/bx.png
     * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/baoxiang.png
     * LongImage : 1
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
     * prizes : [{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.1,"worthUpper":0.5},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.5,"worthUpper":0.9},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.9,"worthUpper":1.1},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":1.1,"worthUpper":3},{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":3.1,"worthUpper":5},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":2,"worthUpper":2},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":3,"worthUpper":3},{"name":"增次卡","type":5,"mode":6,"purpose":"增加一次答题次数，仅限当日使用","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","id":1003,"worthLower":1,"worthUpper":1},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","LongImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":20,"worthUpper":20}]
     */

    private String name;
    private Long type;
    private String description;
    private Long mode;
    private String picture;
    private String mainImage;
    private String LongImage;
    private String id;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String expireTime;
    private String validTime;
    private Long status;
    private Long upcomming;
    private String amount;
    private String bulletin;
    private List<PrizesBean> prizes;
    private Long beat0 = 0L;//正确击败人次
    private Double beatRate;//击败人次的比例
    private Double accuracy;//正确率
    private Long source;//奖品来源
    private String substatusName;
    private String h5Url;
    private int orginal;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long scheduleId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long orderId;
    @JSONField(serializeUsing = ToStringSerializer.class)
    public boolean selected = false;
    private String content;
    private String openPicture;//开奖使用的图片
    private String actionType; //动作值
    private String actionTypeLabel;//按钮名称
    private List<ChildMinePrize> groups; //红包集合
    private Long lock;//显示锁遮罩层
    private LockConfigBean lockConfig;//锁弹框信息

    public MinePrize() {

    }

    protected MinePrize(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readLong();
        }
        description = in.readString();
        if (in.readByte() == 0) {
            mode = null;
        } else {
            mode = in.readLong();
        }
        picture = in.readString();
        mainImage = in.readString();
        LongImage = in.readString();
        id = in.readString();
        expireTime = in.readString();
        validTime = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readLong();
        }
        if (in.readByte() == 0) {
            upcomming = null;
        } else {
            upcomming = in.readLong();
        }
        amount = in.readString();
        bulletin = in.readString();
        prizes = in.createTypedArrayList(PrizesBean.CREATOR);
        if (in.readByte() == 0) {
            beat0 = null;
        } else {
            beat0 = in.readLong();
        }
        if (in.readByte() == 0) {
            beatRate = null;
        } else {
            beatRate = in.readDouble();
        }
        if (in.readByte() == 0) {
            accuracy = null;
        } else {
            accuracy = in.readDouble();
        }
        if (in.readByte() == 0) {
            source = null;
        } else {
            source = in.readLong();
        }
        substatusName = in.readString();
        h5Url = in.readString();
        orginal = in.readInt();
        if (in.readByte() == 0) {
            scheduleId = null;
        } else {
            scheduleId = in.readLong();
        }
        if (in.readByte() == 0) {
            orderId = null;
        } else {
            orderId = in.readLong();
        }
        selected = in.readByte() != 0;
        content = in.readString();
        openPicture = in.readString();
        actionType = in.readString();
        actionTypeLabel = in.readString();
        groups = in.createTypedArrayList(ChildMinePrize.CREATOR);
        if (in.readByte() == 0) {
            lock = null;
        } else {
            lock = in.readLong();
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(type);
        }
        dest.writeString(description);
        if (mode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mode);
        }
        dest.writeString(picture);
        dest.writeString(mainImage);
        dest.writeString(LongImage);
        dest.writeString(id);
        dest.writeString(expireTime);
        dest.writeString(validTime);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(status);
        }
        if (upcomming == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(upcomming);
        }
        dest.writeString(amount);
        dest.writeString(bulletin);
        dest.writeTypedList(prizes);
        if (beat0 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(beat0);
        }
        if (beatRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(beatRate);
        }
        if (accuracy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(accuracy);
        }
        if (source == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(source);
        }
        dest.writeString(substatusName);
        dest.writeString(h5Url);
        dest.writeInt(orginal);
        if (scheduleId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(scheduleId);
        }
        if (orderId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderId);
        }
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(content);
        dest.writeString(openPicture);
        dest.writeString(actionType);
        dest.writeString(actionTypeLabel);
        dest.writeTypedList(groups);
        if (lock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lock);
        }
    }

    @Data
    public static class LockConfigBean {
        /**
         * msg : 您在平安好医生APP完成的订单确认收货15天后可以提现
         * button : 查看订单
         * h5Url : pajkdoctor://jk.cn/jump?query=PEDOMETER
         * action : action.url.scheme
         */

        private String msg;
        private String button;
        private String h5Url;
        private String action;
        private String content;


    }


    public List<ChildMinePrize> getMinePrizes() {
        return groups;
    }

    public void setMinePrizes(List<ChildMinePrize> ChildMinePrizes) {
        this.groups = ChildMinePrizes;
    }


    public static class PrizesBean implements Parcelable {
        /**
         * showName : 现金
         * type : 1
         * mode : 1
         * purpose : 满50可提现至支付宝
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * LongImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * id : 1001
         * worthLower : 0.1
         * worthUpper : 0.5
         */

        private String showName;
        private Long type;
        private Long mode;
        private String purpose;
        private String mainImage;
        private String LongImage;
        private String shortImage;
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;
        private Double worthLower;
        private Double worthUpper;

        public PrizesBean() {

        }

        protected PrizesBean(Parcel in) {
            showName = in.readString();
            type = in.readLong();
            mode = in.readLong();
            purpose = in.readString();
            mainImage = in.readString();
            LongImage = in.readString();
            shortImage = in.readString();
            id = in.readLong();
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(showName);
            dest.writeLong(type);
            dest.writeLong(mode);
            dest.writeString(purpose);
            dest.writeString(mainImage);
            dest.writeString(LongImage);
            dest.writeString(shortImage);
            dest.writeLong(id);
            dest.writeDouble(worthLower);
            dest.writeDouble(worthUpper);
        }
    }

    @Data
    public static class ChildMinePrize implements Parcelable {

        /**
         * name : 红包
         * type : 7
         * description : 123
         * mode : 3
         * picture : http://img-syksc.25876.com/syksc/app/prize/ico/xjhb.png
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png
         * LongImage : http://img-syksc.25876.com/syksc/app/prize/img/hongbao1.png
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
        private Long type;
        private String description;
        private Long mode;
        private String picture;
        private String mainImage;
        private String LongImage;
        private String shortImage;
        private String id;
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long prizeId;
        @JSONField(serializeUsing = ToStringSerializer.class)
        private String expireTime;
        private Long validTime;
        private Long status;
        private Long substatus;
        private Long upcomming;
        private String amount;
        private Long beat;
        private Double beatRate;
        private Double accuracy;
        private Long original;
        private Long source;
        private Long scheduleId;
        private Long orderId;
        public boolean selected = false;
        private String actionType; //动作值
        private String actionTypeLabel;//按钮名称


        public static Creator<ChildMinePrize> getCREATOR() {
            return CREATOR;
        }

        public ChildMinePrize() {

        }

        protected ChildMinePrize(Parcel in) {
            name = in.readString();
            type = in.readLong();
            description = in.readString();
            mode = in.readLong();
            picture = in.readString();
            mainImage = in.readString();
            LongImage = in.readString();
            shortImage = in.readString();
            id = in.readString();
            prizeId = in.readLong();
            expireTime = in.readString();
            validTime = in.readLong();
            status = in.readLong();
            substatus = in.readLong();
            upcomming = in.readLong();
            amount = in.readString();
            beat = in.readLong();
            beatRate = in.readDouble();
            accuracy = in.readDouble();
            original = in.readLong();
            source = in.readLong();
            scheduleId = in.readLong();
            orderId = in.readLong();
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
            dest.writeLong(type);
            dest.writeString(description);
            dest.writeLong(mode);
            dest.writeString(picture);
            dest.writeString(mainImage);
            dest.writeString(LongImage);
            dest.writeString(shortImage);
            dest.writeString(id);
            dest.writeLong(prizeId);
            dest.writeString(expireTime);
            dest.writeLong(validTime);
            dest.writeLong(status);
            dest.writeLong(substatus);
            dest.writeLong(upcomming);
            dest.writeString(amount);
            dest.writeLong(beat);
            dest.writeDouble(beatRate);
            dest.writeDouble(accuracy);
            dest.writeLong(original);
            dest.writeLong(source);
            dest.writeLong(scheduleId);
            dest.writeLong(orderId);
            dest.writeByte((byte) (selected ? 1 : 0));
            dest.writeString(actionType);
            dest.writeString(actionTypeLabel);
        }
    }

}
