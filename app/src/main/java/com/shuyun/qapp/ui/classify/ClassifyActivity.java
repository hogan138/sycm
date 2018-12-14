package com.shuyun.qapp.ui.classify;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseFragment;

/**
 * 首页查看更多分类界面
 */
public class ClassifyActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment classify = getSupportFragmentManager().findFragmentById(R.id.classify_fragment);
                ((BaseFragment)classify).refresh();
            }
        }, 320);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_classify;
    }
}
