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
    @BindView(R.id.tv_sign_date_one)
    TextView tvSignDateOne;
    @BindView(R.id.iv_sign_select_one)
    ImageView ivSignSelectOne;
    @BindView(R.id.tv_sign_content_one)
    TextView tvSignContentOne;
    @BindView(R.id.line_one)
    View lineOne;
    @BindView(R.id.tv_sign_date_two)
    TextView tvSignDateTwo;
    @BindView(R.id.iv_sign_select_two)
    ImageView ivSignSelectTwo;
    @BindView(R.id.tv_sign_content_two)
    TextView tvSignContentTwo;
    @BindView(R.id.line_two)
    View lineTwo;
    @BindView(R.id.tv_sign_date_three)
    TextView tvSignDateThree;
    @BindView(R.id.iv_sign_select_three)
    ImageView ivSignSelectThree;
    @BindView(R.id.tv_sign_content_three)
    TextView tvSignContentThree;
    @BindView(R.id.line_three)
    View lineThree;
    @BindView(R.id.tv_sign_date_four)
    TextView tvSignDateFour;
    @BindView(R.id.iv_sign_select_four)
    ImageView ivSignSelectFour;
    @BindView(R.id.tv_sign_content_four)
    TextView tvSignContentFour;
    @BindView(R.id.line_four)
    View lineFour;
    @BindView(R.id.tv_sign_date_five)
    TextView tvSignDateFive;
    @BindView(R.id.iv_sign_select_five)
    ImageView ivSignSelectFive;
    @BindView(R.id.tv_sign_content_five)
    TextView tvSignContentFive;
    @BindView(R.id.line_five)
    View lineFive;
    @BindView(R.id.tv_sign_date_six)
    TextView tvSignDateSix;
    @BindView(R.id.iv_sign_select_six)
    ImageView ivSignSelectSix;
    @BindView(R.id.tv_sign_content_six)
    TextView tvSignContentSix;
    @BindView(R.id.line_six)
    View lineSix;
    @BindView(R.id.tv_sign_date_seven)
    TextView tvSignDateSeven;
    @BindView(R.id.iv_sign_select_seven)
    ImageView ivSignSelectSeven;
    @BindView(R.id.tv_sign_content_seven)
    TextView tvSignContentSeven;

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
