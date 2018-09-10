package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
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
 * <p>
 * 修改于：2018/6/9
 */
public class MinePrizeActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

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

    /**
     * 是否实名认证
     * 0——未实名认证
     * 1——已实名认证
     * 2——审核中
     * 3——未通过
     * 4——拉黑
     */
    private int status;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private static final String TAG = "MinePrizeActivity";
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
        mTitleList.add("全部");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NoUsePrizeFragment().newInstance(mineBean.getCertification()));
        mFragmentList.add(new UseInPrizeFragment().newInstance(mineBean.getCertification()));
        mFragmentList.add(new UsePrizeFragment().newInstance(mineBean.getCertification()));
        mFragmentList.add(new AllPrizeFragment().newInstance(mineBean.getCertification()));

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
//                    finish();
                }
                break;
            default:
                break;
        }
    }

    private CommonPopupWindow popupWindow;

    /**
     * 实名认证popupWindow
     */
    public void showAuthPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.real_name_auth_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.real_name_auth_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llPrize, Gravity.CENTER, 0, 0);
    }


    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.real_name_auth_popupwindow:
                ImageView ivClose1 = (ImageView) view.findViewById(R.id.iv_close_icon1);
                Button btnRealNameAuth = (Button) view.findViewById(R.id.btn_real_name_auth1);
                ivClose1.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btnRealNameAuth.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        startActivity(new Intent(MinePrizeActivity.this, RealNameAuthActivity.class));
                    }
                });
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
//            finish();
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
                            } else if (status == 0) { //全部
                                vp.setCurrentItem(3);
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
