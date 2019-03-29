package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.view.ViewPagerScroller;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.ArrayList;
import java.util.List;

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

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        rlBack.setOnClickListener(this);


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
            GoodsDeatilsBeans goodsDeatilsBeans = (GoodsDeatilsBeans) response.getDat();
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
                tvRichText.setText(Html.fromHtml(goodsDeatilsBeans.getDetail()));
                tvRichTextBottom.setText(Html.fromHtml(goodsDeatilsBeans.getRemark()));

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
            default:
                break;
        }
    }
}
