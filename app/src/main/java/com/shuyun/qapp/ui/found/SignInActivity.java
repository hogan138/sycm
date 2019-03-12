package com.shuyun.qapp.ui.found;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 签到
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.iv_signIn_logo)
    ImageView ivSignInLogo;
    @BindView(R.id.tv_signIn_day)
    TextView tvSignInDay;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    private boolean isSignIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("签到");
        ivBack.setOnClickListener(this);
        ivSignInLogo.setOnClickListener(this);

        //添加标题
        initTitile();
        //添加fragment
        initFragment();

        if (!isSignIn) {
            ivSignInLogo.setBackgroundResource(R.mipmap.un_signin_logo);
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_sign_in;
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("新手任务");
        mTitleList.add("每日任务");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NewTaskFragment());
        mFragmentList.add(new NewTaskFragment());
        //设置适配器
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
        //将tablayout与fragment关联
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_signIn_logo:
                ivSignInLogo.setBackgroundResource(R.mipmap.has_signin_logo);
                break;
            default:
                break;
        }
    }
}
