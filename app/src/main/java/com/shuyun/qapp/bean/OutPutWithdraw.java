package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/7.
 * 提现正确输出
 */
@Data
public class OutPutWithdraw implements Parcelable {
    private String id;
    private BigDecimal amount;
    private Long type;//现金类型1:现金提现;2:红包提现
    private Long status; //状态2:提现审核中;1:提现成功;3:审核未通过;
    private BigDecimal charge;//手续费
    private BigDecimal actual;//实际可到账金额 分
    private String time;//提现申请时间
    private String remark;//描述

    public OutPutWithdraw() {

    }

    protected OutPutWithdraw(Parcel in) {
        id = in.readString();
        type = in.readLong();
        status = in.readLong();
        time = in.readString();
    }

    public static final Creator<OutPutWithdraw> CREATOR = new Creator<OutPutWithdraw>() {
        @Override
        public OutPutWithdraw createFromParcel(Parcel in) {
            return new OutPutWithdraw(in);
        }

        @Override
        public OutPutWithdraw[] newArray(int size) {
            return new OutPutWithdraw[size];
        }
    };


    @Override
    public String toString() {
        return "OutPutWithdraw{" +
                "id=" + id +
                ", amount=" + amount +
                ", type=" + type +
                ", status=" + status +
                ", charge=" + charge +
                ", actual=" + actual +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(type);
        dest.writeLong(status);
        dest.writeString(time);
    }
}
