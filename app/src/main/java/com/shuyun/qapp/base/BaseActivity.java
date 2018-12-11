package com.shuyun.qapp.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
//    /***是否显示标题栏*/
//    private boolean isshowtitle = true;
//    /***是否显示标题栏*/
//    private boolean isshowstate = true;
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        if (!isshowtitle) {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }
//
        //全屏显示
//        if (isshowstate) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        //底部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
        }

        //设置布局
        setContentView(intiLayout());

        //改变底部导航栏颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
//        }

        //初始化控件
//        initView();
        //设置数据
//        initData();

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

    /**
     * 初始化布局
     */
//    public abstract void initView();

    /**
     * 设置数据
     */
//    public abstract void initData();

    /**
     * 是否设置标题栏
     *
     * @return
     */
//    public void setTitle(boolean ishow) {
//        isshowtitle = ishow;
//    }

    /**
     * 设置是否显示状态栏
     *
     * @param ishow
     */
//    public void setState(boolean ishow) {
//        isshowstate = ishow;
//    }


}
