package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.UmengPageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的奖品界面
 */
public class MinePrizeActivity extends BaseActivity implements OnRemotingCallBackListener<MineBean> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll_prize)
    LinearLayout llPrize;
    @BindView(R.id.tv_box_record)
    TextView tvBoxRecord; //宝箱记录

    private int status;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private MineBean mineBean;
    private CommonPopupWindow popupWindow;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        tvCommonTitle.setText("我的奖品");
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);

        loadMineHomeData();

        //友盟页面统计
        UmengPageUtil.startPage(AppConst.APP_PERSONAL_PRIZE);

        MyActivityManager1.getInstance().pushOneActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        status = intent.getIntExtra("status", 0);
        loadMineHomeData();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_mine_prize;
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("未使用");
        mTitleList.add("使用中");
        mTitleList.add("已使用");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(NoUsePrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UseInPrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UsePrizeFragment.newInstance(mineBean.getCertification()));
        //设置适配器
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
        //将tablayout与fragment关联
        tabLayout.setupWithViewPager(vp);
    }

    @OnClick({R.id.iv_back, R.id.tv_box_record})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    super.onBackPressed();
                }
                break;
            case R.id.tv_box_record: //宝箱记录
                startActivity(new Intent(mContext, BoxRecordActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            popupWindow.dismiss();
            popupWindow = null;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 获取到我的首界面数据
     */
    private void loadMineHomeData() {
        RemotingEx.doRequest(RemotingEx.Builder().getMineHomeData(), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<MineBean> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        mineBean = response.getDat();
        SaveUserInfo.getInstance(mContext).setUserInfo("cer", String.valueOf(mineBean.getCertification()));
        //添加标题
        initTitile();
        //添加fragment
        initFragment();

        if (status == 1) { //未使用
            vp.setCurrentItem(0);
        } else if (status == 2) { //使用中
            vp.setCurrentItem(1);
        } else if (status == 3) { //已使用
            vp.setCurrentItem(2);
        }
    }
}
