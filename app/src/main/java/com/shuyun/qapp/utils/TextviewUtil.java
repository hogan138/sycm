package com.shuyun.qapp.utils;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/22 16:43
 * textview 换行参差不齐
 */
public class TextviewUtil {

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
