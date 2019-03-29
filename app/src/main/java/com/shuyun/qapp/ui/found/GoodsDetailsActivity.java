package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;


/**
 * 商品详情
 */
public class GoodsDetailsActivity extends BaseActivity implements OnRemotingCallBackListener<Object>, View.OnClickListener {

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
            try {
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
                RichText.from(goodsDeatilsBeans.getDetail()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichText);
                RichText.from(goodsDeatilsBeans.getRemark()).bind(this)
                        .showBorder(false)
                        .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                        .into(tvRichTextBottom);

                //视频播放
                if (!goodsDeatilsBeans.getVideos().isEmpty()) {
                    llAddVideo.removeAllViews();
                    for (int i = 0; i < goodsDeatilsBeans.getVideos().size(); i++) {
                        View view = View.inflate(mContext, R.layout.item_video_player, null);
                        NiceVideoPlayer niceVideoPlayer = view.findViewById(R.id.mNiceVideoPlayer);
                        niceVideoPlayer.setUp(goodsDeatilsBeans.getVideos().get(i), null);
                        TxVideoPlayerController controller = new TxVideoPlayerController(this);
                        controller.setTitle("");
                        Glide.with(this)
                                .load(goodsDeatilsBeans.getPictures().get(0))
                                .crossFade()
                                .into(controller.imageView());
                        niceVideoPlayer.setController(controller);
                        llAddVideo.addView(view);
                    }
                }

            } catch (Exception e) {


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
                showExchangePop(goodsDeatilsBeans);
                break;
            default:
                break;
        }
    }


    /**
     * 立即兑换弹窗
     */
    private int count = 1;
    private List<String> mVals = new ArrayList<>();

    public void showExchangePop(final GoodsDeatilsBeans goodsDeatilsBeans) {
        View upView = LayoutInflater.from(mContext).inflate(R.layout.goods_exchange_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.goods_exchange_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.popwin_anim_style_bottom)
                .setOutsideTouchable(false)
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.goods_exchange_popupwindow:
                                RelativeLayout rl_close = view.findViewById(R.id.rl_close);
                                rl_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        count = 1;
                                        popupWindow.dismiss();
                                    }
                                });
                                mVals.clear();
                                for (int i = 0; i < goodsDeatilsBeans.getStandards().get(0).getStas().size(); i++) {
                                    mVals.add(goodsDeatilsBeans.getStandards().get(0).getStas().get(i).getLabel());
                                }

                                final TagFlowLayout tagFlowLayout = view.findViewById(R.id.id_flowlayout);
                                tagFlowLayout.setMaxSelectCount(1);
                                tagFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
                                    @Override
                                    public View getView(FlowLayout parent, int position, String s) {
                                        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv,
                                                tagFlowLayout, false);
                                        tv.setText(s);
                                        return tv;
                                    }
                                });
                                tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                    @Override
                                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                                        Toast.makeText(mContext, mVals.get(position), Toast.LENGTH_SHORT).show();
                                        return true;
                                    }
                                });

                                RoundImageView iv_picture = view.findViewById(R.id.iv_picture);
                                TextView tv_score = view.findViewById(R.id.tv_score);
                                TextView tv_exchange = view.findViewById(R.id.tv_exchange);
                                TextView tv_one = view.findViewById(R.id.tv_one);
                                TextView tv_two = view.findViewById(R.id.tv_two);
                                tv_one.setText(goodsDeatilsBeans.getStandards().get(0).getLabel());
                                tv_two.setText(goodsDeatilsBeans.getStandards().get(1).getLabel());

                                GlideUtils.LoadImage1(mContext, goodsDeatilsBeans.getPicture(), iv_picture);
                                tv_score.setText(goodsDeatilsBeans.getPrice());
                                NumberAddSubView numberAddSubView = view.findViewById(R.id.nb_addsub_view);
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
                                tv_exchange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(mContext, "" + count, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();

        popupWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放富文本内存
        RichText.clear(this);
        RichText.recycle();
    }
}
