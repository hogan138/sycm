package com.shuyun.qapp.utils;

import android.content.Context;

public class DisplayUtil {

    public static float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
