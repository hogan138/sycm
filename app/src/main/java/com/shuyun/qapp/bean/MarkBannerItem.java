package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * homebanner
 * 创建日期：2018/9/19 18:09
 */
public class MarkBannerItem extends BannerItem {
    String markLabel;

    //任意位置logo配置
    List<GroupBean.AdConfigs> adConfigs;

    public MarkBannerItem(String url) {
        super(url);
    }

    public void setMarkLabel(String label) {
        this.markLabel = label;
    }

    public String getMarkLabel() {
        return markLabel;
    }

    public List<GroupBean.AdConfigs> getAdConfigs() {
        return adConfigs;
    }

    public void setAdConfigs(List<GroupBean.AdConfigs> adConfigs) {
        this.adConfigs = adConfigs;
    }


}
