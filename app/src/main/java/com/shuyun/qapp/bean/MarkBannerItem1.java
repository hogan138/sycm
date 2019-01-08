package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * homebanner
 * 创建日期：2018/9/19 18:09
 */
public class MarkBannerItem1 extends BannerItem {

    //任意位置logo配置
    List<BannerBean.AdConfigs> adConfigs;

    public MarkBannerItem1(String url) {
        super(url);
    }

    public List<BannerBean.AdConfigs> getAdConfigs() {
        return adConfigs;
    }

    public void setAdConfigs(List<BannerBean.AdConfigs> adConfigs) {
        this.adConfigs = adConfigs;
    }


}
