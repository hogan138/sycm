package com.shuyun.qapp.animation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;

import com.shuyun.qapp.utils.DisplayUtil;

public class HorizontalStackPageTransformer implements ViewPager.PageTransformer {
    private static final float CENTER_PAGE_SCALE = 0.85f;
    private float horizontalOffsetBase;
    private int offscreenPageLimit;

    public HorizontalStackPageTransformer(Context context, int offscreenPageLimit) {
        this.offscreenPageLimit = offscreenPageLimit;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int pagerWidth = dm.widthPixels;
        horizontalOffsetBase = (pagerWidth - pagerWidth * CENTER_PAGE_SCALE)
                / 2
                / offscreenPageLimit
                + DisplayUtil.dp2px(context, 15);
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position >= 0) {
            float translationX = (horizontalOffsetBase - view.getWidth()) * position;
            view.setTranslationX(translationX);
            view.setTranslationY(30 * position);
        }
        if (position > -1 && position < 0) {
            view.setAlpha((position * position * position + 1));
        } else if (position > offscreenPageLimit - 1) {
            view.setAlpha((float) (1 - position + Math.floor(position)));
        } else {
            view.setAlpha(1);
        }
        if (position == 0) {
            view.setScaleX(CENTER_PAGE_SCALE);
            view.setScaleY(CENTER_PAGE_SCALE);
        } else {
            float scaleFactor = Math.min(CENTER_PAGE_SCALE - position * 0.1f, CENTER_PAGE_SCALE);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }

    }
}