package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * 用户信息bean
 */
@Data
public class MineBean implements Parcelable {


    /**
     * id : 103
     * account : 15868421563
     * nickname : 洋洋
     * obt : 0
     * header : http://3
     * headerId : 3
     * status : 10
     * certification : 1
     * certInfo :
     * phone : 15868421563
     * gender : 0
     * cash : 94.46
     * bp : 15
     * opporitunity : 65
     * withdraw : 1
     * availablePrize : 34
     * propCount : 371
     * upcommings : 9
     * messages : 0
     * wxBind : 1
     * wxHeader : http://thirdwx.qlogo.cn/mmopen/vi_32/uOZPIWOqrRNSxqnoERQRdgjQD74Aib3no7AByXbtDmulibeh2gy9xG9Hjak3SHfF6jJNibS9K9lds5Wr2SJ5F2Mkw/132
     * cashTotal : 25294.61
     * orderEnabled : 0
     * contactUs : http://192.168.3.157/web/h5/contact.html
     * cashRuleUrl : http://192.168.3.157/web/h5/forward.html
     * redRuleUrl : http://192.168.3.157/web/h5/forward.html?now=1
     * codeUrl : http://192.168.3.157/web/h5_code/code.html
     * certCount : 0
     * certBase : {"stateName":"认证成功","type":"cert","message":"认证成功后不可修改您的实名信息","title":" | ","enabled":false,"status":3}
     * withdrawBase : [{"bankId":39,"stateName":"已完善","bankType":1,"type":"withdraw","title":"支付宝 | 甘* | 158******63","message":"认证成功时间:2018/11/07 14:23:36","enabled":true,"status":3},{"stateName":"未完善","bankType":2,"type":"withdraw","title":"点击完善提现信息","message":"支持微信提现，快快补充提现信息哦","enabled":true,"status":1}]
     * datas : [{"bankId":39,"stateName":"已完善","bankType":1,"type":"withdraw","title":"支付宝 | 甘* | 158******63","message":"认证成功时间:2018/11/07 14:23:36","enabled":true,"status":3},{"stateName":"认证成功","type":"cert","message":"认证成功后不可修改您的实名信息","title":" | ","enabled":false,"status":3}]
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
    private String certInfo;
    private String phone;
    private Long gender;
    private String cash;
    private String bp;
    private Long opporitunity;
    private Long withdraw;
    private Long availablePrize;
    private Long propCount;
    private Long upcommings;
    private Long messages;
    private Long wxBind;
    private String wxHeader;
    private String cashTotal;
    private Long orderEnabled;
    private String contactUs;
    private String cashRuleUrl;
    private String redRuleUrl;
    private String codeUrl;
    private String inviteUrl;
    private Long inviteCode;
    private Long certCount;
    private CertBaseBean certBase;
    private List<WithdrawBaseBean> withdrawBase;
    private List<DatasBean> datas;


    protected MineBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        account = in.readString();
        nickname = in.readString();
        if (in.readByte() == 0) {
            obt = null;
        } else {
            obt = in.readLong();
        }
        header = in.readString();
        headerId = in.readInt();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readLong();
        }
        certification = in.readInt();
        certInfo = in.readString();
        phone = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readLong();
        }
        cash = in.readString();
        bp = in.readString();
        if (in.readByte() == 0) {
            opporitunity = null;
        } else {
            opporitunity = in.readLong();
        }
        if (in.readByte() == 0) {
            withdraw = null;
        } else {
            withdraw = in.readLong();
        }
        if (in.readByte() == 0) {
            availablePrize = null;
        } else {
            availablePrize = in.readLong();
        }
        if (in.readByte() == 0) {
            propCount = null;
        } else {
            propCount = in.readLong();
        }
        if (in.readByte() == 0) {
            upcommings = null;
        } else {
            upcommings = in.readLong();
        }
        if (in.readByte() == 0) {
            messages = null;
        } else {
            messages = in.readLong();
        }
        if (in.readByte() == 0) {
            wxBind = null;
        } else {
            wxBind = in.readLong();
        }
        wxHeader = in.readString();
        cashTotal = in.readString();
        if (in.readByte() == 0) {
            orderEnabled = null;
        } else {
            orderEnabled = in.readLong();
        }
        contactUs = in.readString();
        cashRuleUrl = in.readString();
        redRuleUrl = in.readString();
        codeUrl = in.readString();
        inviteUrl = in.readString();
        if (in.readByte() == 0) {
            inviteCode = null;
        } else {
            inviteCode = in.readLong();
        }
        if (in.readByte() == 0) {
            certCount = null;
        } else {
            certCount = in.readLong();
        }
        certBase = in.readParcelable(CertBaseBean.class.getClassLoader());
        withdrawBase = in.createTypedArrayList(WithdrawBaseBean.CREATOR);
        datas = in.createTypedArrayList(DatasBean.CREATOR);
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(account);
        dest.writeString(nickname);
        if (obt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(obt);
        }
        dest.writeString(header);
        dest.writeInt(headerId);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(status);
        }
        dest.writeInt(certification);
        dest.writeString(certInfo);
        dest.writeString(phone);
        if (gender == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(gender);
        }
        dest.writeString(cash);
        dest.writeString(bp);
        if (opporitunity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(opporitunity);
        }
        if (withdraw == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(withdraw);
        }
        if (availablePrize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(availablePrize);
        }
        if (propCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(propCount);
        }
        if (upcommings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(upcommings);
        }
        if (messages == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(messages);
        }
        if (wxBind == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(wxBind);
        }
        dest.writeString(wxHeader);
        dest.writeString(cashTotal);
        if (orderEnabled == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderEnabled);
        }
        dest.writeString(contactUs);
        dest.writeString(cashRuleUrl);
        dest.writeString(redRuleUrl);
        dest.writeString(codeUrl);
        dest.writeString(inviteUrl);
        if (inviteCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(inviteCode);
        }
        if (certCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(certCount);
        }
        dest.writeParcelable(certBase, flags);
        dest.writeTypedList(withdrawBase);
        dest.writeTypedList(datas);
    }

    @Data
    public static class CertBaseBean implements Parcelable {
        /**
         * stateName : 认证成功
         * type : cert
         * message : 认证成功后不可修改您的实名信息
         * title :  |
         * enabled : false
         * status : 3
         */

        private String stateName;
        private String type;
        private String message;
        private String title;
        private boolean enabled;
        private Long status;

        protected CertBaseBean(Parcel in) {
            stateName = in.readString();
            type = in.readString();
            message = in.readString();
            title = in.readString();
            enabled = in.readByte() != 0;
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readLong();
            }
        }

        public static final Creator<CertBaseBean> CREATOR = new Creator<CertBaseBean>() {
            @Override
            public CertBaseBean createFromParcel(Parcel in) {
                return new CertBaseBean(in);
            }

            @Override
            public CertBaseBean[] newArray(int size) {
                return new CertBaseBean[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(stateName);
            dest.writeString(type);
            dest.writeString(message);
            dest.writeString(title);
            dest.writeByte((byte) (enabled ? 1 : 0));
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(status);
            }
        }
    }

    @Data
    public static class WithdrawBaseBean implements Parcelable {
        /**
         * bankId : 39
         * stateName : 已完善
         * bankType : 1
         * type : withdraw
         * title : 支付宝 | 甘* | 158******63
         * message : 认证成功时间:2018/11/07 14:23:36
         * enabled : true
         * status : 3
         */

        private String bankId;
        private String stateName;
        private Long bankType;
        private String type;
        private String title;
        private String message;
        private boolean enabled;
        private Long status;
        private boolean isSelected;


        protected WithdrawBaseBean(Parcel in) {
            bankId = in.readString();
            stateName = in.readString();
            if (in.readByte() == 0) {
                bankType = null;
            } else {
                bankType = in.readLong();
            }
            type = in.readString();
            title = in.readString();
            message = in.readString();
            enabled = in.readByte() != 0;
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readLong();
            }
            isSelected = in.readByte() != 0;
        }

        public static final Creator<WithdrawBaseBean> CREATOR = new Creator<WithdrawBaseBean>() {
            @Override
            public WithdrawBaseBean createFromParcel(Parcel in) {
                return new WithdrawBaseBean(in);
            }

            @Override
            public WithdrawBaseBean[] newArray(int size) {
                return new WithdrawBaseBean[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bankId);
            dest.writeString(stateName);
            if (bankType == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(bankType);
            }
            dest.writeString(type);
            dest.writeString(title);
            dest.writeString(message);
            dest.writeByte((byte) (enabled ? 1 : 0));
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(status);
            }
            dest.writeByte((byte) (isSelected ? 1 : 0));
        }
    }

    @Data
    public static class DatasBean implements Parcelable {
        /**
         * bankId : 39
         * stateName : 已完善
         * bankType : 1
         * type : withdraw
         * title : 支付宝 | 甘* | 158******63
         * message : 认证成功时间:2018/11/07 14:23:36
         * enabled : true
         * status : 3
         */

        private String bankId;
        private String stateName;
        private Long bankType;
        private String type;
        private String title;
        private String message;
        private boolean enabled;
        private Long status;

        protected DatasBean(Parcel in) {
            bankId = in.readString();
            stateName = in.readString();
            if (in.readByte() == 0) {
                bankType = null;
            } else {
                bankType = in.readLong();
            }
            type = in.readString();
            title = in.readString();
            message = in.readString();
            enabled = in.readByte() != 0;
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readLong();
            }
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bankId);
            dest.writeString(stateName);
            if (bankType == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(bankType);
            }
            dest.writeString(type);
            dest.writeString(title);
            dest.writeString(message);
            dest.writeByte((byte) (enabled ? 1 : 0));
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(status);
            }
        }
    }
}
