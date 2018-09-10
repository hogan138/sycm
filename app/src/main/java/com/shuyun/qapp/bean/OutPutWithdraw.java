package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by sunxiao on 2018/5/7.
 * 提现正确输出
 */

public class OutPutWithdraw implements Parcelable {
    private String id;
    private BigDecimal amount;
    private int type;//现金类型1:现金提现;2:红包提现
    private int status; //状态2:提现审核中;1:提现成功;3:审核未通过;
    private BigDecimal charge;//手续费
    private BigDecimal actual;//实际可到账金额 分
    private String time;//提现申请时间

    protected OutPutWithdraw(Parcel in) {
        id = in.readString();
        type = in.readInt();
        status = in.readInt();
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

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public String getTime() {
        return time;
    }

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
        dest.writeInt(type);
        dest.writeInt(status);
        dest.writeString(time);
    }
}
