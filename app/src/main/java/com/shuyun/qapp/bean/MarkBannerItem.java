package com.shuyun.qapp.bean;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * homebanner
 * 创建日期：2018/9/19 18:09
 */
public class MarkBannerItem extends BannerItem {
    String markLabel;

    public MarkBannerItem(String url) {
        super(url);
    }

    public void setMarkLabel(String label) {
        this.markLabel = label;
    }

    public String getMarkLabel() {
        return markLabel;
    }
}
