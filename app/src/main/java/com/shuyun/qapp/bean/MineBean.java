package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunxiao on 2018/5/5.
 */

public class MineBean implements Parcelable{

    /**
     * account : 13968696499
     * obt : 0
     * status : 10
     * certification : 1
     * phone : 13968696499
     * gender : 2
     * cash : 31.38
     * bp : 154
     * opporitunity : 5
     * withdraw : 0
     * availablePrize : 1
     * messages : 0
     */

    private String account;//登录账号


    private String nickname;//昵称
    /**
     * 是否参与公测
     */
    private int obt;
    private String header;//头像图片
    private int headerId;//头像id
    private long id;


    protected MineBean(Parcel in) {
        account = in.readString();
        nickname = in.readString();
        obt = in.readInt();
        header = in.readString();
        headerId = in.readInt();
        id = in.readLong();
        status = in.readInt();
        certification = in.readInt();
        certInfo = in.readString();
        opporitunity = in.readInt();
        phone = in.readString();
        availablePrize = in.readInt();
        upcommings = in.readLong();
        gender = in.readInt();
        withdraw = in.readInt();
        remark = in.readString();
        cash = in.readString();
        bp = in.readString();
        messages = in.readInt();
        wxBind = in.readInt();
        wxHeader = in.readString();
    }

    public static final Creator<MineBean> CREATOR = new Creator<MineBean>() {
        @Override
        public MineBean createFromParcel(Parcel in) {
            return new MineBean(in);
        }

        @Override
        public MineBean[] newArray(int size) {
            return new MineBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * 10——正常
     * 50——冻结
     * 99——禁用
     */
    private int status;
    /**
     * 是否实名认证
     * 0——未实名认证
     * 1——已实名认证
     * 2——审核中
     * 3——未通过
     * 4——拉黑
     */
    private int certification;
    private String certInfo;//实名信息  新加

    private int opporitunity;//剩余答题机会次数
    private String phone;//绑定的手机号码
    private int availablePrize;//未使用的奖品数量

    private long upcommings;//快过期的奖品数量
    /**
     * 0——未知
     * 1——男
     * 2——女
     */
    private int gender;
    /**
     * 提现标志
     * 0——不能提现
     * 1——可以提现
     * 2——提现中
     */
    private int withdraw;//提现标志
    private String remark;//自定义备注
    private String cash;//现金金额
    private String bp;//积分数量
    private int messages;//最新消息的数量,默认为0
    /**
     * 是否已经绑定微信
     * 0——未绑定
     * 1——已绑定
     */
    private int wxBind;

    private String wxHeader;//微信头像地址

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getObt() {
        return obt;
    }

    public void setObt(int obt) {
        this.obt = obt;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo;
    }

    public int getOpporitunity() {
        return opporitunity;
    }

    public void setOpporitunity(int opporitunity) {
        this.opporitunity = opporitunity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAvailablePrize() {
        return availablePrize;
    }

    public void setAvailablePrize(int availablePrize) {
        this.availablePrize = availablePrize;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(int withdraw) {
        this.withdraw = withdraw;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public void setUpcommings(long upcommings) {
        this.upcommings = upcommings;
    }

    public void setWxBind(int wxBind) {
        this.wxBind = wxBind;
    }

    public void setWxHeader(String wxHeader) {
        this.wxHeader = wxHeader;
    }

    public long getUpcommings() {
        return upcommings;
    }

    public int getWxBind() {
        return wxBind;
    }

    public String getWxHeader() {
        return wxHeader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(nickname);
        dest.writeInt(obt);
        dest.writeString(header);
        dest.writeInt(headerId);
        dest.writeLong(id);
        dest.writeInt(status);
        dest.writeInt(certification);
        dest.writeString(certInfo);
        dest.writeInt(opporitunity);
        dest.writeString(phone);
        dest.writeInt(availablePrize);
        dest.writeLong(upcommings);
        dest.writeInt(gender);
        dest.writeInt(withdraw);
        dest.writeString(remark);
        dest.writeString(cash);
        dest.writeString(bp);
        dest.writeInt(messages);
        dest.writeInt(wxBind);
        dest.writeString(wxHeader);
    }

    @Override
    public String toString() {
        return "MineBean{" +
                "account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", obt=" + obt +
                ", header='" + header + '\'' +
                ", headerId=" + headerId +
                ", id=" + id +
                ", status=" + status +
                ", certification=" + certification +
                ", certInfo='" + certInfo + '\'' +
                ", opporitunity=" + opporitunity +
                ", phone='" + phone + '\'' +
                ", availablePrize=" + availablePrize +
                ", upcommings=" + upcommings +
                ", gender=" + gender +
                ", withdraw=" + withdraw +
                ", remark='" + remark + '\'' +
                ", cash='" + cash + '\'' +
                ", bp='" + bp + '\'' +
                ", messages=" + messages +
                ", wxBind=" + wxBind +
                ", wxHeader='" + wxHeader + '\'' +
                '}';
    }
}
