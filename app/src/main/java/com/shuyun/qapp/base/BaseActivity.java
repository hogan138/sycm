package com.shuyun.qapp.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.net.HeartBeatManager;

/**
 * 2018/6/9
 * ganquan
 * base基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        //设置布局
        setContentView(intiLayout());

        //改变底部导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    @Override
    protected void onResume() {
        super.onResume();

        HeartBeatManager.instance().start(this);
    }
}
