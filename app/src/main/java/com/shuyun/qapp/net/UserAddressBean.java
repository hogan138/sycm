package com.shuyun.qapp.net;

/**
 * @Package: com.shuyun.qapp.net
 * @ClassName: UserAddressBean
 * @Description: 增加用户地址入参
 * @Author: ganquan
 * @CreateDate: 2019/4/30 10:21
 */
public class UserAddressBean {

    private String userName; //收件人
    private String userPhone; //手机号码
    private Long isDefault; //是否默认
    private String city;//市代码
    private String detail;//详细地址
    private String province;//省份代码
    private String county;//区县代码
    private String cityName;//城市名称
    private String provinceName;//省份名称
    private String countyName; //区县名称
    private Long id;//地址id

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
