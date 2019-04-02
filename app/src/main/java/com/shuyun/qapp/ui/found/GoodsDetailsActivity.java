package com.shuyun.qapp.ui.found;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GoodsDeatilsBeans;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
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

    GoodsDeatilsBeans goodsDeatilsBeans;

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();

    private static CommonPopupWindow popupWindow;

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

        //获取商品详情
        getGoodsInfo(getIntent().getStringExtra("id"));

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
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        if ("getGoodsInfo".equals(action)) {
            goodsDeatilsBeans = (GoodsDeatilsBeans) response.getDat();
            //设置轮播图
            bannerList.clear();
            if (!goodsDeatilsBeans.getPictures().isEmpty()) {
                for (int i = 0; i < goodsDeatilsBeans.getPictures().size(); i++) {
                    String pictureList = goodsDeatilsBeans.getPictures().get(i);
                    MarkBannerItem1 item = new MarkBannerItem1(pictureList);
                    bannerList.add(item);
                }
                markBannerAdapter1.clearViews();
                markBannerAdapter1.setData(mContext, bannerList);
                //重新生成单位条
                mBannerView.setBannerAdapter(markBannerAdapter1);
            }

            tvName.setText(goodsDeatilsBeans.getName());
            tvContent.setText(goodsDeatilsBeans.getPurpose());
            tvScore.setText(goodsDeatilsBeans.getBp().toString());
            //图文详情
            if (!EncodeAndStringTool.isStringEmpty(goodsDeatilsBeans.getDetail())) {
                RichText.from(goodsDeatilsBeans.getDetail()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichText);
//                    HtmlFromUtils.setTextFromHtml(this, tvRichText, goodsDeatilsBeans.getDetail());
            }
            //底部文字
            if (!EncodeAndStringTool.isStringEmpty(goodsDeatilsBeans.getRemark())) {
                RichText.from(goodsDeatilsBeans.getRemark()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichTextBottom);
//                    HtmlFromUtils.setTextFromHtml(this, tvRichTextBottom, goodsDeatilsBeans.getRemark());
            }

            //视频播放
            if (!goodsDeatilsBeans.getVideos().isEmpty()) {
                llAddVideo.removeAllViews();
                for (int i = 0; i < goodsDeatilsBeans.getVideos().size(); i++) {
                    GoodsDeatilsBeans.VideosBean videosBean = goodsDeatilsBeans.getVideos().get(i);
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

        }
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
    private String standard = "";
    private String color = "";
    private List<String> standList = new ArrayList<>();
    private List<String> colorList = new ArrayList<>();

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
                final TagFlowLayout standardTagFlowLayout = view.findViewById(R.id.standard_flowlayout);
                final TagFlowLayout colorTagFlowLayout = view.findViewById(R.id.color_flowlayout);
                NumberAddSubView numberAddSubView = view.findViewById(R.id.nb_addsub_view);
                RelativeLayout rl_close = view.findViewById(R.id.rl_close);
                TextView tv_score = view.findViewById(R.id.tv_score);
                TextView tv_exchange = view.findViewById(R.id.tv_exchange);
                TextView tv_one = view.findViewById(R.id.tv_one);
                TextView tv_two = view.findViewById(R.id.tv_two);

                //主图
                GlideUtils.LoadImage1(mContext, goodsDeatilsBeans.getPicture(), iv_picture);
                //积分
                tv_score.setText(goodsDeatilsBeans.getPrice());
                tv_one.setText(goodsDeatilsBeans.getStandards().get(0).getLabel());
                tv_two.setText(goodsDeatilsBeans.getStandards().get(1).getLabel());

                //关闭弹框
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count = 1;
                        standard = "";
                        color = "";
                        popupWindow.dismiss();
                    }
                });

                //规格数据
                standList.clear();
                final List<GoodsDeatilsBeans.StandardsBean.StasBean> stasBeanList = goodsDeatilsBeans.getStandards().get(0).getStas();
                for (int i = 0; i < stasBeanList.size(); i++) {
                    standList.add(stasBeanList.get(i).getLabel());
                }
                standardTagFlowLayout.setMaxSelectCount(1);
                standardTagFlowLayout.setAdapter(new TagAdapter<String>(standList) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv, standardTagFlowLayout, false);
                        tv.setText(s);
                        return tv;
                    }
                });
                standardTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        String value = "";
                        for (int i = 0; i < stasBeanList.size(); i++) {
                            if (position == i) {
                                value = stasBeanList.get(i).getValue();
                            }
                        }
                        if (standard.equals(value)) {
                            standard = "";
                        } else {
                            standard = value;
                        }
                        return true;
                    }
                });

                //颜色数据
                colorList.clear();
                final List<GoodsDeatilsBeans.StandardsBean.StasBean> stasBeanList1 = goodsDeatilsBeans.getStandards().get(1).getStas();
                for (int i = 0; i < stasBeanList1.size(); i++) {
                    colorList.add(stasBeanList1.get(i).getLabel() + "," + stasBeanList1.get(i).getColor());
                }
                colorTagFlowLayout.setMaxSelectCount(1);
                colorTagFlowLayout.setAdapter(new TagAdapter<String>(colorList) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        String[] strings = s.split(",");
                        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_layout_tv_color, colorTagFlowLayout, false);
                        Drawable drawable = getResources().getDrawable(R.mipmap.color_logo_iv).mutate();
                        drawable.setBounds(0, 0, ConvertUtils.dp2px(20), ConvertUtils.dp2px(20));//第一0是距左边距离，第二0是距上边距离，20分别是宽高
                        drawable.setColorFilter(Color.parseColor(strings[1]), PorterDuff.Mode.SRC_ATOP);
                        tv.setCompoundDrawables(drawable, null, null, null);//只放左边
                        tv.setText(strings[0]);
                        return tv;
                    }
                });
                colorTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        String[] strings = colorList.get(position).split(",");
                        String value = "";
                        for (int i = 0; i < stasBeanList1.size(); i++) {
                            if (strings[0].equals(stasBeanList1.get(i).getLabel())) {
                                value = stasBeanList1.get(i).getValue();
                            }
                        }
                        if (color.equals(value)) {
                            color = "";
                        } else {
                            color = value;
                        }
                        return true;
                    }
                });

                //数量
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
                        Toast.makeText(mContext, "您选择了" + "规格： " + standard + "  颜色： " + color + "  数量： " + count, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            default:
                break;
        }
    }
}
