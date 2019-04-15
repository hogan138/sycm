package com.shuyun.qapp.utils;

import android.os.Handler;

import com.umeng.analytics.MobclickAgent;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.utils
 * @ClassName: UmengPageUtil
 * @Description: 友盟页面统计
 * @Author: ganquan
 * @CreateDate: 2019/4/15 11:38
 */
public class UmengPageUtil {


    //开始统计页面
    public static void startPage(final String name) {

        //开始统计
        MobclickAgent.onPageStart(name);

        //延迟0.3秒结束统计
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.onPageEnd(name);
            }
        }, 300);
    }


}
