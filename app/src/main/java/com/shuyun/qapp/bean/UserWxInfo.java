package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 微信信息bean
 * 创建日期：2018/7/3 9:49
 */
@Data
public class UserWxInfo {

    private String nickname; // 昵称
    private Long wxBind; // 是否已经绑定微信
    private String wxHeader; // 微信头像


}
