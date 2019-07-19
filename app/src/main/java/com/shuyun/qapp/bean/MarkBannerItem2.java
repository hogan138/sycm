package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * homebanner
 */
@Data
public class MarkBannerItem2 extends BannerItem {

    String name; //名称
    String description;//描述

    //任意位置logo配置
    List<BannerBean.AdConfigs> adConfigs;

    public MarkBannerItem2(String url) {
        super(url);
    }


}
