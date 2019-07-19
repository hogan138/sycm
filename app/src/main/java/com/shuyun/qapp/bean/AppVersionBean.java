package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：gq
 * 创建日期：2018/6/13 21:53
 */
@Data
public class AppVersionBean {
    private String appVersion;//最新版本号
    /**
     * 升级模式
     * 0——没有新版本
     * 1——推荐升级
     * 2——强制升级
     */
    private Long mode;
    private String url;//升级的跳转地址(有升级时必需)

    private List<String> apis;//接口的轮询地址(数组)

    private String boxUrl; //开宝箱的H5跳转地址
    private String examUrl;//答题的H5跳转地址


}
