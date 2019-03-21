package com.shuyun.qapp.ui.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

/**
 * 启动页
 */

public class SplashActivity extends BaseActivity {


    boolean firstFlag = true; //是否首次安装

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化数据
        shipToNavigationOrFrame();

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_splash;
    }

    //判断且实现应跳转导航动画还是广告界面
    private void shipToNavigationOrFrame() {
        SharedPreferences sharedPreferences = getSharedPreferences("flag", MODE_PRIVATE);
        firstFlag = sharedPreferences.getBoolean("first", true);

        final Intent intent = new Intent();
        if (firstFlag) {
            intent.setClass(this, NavigationActivity.class);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first", false);
            editor.apply(); //apply与commit作用相同，虽没返回值，但效率更高
            startActivity(intent);
        } else {
            intent.setClass(this, WelcomeActivity.class);
            startActivity(intent);
        }
        new Handler().postDelayed(new Runnable() { //延时0.5秒
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }

}
