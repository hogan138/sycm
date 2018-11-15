package com.shuyun.qapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MarkBannerItem;

import cn.kevin.banner.BannerAdapter;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.ImageLoader;

/**
 * 扩展角标
 * 创建人：${ganquan}
 * 创建日期：2018/9/19 18:07
 */
public class MarkBannerAdapter extends BannerAdapter {
    private ImageLoader imageLoader;

    public MarkBannerAdapter(ImageLoader imageLoader) {
        super(imageLoader);
        this.imageLoader = imageLoader;
    }

    @Override
    protected View createView(Context context, IBannerItem item) {

        MarkBannerItem markBannerItem = (MarkBannerItem) item;

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.mark_banner_layout, null);

        ImageView itemView = view.findViewById(R.id.imageView);
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (imageLoader != null) {
            imageLoader.onDisplayImage(context, itemView, item.ImageUrl());
        }

        TextView textView = view.findViewById(R.id.textView);
        textView.setText(markBannerItem.getMarkLabel());

        return view;
    }
}
