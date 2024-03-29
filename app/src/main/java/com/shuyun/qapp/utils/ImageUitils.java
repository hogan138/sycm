package com.shuyun.qapp.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.utils
 * @ClassName: ImageUitils
 * @Description: url转drawable
 * @Author: ganquan
 * @CreateDate: 2019/3/13 14:25
 */
public class ImageUitils {

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }
}
