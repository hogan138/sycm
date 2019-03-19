package com.shuyun.qapp.ui.found;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FloatWindowBean;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.view.EnhanceTabLayout;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.shuyun.qapp.view.ViewPagerScroller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;

/**
 * 活动Fragment
 */
public class FoundFragment extends BaseFragment implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    Unbinder unbinder;
    @BindView(R.id.banner)
    BannerViewPager mBannerView;
    @BindView(R.id.tab_layout)
    EnhanceTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.activityRegion)
    LinearLayout activityRegion;
    @BindView(R.id.rl_found)
    RelativeLayout rlFound;
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private Activity mContext;

    private boolean isLoading = false;
    private Handler mHandler = new Handler();

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<FoundDataBean.BannerBean> bannerData = new ArrayList<>();
    private String bannerDataString = null;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;


    static ImageView imageView; //浮窗图片

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("发现");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoading) {
                        init();
                    }
                    isLoading = true;

                    refresh();
                }
            }, 10);
        }
    }

    private void init() {

        MarkBannerItem1 i = new MarkBannerItem1("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        bannerList.add(i);
        //设置轮播图
        markBannerAdapter1 = new MarkBannerAdapter1(new GlideImageLoader(), mContext);
        markBannerAdapter1.setData(mContext, bannerList);
        mBannerView.setBannerAdapter(markBannerAdapter1);

        //设置index 在viewpager下面
        final ViewPager mViewpager = (ViewPager) mBannerView.getChildAt(0);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScroller = new ViewPagerScroller(getActivity());
        pagerScroller.setScrollDuration(400);
        pagerScroller.initViewPagerScroll(mViewpager);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition = position % bannerList.size();
                markBannerAdapter1.refreshAdConfig(bannerList.get(realPosition), realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mBannerView.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
            @Override
            public void onClick(IBannerItem data) {
                for (int i = 0; i < bannerData.size(); i++) {
                    FoundDataBean.BannerBean bannerBean = bannerData.get(i);
                    if (data.ImageUrl().equals(bannerBean.getPicture())) {
                        LoginDataManager.instance().addData(LoginDataManager.BANNER_LOGIN, bannerBean);
                        String action = bannerBean.getAction();
                        String h5Url = bannerBean.getH5Url();
                        Long is_Login = bannerBean.getIsLogin();
                        LoginJumpUtil.dialogSkip(action, mContext, bannerBean.getContent(), h5Url, is_Login);
                    }
                }
            }
        });
    }

    /**
     * 发现页数据
     */
    public void loadHomeInfo() {
        RemotingEx.doRequest("foundInfo", ApiServiceBean.foundInfo(), null, this);
    }

    /**
     * 浮窗数据
     */
    public void loadfloatWindow() {
        RemotingEx.doRequest("floatWindow", ApiServiceBean.floatWindow(), null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh() {

        //发现页数据
        loadHomeInfo();

        //浮窗数据
        loadfloatWindow();
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
        try {
            if ("foundInfo".equals(action)) {
                FoundDataBean foundDataBean = (FoundDataBean) response.getDat();

                bannerData = foundDataBean.getBanner();
                if (!EncodeAndStringTool.isListEmpty(bannerData)) {
                    String str = JSON.toJSONString(bannerData);
                    if (bannerDataString != null && bannerDataString.equals(str)) {
                        return;
                    }
                    bannerDataString = str;

                    //设置轮播图
                    bannerList.clear();
                    for (int i = 0; i < bannerData.size(); i++) {
                        FoundDataBean.BannerBean bean = bannerData.get(i);
                        MarkBannerItem1 item = new MarkBannerItem1(bean.getBaseImage());
                        bannerList.add(item);
                    }
                    markBannerAdapter1.clearViews();
                    markBannerAdapter1.setData(mContext, bannerList);
                    //重新生成单位条
                    mBannerView.setBannerAdapter(markBannerAdapter1);
                }

                //动态布局区域
                FoundDataBean.RegionBean regionBean = foundDataBean.getRegion();
                //动态添加布局
                activityRegion.removeAllViews();
                activityRegion.addView(ActivityRegionManager1.getView(mContext, regionBean, rlFound));


                //动态tab添加
                mTitleList = new ArrayList<>();
                mFragmentList = new ArrayList<>();
                List<FoundDataBean.TablesBean> tablesBeanList = foundDataBean.getTables();
                //添加fragment
                for (int i = 0; i < tablesBeanList.size(); i++) {
                    mTitleList.add(tablesBeanList.get(i).getTitle());
                    if (tablesBeanList.get(i).getType() == 1) {
                        mFragmentList.add(WebFragment.newInstance(tablesBeanList.get(i).getH5Url())); //h5页面
                    } else if (tablesBeanList.get(i).getType() == 2) {
                        mFragmentList.add(HotGroupFragment.newInstance(tablesBeanList.get(i).getGroups()));//热门题组
                    }
                }

                //添加标题
                for (int i = 0; i < mTitleList.size(); i++) {
                    tabLayout.addTab(mTitleList.get(i));
                }
                //设置适配器
                vp.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList));
                vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        hideImageview();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showImageview();
                            }
                        }, 1000);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                //将tablayout与fragment关联
                tabLayout.setupWithViewPager(vp);

            } else if ("floatWindow".equals(action)) {
                final FloatWindowBean floatWindowBean = (FloatWindowBean) response.getDat();
                Long status = floatWindowBean.getStatus();
                if (status == 1) {
                    rlLogo.setVisibility(View.VISIBLE);
                    //添加图片
                    execute(floatWindowBean, rlLogo, mContext);
                } else {
                    rlLogo.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }

    }


    //添加图片
    public void execute(final FloatWindowBean anyPositionBean, RelativeLayout rootView, final Activity context) {
        try {
            rootView.removeAllViews();
            //获取布局宽高
            rootView.requestLayout();
            int rH = rootView.getMeasuredHeight();
            int rW = rootView.getMeasuredWidth();

            int location = 6; //位置
            Long shadow = anyPositionBean.getShadow(); //是否有阴影

            //初始化数据
            //图片间距
            String[] ps = anyPositionBean.getPadding().split(",");
            int[] padds = new int[]{Integer.valueOf(ps[0]), Integer.valueOf(ps[1]), Integer.valueOf(ps[2]), Integer.valueOf(ps[3])};

            //广告间距
            String[] ps1 = anyPositionBean.getMargin().split(",");
            int[] layout_margin = new int[]{Integer.valueOf(ps1[0]), Integer.valueOf(ps1[1]), Integer.valueOf(ps1[2]), Integer.valueOf(ps1[3])};
            int x = 0, y = 0, w = (int) dp2px(context, anyPositionBean.getWidth() + padds[0] + padds[2]), h = (int) dp2px(context, anyPositionBean.getHeight() + padds[1] + padds[3]);
            //计算xy
            if (location == 1) {//左上角
                x = (int) dp2px(context, layout_margin[0]);
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 2) {//左上中
                x = (rW - w) / 2;
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 3) {//右上角
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 4) {//左中
                x = (int) dp2px(context, layout_margin[0]);
                y = (rH - h) / 2;
            } else if (location == 5) {//中间
                x = (rW - w) / 2;
                y = (rH - h) / 2;
            } else if (location == 6) {//右中
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (rH - h) / 2;
            } else if (location == 7) {//左下角
                x = (int) dp2px(context, layout_margin[0]);
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            } else if (location == 8) {//左下中
                x = (rW - w) / 2;
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            } else if (location == 9) {//右下角
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            }

            View childView = LayoutInflater.from(context).inflate(R.layout.item_any_position_img, null);
            RelativeLayout layoutView = childView.findViewById(R.id.layout);
            //设置位置
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            params.leftMargin = x;
            params.topMargin = y;
            layoutView.setLayoutParams(params);

            imageView = childView.findViewById(R.id.image);
            GlideUtils.LoadImage(context, anyPositionBean.getPicture(), imageView);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            imageParams.setMargins((int) dp2px(context, padds[0]), (int) dp2px(context, padds[1]), (int) dp2px(context, padds[2]), (int) dp2px(context, padds[3]));
            imageView.setLayoutParams(imageParams);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String action = anyPositionBean.getAction();
                    String content = anyPositionBean.getContent();
                    String h5Url = anyPositionBean.getH5Url();
                    LoginJumpUtil.dialogSkip(action, context, content, h5Url, (long) 0);
                }
            });

            //设置阴影
            RelativeLayout shadowView = childView.findViewById(R.id.shadow);
            if (shadow == 1) {
                shadowView.setVisibility(View.VISIBLE);
                GradientDrawable drawable = new GradientDrawable();
                String[] ds = anyPositionBean.getShadowRadius().split(",");
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                float[] fs = new float[]{dp2px(context, Float.valueOf(ds[0])),
                        dp2px(context, Float.valueOf(ds[0])),
                        dp2px(context, Float.valueOf(ds[1])),
                        dp2px(context, Float.valueOf(ds[1])),
                        dp2px(context, Float.valueOf(ds[2])),
                        dp2px(context, Float.valueOf(ds[2])),
                        dp2px(context, Float.valueOf(ds[3])),
                        dp2px(context, Float.valueOf(ds[3]))};
                drawable.setCornerRadii(fs);
                drawable.setColor(Color.parseColor(anyPositionBean.getShadowColor()));
                shadowView.setBackground(drawable);
                shadowView.setAlpha(Float.valueOf(anyPositionBean.getShadowAlpha()));

            } else {
                shadowView.setVisibility(View.GONE);
            }

            rootView.addView(childView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //恢复显示
    public static void showImageview() {
        try {
            Animation anim = new RotateAnimation(-90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            TranslateAnimation translateAnim = new TranslateAnimation(Animation.ABSOLUTE, 90, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(anim);
            set.addAnimation(translateAnim);
            set.setFillAfter(true);// 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            imageView.startAnimation(set);
        } catch (Exception e) {

        }
    }

    //靠边隐藏
    public static void hideImageview() {
        try {
            Animation anim = new RotateAnimation(0f, -90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            TranslateAnimation translateAnim = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 90, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(anim);
            set.addAnimation(translateAnim);
            set.setFillAfter(true);// 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            imageView.startAnimation(set);
        } catch (Exception e) {

        }
    }
}
