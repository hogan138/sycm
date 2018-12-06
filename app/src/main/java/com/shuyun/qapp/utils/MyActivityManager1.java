package com.shuyun.qapp.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * 创建人：${ganquan}
 * activity管理类
 */
public class MyActivityManager1 {

    private static MyActivityManager1 instance;
    private Stack<Activity> activityStack;//activity栈

    private MyActivityManager1() {
    }

    //单例模式
    public static MyActivityManager1 getInstance() {
        if (instance == null) {
            instance = new MyActivityManager1();
        }
        return instance;
    }

    //把一个activity压入栈中
    public void pushOneActivity(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(actvity);
        Log.d("MyActivityManager ", "size = " + activityStack.size());
    }

    //获取栈顶的activity，先进后出原则
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    //移除一个activity
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }

        }
    }

    //退出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }
}

