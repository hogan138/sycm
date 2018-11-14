package com.shuyun.qapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sunxiao on 2018/4/28.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
