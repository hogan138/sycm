package com.shuyun.qapp.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.utils
 * @ClassName: GlideConfiguration
 * @Description: glide加载图片优化
 * @Author: ganquan
 * @CreateDate: 2018/12/12 10:13
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
