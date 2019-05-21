package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * homebanner
 */
public class MarkBannerItem2 extends BannerItem {

    String name; //名称
    String description;//描述

    //任意位置logo配置
    List<BannerBean.AdConfigs> adConfigs;

    public MarkBannerItem2(String url) {
        super(url);
    }

    public List<BannerBean.AdConfigs> getAdConfigs() {
        return adConfigs;
    }

    public void setAdConfigs(List<BannerBean.AdConfigs> adConfigs) {
        this.adConfigs = adConfigs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
