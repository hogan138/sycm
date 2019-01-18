package com.shuyun.qapp.bean;

/**
 * 提现返回bean
 */
public class AddWithdrawResultBean {


    /**
     * stateName : 信息错误
     * bankType : 1
     * type : withdraw
     * message : 提现信息与其他账号重复，如有疑问，请联系客服
     * title : 请点击重新完善提现信息
     * enabled : true
     * status : 2
     */

    private String stateName;
    private Long bankType;
    private String type;
    private String message;
    private String title;
    private Boolean enabled;
    private Long status;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
