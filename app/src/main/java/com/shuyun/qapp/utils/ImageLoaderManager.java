package com.shuyun.qapp.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * @user sunxiao
 * @desc 图片加载管理
 */
public class ImageLoaderManager {

    public static void LoadImage(Context context, String imgUrl, ImageView imageView, int defaultImg) {

        try {
            Glide.with(context)
                    .load(imgUrl)
//                    .placeholder(defaultImg)
                    .dontAnimate() //解决圆形图显示占位图问题
//                    .error(defaultImg)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        } catch (Exception e) {
        }
    }


    public static void loadImgToBackground(Context context, String imgUrl, final View view, int defaultImg) {
        Glide.with(context).load(imgUrl)
                .placeholder(defaultImg)
                .dontAnimate() //解决圆形图显示占位图问题
                .error(defaultImg)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new ViewTarget<View, GlideDrawable>(view) {
                    //括号里为需要加载的控件
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }


}
