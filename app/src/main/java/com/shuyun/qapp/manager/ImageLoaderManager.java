package com.shuyun.qapp.manager;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @user sunxiao
 * @desc 图片加载管理
 */
public class ImageLoaderManager {

    public static void LoadImage(Context context, String imgUrl, ImageView imageView, int defaultImg) {

        try {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(defaultImg)
                    .dontAnimate() //解决圆形图显示占位图问题
                    .error(defaultImg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } catch (Exception e) {
        }
    }

}
