package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunxiao on 2018/5/7.
 * 实名认证返回结果
 */

public class AuthNameBean implements Parcelable {
    /**
     * 实名认证状态
     * 1:已认证
     * 2：审核中
     * 3:未通过
     */
    private int status;
    private String opinion;//审核结果备注
    private String certInfo; //实名认证信息

    public AuthNameBean(){

    }

    protected AuthNameBean(Parcel in) {
        status = in.readInt();
        opinion = in.readString();
        certInfo = in.readString();
    }

    public static final Creator<AuthNameBean> CREATOR = new Creator<AuthNameBean>() {
        @Override
        public AuthNameBean createFromParcel(Parcel in) {
            return new AuthNameBean(in);
        }

        @Override
        public AuthNameBean[] newArray(int size) {
            return new AuthNameBean[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(opinion);
        dest.writeString(certInfo);
    }
}
