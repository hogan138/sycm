package com.shuyun.qapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import cn.kevin.banner.BaseViewAdapter;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.ImageLoader;

/**
 * 首页banner
 * 项目名称：android
 * 创建人：${ganquan}
 * 创建日期：2018/9/19 18:07
 */
public class HomeBannerAdapter extends BaseViewAdapter<IBannerItem> {
    private ImageLoader imageLoader;

    public HomeBannerAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    protected View createView(Context context, IBannerItem item) {
        ImageView itemView = new ImageView(context);
        itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (imageLoader != null) {
            imageLoader.onDisplayImage(context, itemView, item.ImageUrl());
        }
        return itemView;
    }
}
