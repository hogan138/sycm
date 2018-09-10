package com.shuyun.qapp.utils;

import android.app.Activity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxiao on 2018/5/23.
 */

public class AtyContainer {

    private AtyContainer() {
    }

    private static AtyContainer instance;
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public synchronized static AtyContainer getInstance() {
        if (null == instance) {
            instance = new AtyContainer();
        }
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
