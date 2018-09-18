package com.shuyun.qapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyhdyh.widget.loading.factory.LoadingFactory;
import com.shuyun.qapp.R;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/9/4 11:18
 * 加载进度条
 */
public class CustomLoadingFactory implements LoadingFactory {
    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_process_dialog_color, parent, false);
        return view;
    }
}
