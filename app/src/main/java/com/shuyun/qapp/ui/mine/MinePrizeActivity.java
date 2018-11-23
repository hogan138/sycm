package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的奖品界面
 * 修改于：2018/6/9
 */
public class MinePrizeActivity extends BaseActivity {

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

    private int status;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private MineBean mineBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("我的奖品");
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);

        loadMineHomeData();

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
//        mTitleList.add("全部");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
//        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NoUsePrizeFragment().newInstance(mineBean.getCertification()));
        mFragmentList.add(new UseInPrizeFragment().newInstance(mineBean.getCertification()));
        mFragmentList.add(new UsePrizeFragment().newInstance(mineBean.getCertification()));
//        mFragmentList.add(new AllPrizeFragment().newInstance(mineBean.getCertification()));

    }


    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

    }


    @OnClick({R.id.iv_back})
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
            default:
                break;
        }
    }

    private CommonPopupWindow popupWindow;


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
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMineHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<MineBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<MineBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            mineBean = listDataResponse.getDat();
                            SaveUserInfo.getInstance(MinePrizeActivity.this).setUserInfo("cer", String.valueOf(mineBean.getCertification()));
                            //添加标题
                            initTitile();
                            //添加fragment
                            initFragment();
                            //设置适配器
                            vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
                            //将tablayout与fragment关联
                            tabLayout.setupWithViewPager(vp);

                            if (status == 1) { //未使用
                                vp.setCurrentItem(0);
                            } else if (status == 2) { //使用中
                                vp.setCurrentItem(1);
                            } else if (status == 3) { //已使用
                                vp.setCurrentItem(2);
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(MinePrizeActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                        return;
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
