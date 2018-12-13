package com.shuyun.qapp.ui.classify;

import android.os.Bundle;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页查看更多分类界面
 */
public class ClassifyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_classify;
    }

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }
}
