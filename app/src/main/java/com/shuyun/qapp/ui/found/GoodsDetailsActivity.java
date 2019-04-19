package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPObject;
import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GoodsCommitBean;
import com.shuyun.qapp.bean.GoodsDetailBeans;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.bean.ScoreExchangeResult;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.classify.ClassifyActivity;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.NumberAddSubView;
import com.shuyun.qapp.view.RoundImageView;
import com.shuyun.qapp.view.ViewPagerScroller;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;


/**
 * 商品详情
 */
public class GoodsDetailsActivity extends BaseActivity implements OnRemotingCallBackListener<Object>, View.OnClickListener, CommonPopupWindow.ViewInterface {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.banner)
    BannerViewPager mBannerView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_rich_text)
    TextView tvRichText;
    @BindView(R.id.tv_rich_text_bottom)
    TextView tvRichTextBottom;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.ll_add_video)
    LinearLayout llAddVideo;

    Context mContext;

    GoodsDetailBeans goodsDetailBeans;

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<View> stasViewList = new ArrayList<>();

    private static CommonPopupWindow popupWindow;

    String goods_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        //初始化缓存
        RichText.initCacheDir(this);

        rlBack.setOnClickListener(this);
        tvExchange.setOnClickListener(this);


        //初始化
        init();

        goods_id = getIntent().getStringExtra("id");

        //获取商品详情
        getGoodsInfo(goods_id);

    }

    private void init() {
        MarkBannerItem1 i = new MarkBannerItem1("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        bannerList.add(i);
        //设置轮播图
        markBannerAdapter1 = new MarkBannerAdapter1(new GlideImageLoader(), this);
        markBannerAdapter1.setData(mContext, bannerList);
        mBannerView.setBannerAdapter(markBannerAdapter1);

        final ViewPager mViewpager = (ViewPager) mBannerView.getChildAt(0);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScroller = new ViewPagerScroller(mContext);
        pagerScroller.setScrollDuration(400);
        pagerScroller.initViewPagerScroll(mViewpager);
    }

    //获取商品信息
    private void getGoodsInfo(String id) {
        RemotingEx.doRequest("getGoodsInfo", ApiServiceBean.getGoodsInfo(), new Object[]{id}, this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_goods_details;
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
            String err = response.getErr();
            if ("L0001".equals(err)) {
                ExchangeTipDialog("抱歉您的积分不足", "答题赚积分", "确认", "", "");
            } else {
                ErrorCodeTools.errorCodePrompt(mContext, err, response.getMsg());
            }
            return;
        }
        if ("getGoodsInfo".equals(action)) {
            goodsDetailBeans = (GoodsDetailBeans) response.getDat();
            //设置轮播图
            bannerList.clear();
            if (!goodsDetailBeans.getPictures().isEmpty()) {
                for (int i = 0; i < goodsDetailBeans.getPictures().size(); i++) {
                    String pictureList = goodsDetailBeans.getPictures().get(i);
                    MarkBannerItem1 item = new MarkBannerItem1(pictureList);
                    bannerList.add(item);
                }
                markBannerAdapter1.clearViews();
                markBannerAdapter1.setData(mContext, bannerList);
                //重新生成单位条
                mBannerView.setBannerAdapter(markBannerAdapter1);
            }

            tvName.setText(goodsDetailBeans.getName());
            tvContent.setText(goodsDetailBeans.getPurpose());
            tvScore.setText(goodsDetailBeans.getBp().toString());
            //图文详情
            if (!EncodeAndStringTool.isStringEmpty(goodsDetailBeans.getDetail())) {
                RichText.from(goodsDetailBeans.getDetail()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichText);
            }
            //底部文字
            if (!EncodeAndStringTool.isStringEmpty(goodsDetailBeans.getRemark())) {
                RichText.from(goodsDetailBeans.getRemark()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichTextBottom);
            }

            //视频播放
            if (!goodsDetailBeans.getVideos().isEmpty()) {
                llAddVideo.removeAllViews();
                for (int i = 0; i < goodsDetailBeans.getVideos().size(); i++) {
                    GoodsDetailBeans.VideosBean videosBean = goodsDetailBeans.getVideos().get(i);
                    View view = View.inflate(mContext, R.layout.item_video_player, null);
                    NiceVideoPlayer niceVideoPlayer = view.findViewById(R.id.mNiceVideoPlayer);
                    niceVideoPlayer.setUp(videosBean.getValue(), null);
                    TxVideoPlayerController controller = new TxVideoPlayerController(this);
                    if (!EncodeAndStringTool.isStringEmpty(videosBean.getTitle())) {
                        controller.setTitle(videosBean.getTitle());
                    }
                    Glide.with(this)
                            .load(videosBean.getPicture())
                            .crossFade()
                            .into(controller.imageView());
                    niceVideoPlayer.setController(controller);
                    llAddVideo.addView(view);
                }
            }

        } else if ("goodsExchange".equals(action)) {
            popupWindow.dismiss();
            //兑换商品成功
            ScoreExchangeResult scoreExchangeResult = (ScoreExchangeResult) response.getDat();
            showSuccessPop(scoreExchangeResult);
        }
    }

    /**
     * 兑换道具成功popupWindow
     */
    public void showSuccessPop(final ScoreExchangeResult scoreExchangeResult) {
        View upView = LayoutInflater.from(this).inflate(R.layout.exchange_prop_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.exchange_prop_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.exchange_prop_popupwindow:
                                TextView tvcontent = view.findViewById(R.id.tv_content);
                                ImageView ivLogo = view.findViewById(R.id.iv_logo);
                                ImageView image_close = view.findViewById(R.id.iv_close);
                                TextView tv_look_detail = view.findViewById(R.id.tv_look_detail);
                                tv_look_detail.setVisibility(View.VISIBLE);
                                tv_look_detail.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        Intent intent = new Intent(mContext, MinePrizeActivity.class);
                                        intent.putExtra("status", 1);
                                        startActivity(intent);
                                    }
                                });
                                tvcontent.setText(scoreExchangeResult.getTitle());
                                GlideUtils.LoadImage1(mContext, scoreExchangeResult.getPicture(), ivLogo);
                                image_close.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();

        popupWindow.showAtLocation(rlMain, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_exchange:
                //立即兑换
                showExchangePop();
                break;
            default:
                break;
        }
    }


    /**
     * 立即兑换弹窗
     */
    private int count = 1;

    public void showExchangePop() {
        View upView = LayoutInflater.from(mContext).inflate(R.layout.goods_exchange_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int height = (dm.heightPixels / 10) * 7;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.goods_exchange_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, height)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.popwin_anim_style_bottom)
                .setOutsideTouchable(false)
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放富文本内存
        RichText.clear(this);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.goods_exchange_popupwindow:
                RoundImageView iv_picture = view.findViewById(R.id.iv_picture);
                LinearLayout ll_flowlayout = view.findViewById(R.id.ll_flowlayout);
                NumberAddSubView numberAddSubView = view.findViewById(R.id.nb_addsub_view);
                RelativeLayout rl_close = view.findViewById(R.id.rl_close);
                TextView tv_score = view.findViewById(R.id.tv_score);
                TextView tv_exchange = view.findViewById(R.id.tv_exchange);

                //主图
                GlideUtils.LoadImage1(mContext, goodsDetailBeans.getPicture(), iv_picture);

                //积分
                tv_score.setText(goodsDetailBeans.getPrice());

                //关闭弹框
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 1;
                        popupWindow.dismiss();
                    }
                });

                //动态添加规格
                stasViewList.clear();
                if (!EncodeAndStringTool.isListEmpty(goodsDetailBeans.getStandards())) {
                    List<GoodsDetailBeans.StandardsBean> standardsBeanList = goodsDetailBeans.getStandards();
                    for (int i = 0; i < standardsBeanList.size(); i++) {
                        GoodsDetailBeans.StandardsBean standardsBean = standardsBeanList.get(i);
                        String label = standardsBean.getLabel();
                        final List<GoodsDetailBeans.StandardsBean.StasBean> stasBeanList = standardsBean.getStas();
                        if (!EncodeAndStringTool.isListEmpty(stasBeanList)) {
                            View viewFlow = View.inflate(mContext, R.layout.item_add_tag_flow_layout, null);
                            final TextView textView = viewFlow.findViewById(R.id.tv_label);
                            final TagFlowLayout tagFlowLayout = viewFlow.findViewById(R.id.flowlayout);
                            textView.setText(label);
                            tagFlowLayout.setMaxSelectCount(1);
                            tagFlowLayout.setTag(standardsBean.getParam());
                            tagFlowLayout.setAdapter(new TagAdapter<GoodsDetailBeans.StandardsBean.StasBean>(stasBeanList) {
                                @Override
                                public View getView(FlowLayout parent, int position, GoodsDetailBeans.StandardsBean.StasBean bean) {
                                    View view = LayoutInflater.from(mContext).inflate(R.layout.stas_layout, tagFlowLayout, false);
                                    TextView color = view.findViewById(R.id.color);
                                    TextView label = view.findViewById(R.id.label);
                                    //设置label
                                    label.setText(bean.getLabel());
                                    //设置颜色
                                    if (!EncodeAndStringTool.isStringEmpty(bean.getColor())) {
                                        color.setVisibility(View.VISIBLE);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.color_logo_iv).mutate();
                                        drawable.setBounds(0, 0, ConvertUtils.dp2px(20), ConvertUtils.dp2px(20));
                                        drawable.setColorFilter(Color.parseColor(bean.getColor()), PorterDuff.Mode.SRC_ATOP);
                                        color.setCompoundDrawables(drawable, null, null, null);
                                    }
                                    return view;
                                }
                            });
                            ll_flowlayout.addView(viewFlow);
                            stasViewList.add(viewFlow);
                        }
                    }
                }
                //数量
                numberAddSubView.setMaxValue(goodsDetailBeans.getInventory().intValue());
                numberAddSubView.setOnButtonClickListenter(new NumberAddSubView.OnButtonClickListenter() {
                    @Override
                    public void onButtonAddClick(View view, int value) {
                        count = value;
                    }

                    @Override
                    public void onButtonSubClick(View view, int value) {
                        count = value;
                    }
                });
                //立即兑换
                tv_exchange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GoodsCommitBean goodsCommitBean = new GoodsCommitBean();
                        List<GoodsCommitBean.StandardsBean> standardsBeanList = new ArrayList<>();

                        for (int i = 0; i < stasViewList.size(); i++) {
                            View viewFlow = stasViewList.get(i);
                            TextView textView = viewFlow.findViewById(R.id.tv_label);
                            TagFlowLayout layout = viewFlow.findViewById(R.id.flowlayout);

                            Set<Integer> sets = layout.getSelectedList();
                            if (sets == null || sets.isEmpty()) {
                                ToastUtil.showToast(mContext, "请选择" + textView.getText());
                                return;
                            }
                            int index = (Integer) sets.toArray()[0];
                            GoodsDetailBeans.StandardsBean.StasBean bean = (GoodsDetailBeans.StandardsBean.StasBean) layout.getAdapter().getItem(index);
                            GoodsCommitBean.StandardsBean standardsBean = new GoodsCommitBean.StandardsBean();
                            standardsBean.setParam((String) layout.getTag());
                            standardsBean.setValue(bean.getValue());
                            standardsBeanList.add(standardsBean);
                        }

                        if (!EncodeAndStringTool.isListEmpty(standardsBeanList)) {
                            goodsCommitBean.setStandards(standardsBeanList);
                        }

                        goodsCommitBean.setCount((long) count);
                        String detail = JSON.toJSONString(goodsCommitBean);

                        //兑换弹框
                        Long bp = goodsDetailBeans.getBp() * count;
                        ExchangeTipDialog("您将消耗" + bp + "积分兑换", "确认兑换", "我点错了", goods_id, detail);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 商品兑换
     */
    private void propExchange(String goodsId, String detail) {
        RemotingEx.doRequest("goodsExchange", ApiServiceBean.scoreExchangeApplyGoods(), new Object[]{goodsId, detail}, this);
    }

    //兑换弹框
    private void ExchangeTipDialog(String title, final String tv_right, String tv_left,
                                   final String goodsId, final String detail) {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText(title)
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative(tv_left, null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#9B9B9B");
                    }
                })
                .setPositive(tv_right, new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (tv_right.equals("答题赚积分")) {
                            //分类页
                            SaveUserInfo.getInstance(mContext).setUserInfo("show_back", "show_back");
                            mContext.startActivity(new Intent(mContext, ClassifyActivity.class));
                        } else {
                            propExchange(goodsId, detail);
                        }

                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }
}
