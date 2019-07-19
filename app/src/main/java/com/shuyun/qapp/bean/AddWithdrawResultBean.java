package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 提现返回bean
 */
@Data
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

}
