package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 用户信息bean
 */

public class MineBean implements Parcelable {


    /**
     * id : 103
     * account : 15868421563
     * nickname : Android style
     * obt : 0
     * header : http://5
     * headerId : 5
     * status : 10
     * certification : 0
     * phone : 15868421563
     * gender : 0
     * cash : 83.24
     * bp : 7174
     * opporitunity : 9967
     * withdraw : 1
     * availablePrize : 13
     * upcommings : 0
     * messages : 0
     * wxBind : 1
     * wxHeader : http://thirdwx.qlogo.cn/mmopen/vi_32/Fq4vqQJao9JdSIlLfeww4mcXPSrRoYDDUrgEOJLOgg2xAXYylqhJlXcwt4c3Lb1odTV5r0ibQrpPhjlW7IVyWyw/132
     * contactUs : http://192.168.3.137:8080/web/h5/contact.html
     * datas : [{"stateName":"未完善","bankType":1,"type":"withdraw","title":"点击完善提现信息","message":"支持支付宝提现，快快补充提现信息哦","status":1}]
     */

    private Long id;
    private String account;
    private String nickname;
    private int obt;
    private String header;
    private int headerId;
    private int status;
    private int certification;
    private String phone;
    private int gender;
    private String cash;
    private String bp;
    private int opporitunity;
    private int withdraw;
    private int availablePrize;
    private int upcommings;
    private int messages;
    private int wxBind;
    private String wxHeader;
    private String contactUs; //联系客服地址
    private List<DatasBean> datas;  //提现|实名信息
    private String cashRuleUrl; //现金提现规则
    private String redRuleUrl; //红包提现规则
    private int certCount;//实名认证次数

    protected MineBean(Parcel in) {
        id = in.readLong();
        account = in.readString();
        nickname = in.readString();
        obt = in.readInt();
        header = in.readString();
        headerId = in.readInt();
        status = in.readInt();
        certification = in.readInt();
        phone = in.readString();
        gender = in.readInt();
        cash = in.readString();
        bp = in.readString();
        opporitunity = in.readInt();
        withdraw = in.readInt();
        availablePrize = in.readInt();
        upcommings = in.readInt();
        messages = in.readInt();
        wxBind = in.readInt();
        wxHeader = in.readString();
        contactUs = in.readString();
        datas = in.createTypedArrayList(DatasBean.CREATOR);
        cashRuleUrl = in.readString();
        redRuleUrl = in.readString();
        certCount = in.readInt();
    }

    public MineBean() {

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public int getOpporitunity() {
        return opporitunity;
    }

    public void setOpporitunity(int opporitunity) {
        this.opporitunity = opporitunity;
    }

    public int getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(int withdraw) {
        this.withdraw = withdraw;
    }

    public int getAvailablePrize() {
        return availablePrize;
    }

    public void setAvailablePrize(int availablePrize) {
        this.availablePrize = availablePrize;
    }

    public int getUpcommings() {
        return upcommings;
    }

    public void setUpcommings(int upcommings) {
        this.upcommings = upcommings;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public int getWxBind() {
        return wxBind;
    }

    public void setWxBind(int wxBind) {
        this.wxBind = wxBind;
    }

    public String getWxHeader() {
        return wxHeader;
    }

    public void setWxHeader(String wxHeader) {
        this.wxHeader = wxHeader;
    }

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public String getCashRuleUrl() {
        return cashRuleUrl;
    }

    public void setCashRuleUrl(String cashRuleUrl) {
        this.cashRuleUrl = cashRuleUrl;
    }

    public String getRedRuleUrl() {
        return redRuleUrl;
    }

    public void setRedRuleUrl(String redRuleUrl) {
        this.redRuleUrl = redRuleUrl;
    }

    public int getCertCount() {
        return certCount;
    }

    public void setCertCount(int certCount) {
        this.certCount = certCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(account);
        dest.writeString(nickname);
        dest.writeInt(obt);
        dest.writeString(header);
        dest.writeInt(headerId);
        dest.writeInt(status);
        dest.writeInt(certification);
        dest.writeString(phone);
        dest.writeInt(gender);
        dest.writeString(cash);
        dest.writeString(bp);
        dest.writeInt(opporitunity);
        dest.writeInt(withdraw);
        dest.writeInt(availablePrize);
        dest.writeInt(upcommings);
        dest.writeInt(messages);
        dest.writeInt(wxBind);
        dest.writeString(wxHeader);
        dest.writeString(contactUs);
        dest.writeTypedList(datas);
        dest.writeString(cashRuleUrl);
        dest.writeString(redRuleUrl);
        dest.writeInt(certCount);
    }

    public static class DatasBean implements Parcelable {

        /**
         * stateName : 未完善
         * bankType : 1
         * type : withdraw
         * title : 点击完善提现信息
         * message : 支持支付宝提现，快快补充提现信息哦
         * status : 1
         */

        private String bankId;
        private String stateName;
        private int bankType;
        private String type;
        private String title;
        private String message;
        private int status;
        private boolean enabled;

        public DatasBean() {

        }

        protected DatasBean(Parcel in) {
            bankId = in.readString();
            stateName = in.readString();
            bankType = in.readInt();
            type = in.readString();
            title = in.readString();
            message = in.readString();
            status = in.readInt();
            enabled = in.readByte() != 0;
        }

        public static final Creator<DatasBean> CREATOR = new Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel in) {
                return new DatasBean(in);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
            }
        };

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public int getBankType() {
            return bankType;
        }

        public void setBankType(int bankType) {
            this.bankType = bankType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bankId);
            dest.writeString(stateName);
            dest.writeInt(bankType);
            dest.writeString(type);
            dest.writeString(title);
            dest.writeString(message);
            dest.writeInt(status);
            dest.writeByte((byte) (enabled ? 1 : 0));
        }
    }
}
