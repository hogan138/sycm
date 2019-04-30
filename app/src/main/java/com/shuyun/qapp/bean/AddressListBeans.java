package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AddressListBeans
 * @Description: 收货地址列表
 * @Author: ganquan
 * @CreateDate: 2019/4/29 16:13
 */
@Data
public class AddressListBeans implements Parcelable {
    /**
     * id : 6528821910883803136
     * userName : 哈哈哈
     * userPhone : 15868421563
     * isDefault : 1
     * city : 140100
     * detail :  突突突
     * province : 14
     * county : 140105
     * cityName : 太原
     * provinceName : 山西省
     * countyName : 小店区
     */
    private String id;
    private String userName;
    private String userPhone;
    private Long isDefault;
    private String city;
    private String detail;
    private String province;
    private String county;
    private String cityName;
    private String provinceName;
    private String countyName;

    protected AddressListBeans(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        if (in.readByte() == 0) {
            isDefault = null;
        } else {
            isDefault = in.readLong();
        }
        city = in.readString();
        detail = in.readString();
        province = in.readString();
        county = in.readString();
        cityName = in.readString();
        provinceName = in.readString();
        countyName = in.readString();
    }

    public static final Creator<AddressListBeans> CREATOR = new Creator<AddressListBeans>() {
        @Override
        public AddressListBeans createFromParcel(Parcel in) {
            return new AddressListBeans(in);
        }

        @Override
        public AddressListBeans[] newArray(int size) {
            return new AddressListBeans[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(userPhone);
        if (isDefault == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(isDefault);
        }
        dest.writeString(city);
        dest.writeString(detail);
        dest.writeString(province);
        dest.writeString(county);
        dest.writeString(cityName);
        dest.writeString(provinceName);
        dest.writeString(countyName);
    }
}
