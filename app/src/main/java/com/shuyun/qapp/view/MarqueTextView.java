package com.shuyun.qapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: MarqueTextView
 * @Author: ganquan
 * @CreateDate: 2019/1/21 16:52
 */
@SuppressLint("AppCompatCustomView")
public class MarqueTextView extends TextView {
    public MarqueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        //就是把这里返回true即可
        return true;
    }
}

