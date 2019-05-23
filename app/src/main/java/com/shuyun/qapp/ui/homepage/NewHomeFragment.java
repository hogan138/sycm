package com.shuyun.qapp.ui.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.androidkun.xtablayout.XTabLayout;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.TabViewPagerAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FloatWindowBean;
import com.shuyun.qapp.bean.HomeTabBeans;
import com.shuyun.qapp.bean.TitleBean;
import com.shuyun.qapp.manager.FragmentTouchManager;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.ui.webview.WebFragment;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.NotificationsUtils;
import com.shuyun.qapp.utils.ObjectUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.manager.FloatImageviewManage;
import com.shuyun.qapp.view.MainActivityDialogInfo;
import com.shuyun.qapp.view.NotifyDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Package: com.shuyun.qapp.ui.homepage
 * @ClassName: NewHomeFragment
 * @Description: 新版首页
 * @Author: ganquan
 * @CreateDate: 2019/5/6 15:23
 */
public class NewHomeFragment extends BaseFragment implements OnRemotingCallBackListener<Object>, XTabLayout.OnTabSelectedListener, TagFlowLayout.OnTagClickListener, FragmentTouchManager.FragmentTouchListener {

    Unbinder unbinder;
    @BindView(R.id.home_tab_layout)
    XTabLayout homeTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.ll_home_fragment)
    RelativeLayout llHomeFragment;
    @BindView(R.id.iv_bx)
    ImageView ivBx;//宝箱晃动动画
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo;//浮窗

    private Activity mContext;
    private Handler mHandler = new Handler();
    private boolean isLoading = false;

    //顶部tablayout
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<TitleBean> homeTabBeansList = new ArrayList<>();

    private String tabDataString = null;

    private RelativeLayout rl_sort; //分类
    private boolean dialogShowing = false; //弹窗是否显示
    private TagFlowLayout tagFlowLayout;
    private TagAdapter<TitleBean> tagAdapter;
    private TabViewPagerAdapter tabViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        FragmentTouchManager.instance().registerFragmentTouchListener(this);

        if (mContext == null)
            return;
        rl_sort = mContext.findViewById(R.id.rl_sort);
        tagFlowLayout = mContext.findViewById(R.id.sort_flowlayout);
        rl_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_sort.setVisibility(View.GONE);
                dialogShowing = false;
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 10);

            //友盟页面统计
            UmengPageUtil.startPage(AppConst.APP_HOME);

            //记录标记
            SaveUserInfo.getInstance(mContext).setUserInfo("umeng_from", "home");
        }
    }

    @OnClick({R.id.ll_sort})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_sort:
                rl_sort.setVisibility(dialogShowing ? View.GONE : View.VISIBLE);

                if (!dialogShowing) {
                    //TODO 设置选中值
                    tagAdapter.notifyDataChanged();
                }

                dialogShowing = !dialogShowing;
                break;
        }
    }

    //初始化tablayout
    private void init(int size) {
        tabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager(),
                mFragmentList,
                homeTabBeansList);
        //设置适配器
        viewpager.setOffscreenPageLimit(size);
        viewpager.setAdapter(tabViewPagerAdapter);
        viewpager.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(homeTabLayout));
        //将tablayout与fragment关联
        homeTabLayout.setxTabDisplayNum(7);
        homeTabLayout.setupWithViewPager(viewpager);
        homeTabLayout.addOnTabSelectedListener(this);

        tagFlowLayout.setMaxSelectCount(1);
        tagAdapter = new TagAdapter<TitleBean>(homeTabBeansList) {
            @Override
            public View getView(FlowLayout parent, int position, TitleBean bean) {
                HomeTabBeans $bean = (HomeTabBeans) bean;
                View view = LayoutInflater.from(mContext).inflate(R.layout.home_stas_layout, tagFlowLayout, false);
                TextView label = view.findViewById(R.id.label);
                //设置label
                label.setText($bean.getLabel());
                if ($bean.getSelected()) {
                    label.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sort_tag_checked_bg));
                    label.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    label.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sort_tag_normal_bg));
                    label.setTextColor(ContextCompat.getColor(mContext, R.color.color_66));
                }
                return view;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
        tagFlowLayout.setOnTagClickListener(this);
    }

    @Override
    public void refresh() {
        if (mContext != null) {
            long newNowMills = (long) SharedPrefrenceTool.get(mContext, "nowMills", (TimeUtils.getNowMills() - 86400000 * 7));//获取首选项中的时间,默认值为七天前的时间戳
            long timeSpan = TimeUtils.getTimeSpan(newNowMills, TimeUtils.getNowMills(), TimeConstants.DAY);//当前时间和首选项中时间差
            /**
             * 如果通知权限关闭且当前时间和首选项中的时间相差大于等于7天(每7天提醒一次),弹窗提示
             */
            if (!NotificationsUtils.isNotificationEnabled(mContext) && timeSpan >= 7) {
                //当前弹窗提示时间
                long nowMills = TimeUtils.getNowMills();
                SharedPrefrenceTool.put(mContext, "nowMills", nowMills);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果通知栏未打开,弹出前往设置打开通知栏的弹窗
                        NotifyDialog.dialogShow(mContext);
                    }
                }, 10);
            }
        }

        //tab数据
        loadTabInfo();

        //获取弹框信息
        getDialogInfo();

        //用户已登录
        if (AppConst.isLogin()) {
            //用户活跃度
            RemotingEx.doRequest(ApiServiceBean.activeness(), null, null);

            //获取宝箱数量
            loadTreasureBoxNum();

            //获取首页浮窗
            loadHomeFloatWindow();
        } else {
            //用户未登录
            try {
                ivBx.clearAnimation();
                ivBx.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }
    }

    /**
     * tab数据
     */
    public void loadTabInfo() {
        RemotingEx.doRequest("getTabInfo", ApiServiceBean.newHometab(), null, this);
    }

    /**
     * 获取宝箱数量
     */
    private void loadTreasureBoxNum() {
        RemotingEx.doRequest("loadTreasureBoxNum", ApiServiceBean.getTreasureNumV2(), null, this);
    }

    /**
     * 首页浮窗
     */
    public void loadHomeFloatWindow() {
        RemotingEx.doRequest("getHomefloatwindow", ApiServiceBean.homeFloatWindow(), null, this);
    }

    /**
     * 获取活动弹框信息
     */
    private void getDialogInfo() {
        RemotingEx.doRequest("configDialog", ApiServiceBean.configDialog(), null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == AppConst.INVITE_CODE
                || requestCode == AppConst.GROUP_CODE
                || requestCode == AppConst.INTEGRAL_CODE
                || requestCode == AppConst.TREASURE_CODE
                || requestCode == AppConst.REAL_CODE
                || requestCode == AppConst.H5_CODE
                || requestCode == AppConst.AGAINST_CODE
                || requestCode == AppConst.OPEN_BOX_CODE
                || requestCode == AppConst.WITHDRAW_INFO_CODE
        )) {
            LoginDataManager.instance().handler(mContext, null);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentTouchManager.instance().unRegisterFragmentTouchListener(this);
        unbinder.unbind();

    }


    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        //tab数据
        if ("getTabInfo".equals(action)) {
            List<HomeTabBeans> list = ObjectUtil.cast(response.getDat());
            String str = JSON.toJSONString(list);
            if (tabDataString != null && tabDataString.equals(str)) {
                return;
            }
            tabDataString = str;

            //设置tabs
            homeTabBeansList.clear();
            HomeTabBeans dBean = new HomeTabBeans();
            dBean.setTabTitle("精选");
            dBean.setTabId(0L);
            dBean.setSelected(true);
            dBean.setLabel("精选");
            dBean.setType(2L);
            homeTabBeansList.add(dBean);
            homeTabBeansList.addAll(list);

            //动态tab添加
            mFragmentList.clear();
            mFragmentList.add(new HomeSelectFragment());
            //添加标题
            //添加fragment
            for (int i = 1; i < homeTabBeansList.size(); i++) {
                HomeTabBeans bean = ObjectUtil.cast(homeTabBeansList.get(i));
                bean.setTabTitle(bean.getLabel());
                Long type = bean.getType();
                if (type == 2) {
                    //原生页面
                    mFragmentList.add(TabClassifyFragment.newInstance(bean.getTabId()));
                } else if (type == 1) {
                    //h5页面
                    mFragmentList.add(WebFragment.newInstance(bean.getH5Url())); //h5页面
                }
            }

            //初始化tablayout
            int size = homeTabBeansList.size();
            init(size);

        } else if ("loadTreasureBoxNum".equals(action)) {
            BoxBean boxBean = (BoxBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(boxBean)) {
                if (boxBean.getCount() > 0) {
                    if (boxBean.getCount() == 1 && boxBean.getSource() == 3) {//从微信获取且只有一个宝箱，直接跳开宝箱页面
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("FirstRun", 0);
                        Boolean first_run = sharedPreferences.getBoolean("Main", true);
                        if (first_run) {
                            sharedPreferences.edit().putBoolean("Main", false).apply();
                            Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
                            intent.putExtra("BoxBean", boxBean);
                            intent.putExtra("main_box", "main_box");
                            startActivity(intent);
                        }
                    } else {//如果宝箱数量大于0,首页左下角显示宝箱摇晃动画
                        ivBx.setVisibility(View.VISIBLE);
                        TranslateAnimation animation = new TranslateAnimation(5, -5, 0, 0);
                        animation.setInterpolator(new OvershootInterpolator());
                        animation.setDuration(500);
                        animation.setRepeatCount(Animation.INFINITE);
                        animation.setRepeatMode(Animation.REVERSE);
                        ivBx.startAnimation(animation);
                        ivBx.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                Intent intent = new Intent(mContext, MinePrizeActivity.class);
                                intent.putExtra("status", 1);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    ivBx.clearAnimation();
                    ivBx.setVisibility(View.GONE);
                }
                //是否实名认证
                if (!EncodeAndStringTool.isStringEmpty(boxBean.getIsRealName())) {
                    if (boxBean.getIsRealName().equals("true")) {
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", "1");
                    } else {
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", "0");
                    }
                }
            }
        } else if ("getHomefloatwindow".equals(action)) {
            //首页浮窗
            final FloatWindowBean floatWindowBean = (FloatWindowBean) response.getDat();
            Long status = floatWindowBean.getStatus();
            if (status == 1) {
                rlLogo.setVisibility(View.VISIBLE);
                //添加图片
                FloatImageviewManage.execute(floatWindowBean, rlLogo, mContext);
            } else {
                rlLogo.setVisibility(View.GONE);
            }
        } else if ("configDialog".equals(action)) {
            ConfigDialogBean configDialogBean = (ConfigDialogBean) response.getDat();
            MainActivityDialogInfo.info(configDialogBean, mContext);
        }
    }

    @Override
    public void clear() {
        super.clear();
        tabDataString = null;
    }

    //tabs选择事件
    @Override
    public void onTabSelected(XTabLayout.Tab tab) {
        int selectIndex = tab.getPosition();
        for (TitleBean bean : homeTabBeansList) {
            ((HomeTabBeans) bean).setSelected(false);
        }
        HomeTabBeans el = (HomeTabBeans) homeTabBeansList.get(selectIndex);
        el.setSelected(true);
        tagAdapter.notifyDataChanged();
    }

    @Override
    public void onTabUnselected(XTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(XTabLayout.Tab tab) {

    }

    //标签点击事件
    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        viewpager.setCurrentItem(position);
        rl_sort.setVisibility(View.GONE);
        dialogShowing = false;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            FloatImageviewManage.hideImageview();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            FloatImageviewManage.showImageview();
        }
        return false;
    }
}
