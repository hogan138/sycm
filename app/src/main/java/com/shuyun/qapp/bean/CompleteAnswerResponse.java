package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/4.
 * 完成答题 返回数据
 */
@Data
public class CompleteAnswerResponse implements Parcelable {
    private Long total;//答题实际回答的数量
    private Long status;//答题的最终状态
    private String id;//答题id
    private Long correct;//回答正确的数量
    private Long error;//回答错误的数量
    private Long timeout;//超时回答的数量
    private Double accuracy;//答题正确率
    private Float difficulty;//题目平均难度系数
    private Long result;//答题结果0:未中奖;1:中奖
    private Float worth;//奖品的价值
    private String bulletin;//开奖公告
    private List<MinePrize> prize;//中奖的奖品
    private Long beat;//击败多少人
    private Long beat0;//真实击败人次
    private Double beatRate;//击败的人次比例值
    private List<PrizesBean> prizes;
    public  CompleteAnswerResponse(){

    }
    protected CompleteAnswerResponse(Parcel in) {
        total = in.readLong();
        status = in.readLong();
        id = in.readString();
        correct = in.readLong();
        error = in.readLong();
        timeout = in.readLong();
        accuracy = in.readDouble();
        difficulty = in.readFloat();
        result = in.readLong();
        worth = in.readFloat();
        bulletin = in.readString();
        prize = in.createTypedArrayList(MinePrize.CREATOR);
        beat = in.readLong();
        beat0 = in.readLong();
        beatRate = in.readDouble();
        prizes = in.createTypedArrayList(PrizesBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(total);
        dest.writeLong(status);
        dest.writeString(id);
        dest.writeLong(correct);
        dest.writeLong(error);
        dest.writeLong(timeout);
        dest.writeDouble(accuracy);
        dest.writeFloat(difficulty);
        dest.writeLong(result);
        dest.writeFloat(worth);
        dest.writeString(bulletin);
        dest.writeTypedList(prize);
        dest.writeLong(beat);
        dest.writeLong(beat0);
        dest.writeDouble(beatRate);
        dest.writeTypedList(prizes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompleteAnswerResponse> CREATOR = new Creator<CompleteAnswerResponse>() {
        @Override
        public CompleteAnswerResponse createFromParcel(Parcel in) {
            return new CompleteAnswerResponse(in);
        }

        @Override
        public CompleteAnswerResponse[] newArray(int size) {
            return new CompleteAnswerResponse[size];
        }
    };


    @Data
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

        public PrizesBean(){

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

}
