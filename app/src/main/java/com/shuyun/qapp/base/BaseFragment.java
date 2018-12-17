package com.shuyun.qapp.base;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    /**
     * 刷新数据
     */
    public abstract void refresh();

    /**
     * 清理数据
     */
    public void clear() {

    }
}
