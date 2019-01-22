package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

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

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    private String account;
    private String nickname;
    private Long obt;
    private String header;
    private int headerId;
    private Long status;
    private int certification;
    private String phone;
    private Long gender;
    private String cash;
    private String bp;
    private Long opporitunity;
    private Long withdraw;
    private Long availablePrize; //奖品数量
    private Long upcommings;
    private Long propCount;//我的道具数量
    private Long messages;
    private Long wxBind;
    private String wxHeader;
    private String contactUs; //联系客服地址
    private List<DatasBean> datas;  //提现|实名信息
    private String cashRuleUrl; //现金提现规则
    private String redRuleUrl; //红包提现规则
    private Long certCount;//实名认证次数
    private String cashTotal; //用户现金累计金额
    private Long orderEnabled;//我的订单是否启用

    protected MineBean(Parcel in) {
        id = in.readLong();
        account = in.readString();
        nickname = in.readString();
        obt = in.readLong();
        header = in.readString();
        headerId = in.readInt();
        status = in.readLong();
        certification = in.readInt();
        phone = in.readString();
        gender = in.readLong();
        cash = in.readString();
        bp = in.readString();
        opporitunity = in.readLong();
        withdraw = in.readLong();
        availablePrize = in.readLong();
        upcommings = in.readLong();
        propCount = in.readLong();
        messages = in.readLong();
        wxBind = in.readLong();
        wxHeader = in.readString();
        contactUs = in.readString();
        datas = in.createTypedArrayList(DatasBean.CREATOR);
        cashRuleUrl = in.readString();
        redRuleUrl = in.readString();
        certCount = in.readLong();
        cashTotal = in.readString();
        orderEnabled = in.readLong();
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

    public Long getPropCount() {
        return propCount;
    }

    public void setPropCount(Long propCount) {
        this.propCount = propCount;
    }

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

    public Long getObt() {
        return obt;
    }

    public void setObt(Long obt) {
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
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

    public Long getOpporitunity() {
        return opporitunity;
    }

    public void setOpporitunity(Long opporitunity) {
        this.opporitunity = opporitunity;
    }

    public Long getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Long withdraw) {
        this.withdraw = withdraw;
    }

    public Long getAvailablePrize() {
        return availablePrize;
    }

    public void setAvailablePrize(Long availablePrize) {
        this.availablePrize = availablePrize;
    }

    public Long getUpcommings() {
        return upcommings;
    }

    public void setUpcommings(Long upcommings) {
        this.upcommings = upcommings;
    }

    public Long getMessages() {
        return messages;
    }

    public void setMessages(Long messages) {
        this.messages = messages;
    }

    public Long getWxBind() {
        return wxBind;
    }

    public void setWxBind(Long wxBind) {
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

    public Long getCertCount() {
        return certCount;
    }

    public void setCertCount(Long certCount) {
        this.certCount = certCount;
    }

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
        this.cashTotal = cashTotal;
    }

    public Long getOrderEnabled() {
        return orderEnabled;
    }

    public void setOrderEnabled(Long orderEnabled) {
        this.orderEnabled = orderEnabled;
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
        dest.writeLong(obt);
        dest.writeString(header);
        dest.writeInt(headerId);
        dest.writeLong(status);
        dest.writeLong(certification);
        dest.writeString(phone);
        dest.writeLong(gender);
        dest.writeString(cash);
        dest.writeString(bp);
        dest.writeLong(opporitunity);
        dest.writeLong(withdraw);
        dest.writeLong(availablePrize);
        dest.writeLong(upcommings);
        dest.writeLong(propCount);
        dest.writeLong(messages);
        dest.writeLong(wxBind);
        dest.writeString(wxHeader);
        dest.writeString(contactUs);
        dest.writeTypedList(datas);
        dest.writeString(cashRuleUrl);
        dest.writeString(redRuleUrl);
        dest.writeLong(certCount);
        dest.writeString(cashTotal);
        dest.writeLong(orderEnabled);
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
        private Long bankType;
        private String type;
        private String title;
        private String message;
        private Long status;
        private boolean enabled;

        public DatasBean() {

        }

        protected DatasBean(Parcel in) {
            bankId = in.readString();
            stateName = in.readString();
            bankType = in.readLong();
            type = in.readString();
            title = in.readString();
            message = in.readString();
            status = in.readLong();
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

        public Long getBankType() {
            return bankType;
        }

        public void setBankType(Long bankType) {
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

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
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
            dest.writeLong(bankType);
            dest.writeString(type);
            dest.writeString(title);
            dest.writeString(message);
            dest.writeLong(status);
            dest.writeByte((byte) (enabled ? 1 : 0));
        }
    }
}
