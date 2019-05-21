package com.shuyun.qapp.manager;

import com.shuyun.qapp.base.BaseActivity;

/**
 * 当前登录回调活动的activity
 */
public class ActivityCallManager {

    private static ActivityCallManager manager = null;
    private BaseActivity activity;

    protected ActivityCallManager() {

    }

    public static ActivityCallManager instance() {
        if (manager == null)
            manager = new ActivityCallManager();
        return manager;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }
}
