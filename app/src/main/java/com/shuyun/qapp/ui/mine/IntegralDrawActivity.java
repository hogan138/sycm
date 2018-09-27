package com.shuyun.qapp.ui.mine;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyGridViewAdapter;
import com.shuyun.qapp.adapter.MyViewPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AnnounceBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.IntegralExchangeBean;
import com.shuyun.qapp.bean.IntegralPrizeBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.ProductListBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.adapter.BxPrizeAdapter;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.webview.WebPrizeActivity;
import com.shuyun.qapp.ui.webview.WebRulesActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.TimeTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.ViewPagerSlide;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 积分抽奖
 */
public class IntegralDrawActivity extends BaseActivity implements CommonPopupWindow.ViewInterface, ViewPager.OnPageChangeListener {//
    //TODO 积分抽奖点击再开一次的时候跳到 打开宝箱页面
    @BindView(R.id.tv_superise)
    TextView tvSuperise;//恭喜您获得宝箱
    @BindView(R.id.tv_accuracy1)
    TextView tvAccuracy1;//正确率达到了<font color='#ffd149'>80%</font>,超过了12666名用户

    @BindView(R.id.ll_shared_prize)
    LinearLayout llSharedPrize;

    @BindView(R.id.ll_look_answer_his)
    LinearLayout llLookAnswerHis;

    //答题业务:答题抽奖要隐藏的放上面
    //我的业务:积分抽奖要显示的放下面
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回按钮
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.tv_des)
    TextView tvDes;//开宝箱消耗100积分
    @BindView(R.id.ll_integral_prize)
    LinearLayout llIntegralPrize;//积分数量父控件
    @BindView(R.id.tv_jifen_balance)
    TextView tvJiFenBalance;//积分数量
    @BindView(R.id.iv_treasure_box)
    ImageView ivTreasureBox;//宝箱图片
    @BindView(R.id.tv_congratulation)
    TextView tvCongratulation;//恭喜你获得以下奖品
    @BindView(R.id.tv_hint1)
    TextView tvHint1;//提示语
    @BindView(R.id.rv_prize1)
    RecyclerView rvPrize1;//奖品列表
    @BindView(R.id.btn_open_treasure_box)
    Button btnOpenTreasureBox;//开宝箱
    @BindView(R.id.rl_prize_bg)
    RelativeLayout rlPrizeBg;//奖品动画背景
    @BindView(R.id.rl_prize)
    RelativeLayout rlPrize;//奖品动画
    @BindView(R.id.iv_prize_pic)
    OvalImageView ivPrizePic;//开奖图片
    @BindView(R.id.tv_title_name1)
    TextView tvTitleName1;//开奖标题
    @BindView(R.id.tv_prize_content)
    TextView tvPrizeContent;//开奖内容
    @BindView(R.id.tv_prize_date)
    TextView tvPrizeDate;//奖品使用日期
    @BindView(R.id.rl_integral_draw)
    RelativeLayout rlIntegralDraw;//开宝箱界面
    @BindView(R.id.rl_grideview_prize)
    RelativeLayout rlGrideviewPrize;//奖品轮播
    @BindView(R.id.viewPager)
    ViewPagerSlide viewPager;//奖品轮播图
    @BindView(R.id.points)
    ViewGroup points;//初始化小圆点指示器
    @BindView(R.id.tv_look_rules)
    TextView tvLookRules;//开宝箱  新增查看规则
    private static final String TAG = "IntegralDrawActivity";


    private AnnounceBean announceBean;//积分抽奖  消耗积分数
    private int status;//状态为0是积分抽奖,状态为1是宝箱抽奖
    private String prizeId;//奖品的id  仅对于宝箱抽奖有用
    private SharedBean sharedBean;
    private MinePrize minePrize1;//我的奖品

    //奖品grideview
    private ImageView[] ivPoints;//小圆点图片集合
    private int totalPage;//总的页数
    private int mPageSize = 6;//每页显示的最大数量
    private List<ProductListBean> listDatas = new ArrayList<>();//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("全民共进");
        initData();
        initView();

        Log.e("token", AppConst.TOKEN);
        Log.e("sycm", AppConst.sycm());

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_integral_draw;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        prizeId = intent.getStringExtra("prize_id");//奖品id
        /**
         * 状态0:代表积分抽奖
         * 1:代表我的奖品开宝箱
         */
        status = intent.getIntExtra("status", 0);//状态
        minePrize1 = intent.getParcelableExtra("minePrize");//我的奖品
        if (status == 0) {//积分抽奖
            tvJiFenBalance.setText(SaveUserInfo.getInstance(IntegralDrawActivity.this).getUserInfo("my_bp"));
            loadGetIntegralDrawInfo();
        } else if (status == 1) {//宝箱开奖
            tvDes.setVisibility(View.GONE);
            rlGrideviewPrize.setVisibility(View.VISIBLE);
            showpopu();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvSuperise.setVisibility(View.GONE);
        tvAccuracy1.setVisibility(View.GONE);
        llSharedPrize.setVisibility(View.GONE);
        llLookAnswerHis.setVisibility(View.GONE);
        //以上是答题有奖显示的控件
        //
        tvCongratulation.setVisibility(View.GONE);
        tvHint1.setVisibility(View.GONE);
        rvPrize1.setVisibility(View.GONE);
        rlPrizeBg.setVisibility(View.GONE);
        if (status == 1) {
            llIntegralPrize.setVisibility(View.GONE);
            /**
             * 奖品原始来源
             * 0——容器奖品；
             * 1——答题；
             * 2——分享；
             * 3——抽奖；
             * 4——扩展规则
             */
            if (minePrize1.getOrginal() == 1) {
                tvAccuracy1.setVisibility(View.VISIBLE);
                String msg = "正确率达到了<font color='#ffd149'>" + minePrize1.getAccuracy() + "%</font>,超过了" + minePrize1.getBeatRate() + "%的用户";
                tvAccuracy1.setText(Html.fromHtml(msg));
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //改变小圆圈指示器的切换效果
        setImageBackground(position);
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 改变点点点的切换效果
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.drawable.banner_x);
            } else {
                ivPoints[i].setBackgroundResource(R.drawable.banner_w);
            }
        }
    }


    private void showpopu() {
        //奖品图片
        for (int i = 0; i < minePrize1.getPrizes().size(); i++) {
            ProductListBean prizesBean = new ProductListBean();
            prizesBean.setName(minePrize1.getPrizes().get(i).getName());
            prizesBean.setMainImage(minePrize1.getPrizes().get(i).getMainImage());
            prizesBean.setLongImage(minePrize1.getPrizes().get(i).getLongImage());
            prizesBean.setPurpose(minePrize1.getPrizes().get(i).getPurpose());
            listDatas.add(prizesBean);
        }
        LayoutInflater inflater = LayoutInflater.from(IntegralDrawActivity.this);
        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, viewPager, false);
            gridView.setAdapter(new MyGridViewAdapter(IntegralDrawActivity.this, listDatas, i, mPageSize, rlIntegralDraw));
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        //小圆点指示器
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < ivPoints.length; i++) {
            ImageView imageView = new ImageView(IntegralDrawActivity.this);
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.banner_x);
            } else {
                imageView.setBackgroundResource(R.drawable.banner_w);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            points.addView(imageView, layoutParams);
        }
        //设置ViewPager滑动监听
        viewPager.addOnPageChangeListener(IntegralDrawActivity.this);
    }


    /**
     * 获取积分抽奖活动信息
     */
    private void loadGetIntegralDrawInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getIntegralDrawInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AnnounceBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AnnounceBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            announceBean = listDataResponse.getDat();
                            Log.e("积分抽奖返回", listDataResponse.toString());
                            if (!EncodeAndStringTool.isObjectEmpty(announceBean)) {
                                tvDes.setText("打开宝箱将消耗" + announceBean.getBp() + "积分");
                                rlGrideviewPrize.setVisibility(View.VISIBLE);
                                if (!EncodeAndStringTool.isListEmpty(announceBean.getPrizes())) {
                                    //奖品图片
                                    for (int i = 0; i < announceBean.getPrizes().size(); i++) {
                                        ProductListBean prizesBean = new ProductListBean();
                                        prizesBean.setName(announceBean.getPrizes().get(i).getName());
                                        prizesBean.setMainImage(announceBean.getPrizes().get(i).getMainImage());
                                        prizesBean.setLongImage(announceBean.getPrizes().get(i).getLongImage());
                                        prizesBean.setPurpose(announceBean.getPrizes().get(i).getPurpose());
                                        listDatas.add(prizesBean);
                                    }
                                    LayoutInflater inflater = LayoutInflater.from(IntegralDrawActivity.this);
                                    //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
                                    totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
                                    viewPagerList = new ArrayList<>();
                                    for (int i = 0; i < totalPage; i++) {
                                        //每个页面都是inflate出一个新实例
                                        GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, viewPager, false);
                                        gridView.setAdapter(new MyGridViewAdapter(IntegralDrawActivity.this, listDatas, i, mPageSize, rlIntegralDraw));
                                        //每一个GridView作为一个View对象添加到ViewPager集合中
                                        viewPagerList.add(gridView);
                                    }
                                    //设置ViewPager适配器
                                    viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
                                    //小圆点指示器
                                    ivPoints = new ImageView[totalPage];
                                    for (int i = 0; i < ivPoints.length; i++) {
                                        ImageView imageView = new ImageView(IntegralDrawActivity.this);
                                        //设置图片的宽高
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
                                        if (i == 0) {
                                            imageView.setBackgroundResource(R.drawable.banner_x);
                                        } else {
                                            imageView.setBackgroundResource(R.drawable.banner_w);
                                        }
                                        ivPoints[i] = imageView;
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        layoutParams.leftMargin = 20;//设置点点点view的左边距
                                        layoutParams.rightMargin = 20;//设置点点点view的右边距
                                        points.addView(imageView, layoutParams);
                                    }
                                    //设置ViewPager滑动监听
                                    viewPager.addOnPageChangeListener(IntegralDrawActivity.this);
                                }
                            } else {
                                ToastUtil.showToast(IntegralDrawActivity.this, "获取积分抽奖活动信息返回数据为空");
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(IntegralDrawActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @OnClick({R.id.iv_back, R.id.tv_look_rules, R.id.btn_open_treasure_box, R.id.iv_treasure_box})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //返回到首页
                if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                } else {
                    finish();
                }
                break;
            case R.id.tv_look_rules://查看规则
                Intent intent = new Intent(this, WebRulesActivity.class);
                if (status == 0) {//积分抽奖
                    if (!EncodeAndStringTool.isObjectEmpty(announceBean) && !EncodeAndStringTool.isStringEmpty(announceBean.getBulletin())) {
                        intent.putExtra("bulletin", announceBean.getBulletin());
                        startActivity(intent);
                    } else {
                        ToastUtil.showToast(this, "该奖品暂时没有配活动规则");
                    }
                } else if (status == 1) {//宝箱开奖
                    if (!EncodeAndStringTool.isObjectEmpty(minePrize1) && !EncodeAndStringTool.isStringEmpty(minePrize1.getBulletin())) {
                        intent.putExtra("bulletin", minePrize1.getBulletin());
                        startActivity(intent);
                    } else {
                        ToastUtil.showToast(this, "该奖品暂时没有配活动规则");
                    }
                }

                break;
            case R.id.btn_open_treasure_box:
                //获取到状态,如果是(0)积分抽奖调用积分抽奖的业务,如果是(1)开宝箱,调用开宝箱的业务;
                if (0 == status) {
                    //调用积分抽奖的业务
                    if (Long.parseLong(tvJiFenBalance.getText().toString()) >= announceBean.getBp()) {
                        if (btnOpenTreasureBox.getText().equals("打开宝箱")) {
                            rlGrideviewPrize.setVisibility(View.GONE);
                            tvCongratulation.setVisibility(View.VISIBLE);
                            tvHint1.setVisibility(View.VISIBLE);
                            rvPrize1.setVisibility(View.VISIBLE);
                            btnOpenTreasureBox.setText("再开一次");
                            loadPrize();
                            //设置按钮不能重复点击
                            btnOpenTreasureBox.setEnabled(false);
                        } else if (btnOpenTreasureBox.getText().equals("再开一次")) {
                            ivTreasureBox.setImageResource(R.mipmap.bxclose);
                            tvHint1.setVisibility(View.GONE);
                            rvPrize1.setVisibility(View.GONE);
                            tvCongratulation.setVisibility(View.GONE);
                            rlGrideviewPrize.setVisibility(View.VISIBLE);
                            btnOpenTreasureBox.setText("打开宝箱");
                        }

                    } else {
                        ToastUtil.showToast(IntegralDrawActivity.this, "您的积分余额不足,请前往答题获取更多积分。");
                    }
                } else if (1 == status) {//我的奖品开宝箱
                    tvDes.setVisibility(View.GONE);
                    llIntegralPrize.setVisibility(View.GONE);//隐藏积分数量
                    rlGrideviewPrize.setVisibility(View.GONE);
                    tvCongratulation.setVisibility(View.VISIBLE);
                    tvHint1.setVisibility(View.VISIBLE);
                    rvPrize1.setVisibility(View.VISIBLE);
                    btnOpenTreasureBox.setVisibility(View.GONE);
                    ivTreasureBox.setImageResource(R.mipmap.bxopen);
                    ivTreasureBox.setEnabled(false);
                    //调用开宝箱的业务奖品类型是101,传一个String类型奖品的id;
                    openPrize(prizeId);
                    //设置按钮不能重复点击
                    btnOpenTreasureBox.setEnabled(false);
                }
                break;
            case R.id.iv_treasure_box:
                if (0 == status) {//积分抽奖
                    //调用积分抽奖的业务
                    if (Long.parseLong(tvJiFenBalance.getText().toString()) >= announceBean.getBp()) {
                        if (btnOpenTreasureBox.getText().equals("打开宝箱")) {
                            rlGrideviewPrize.setVisibility(View.GONE);
                            tvCongratulation.setVisibility(View.VISIBLE);
                            tvHint1.setVisibility(View.VISIBLE);
                            rvPrize1.setVisibility(View.VISIBLE);
                            btnOpenTreasureBox.setText("再开一次");
                            loadPrize();
                            //设置按钮不能重复点击
                            ivTreasureBox.setEnabled(false);
                        } else if (btnOpenTreasureBox.getText().equals("再开一次")) {
                            ivTreasureBox.setImageResource(R.mipmap.bxclose);
                            tvHint1.setVisibility(View.GONE);
                            rvPrize1.setVisibility(View.GONE);
                            tvCongratulation.setVisibility(View.GONE);
                            rlGrideviewPrize.setVisibility(View.VISIBLE);
                            btnOpenTreasureBox.setText("打开宝箱");
                        }
                    } else {
                        ToastUtil.showToast(IntegralDrawActivity.this, "您的积分余额不足,请前往答题获取更多积分。");
                    }
                } else if (1 == status) {//宝箱抽奖
                    tvDes.setVisibility(View.GONE);
                    llIntegralPrize.setVisibility(View.GONE);//隐藏积分数量
                    rlGrideviewPrize.setVisibility(View.GONE);
                    tvCongratulation.setVisibility(View.VISIBLE);
                    tvHint1.setVisibility(View.VISIBLE);
                    rvPrize1.setVisibility(View.VISIBLE);
                    btnOpenTreasureBox.setVisibility(View.GONE);
                    ivTreasureBox.setImageResource(R.mipmap.bxopen);
                    ivTreasureBox.setEnabled(false);
                    //调用开宝箱的业务奖品类型是101,传一个String类型奖品的id;
                    openPrize(prizeId);
                    //设置按钮不能重复点击
                    ivTreasureBox.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 积分抽奖
     */
    private void loadPrize() {
        String deviceId = SmAntiFraud.getDeviceId();
        ApiService apiService = BasePresenter.create(8000);
        apiService.integralDraw(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<IntegralPrizeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<IntegralPrizeBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            rvPrize1.setVisibility(View.VISIBLE);

                            List<MinePrize> minePrizeList = dataResponse.getDat().getPrize();
                            try {
                                if (minePrizeList.size() > 0) {
                                    openPrize(minePrizeList.get(0).getId());
                                }
                            } catch (Exception e) {

                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(IntegralDrawActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 打开宝箱或者打开奖品
     *
     * @param s 奖品类型是101宝箱,s代表奖品id
     */
    private void openPrize(String s) {
        OkHttpClient client = new OkHttpClient.Builder()
                //设置读取数据的时间
                .readTimeout(5, TimeUnit.SECONDS)
                //对象的创建
                .build();
        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url(AppConst.BASE_URL + "/rest/user/lucky/" + s)
                .addHeader("Authorization", AppConst.TOKEN)
                .addHeader("sycm", AppConst.sycm()).build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(TAG, Thread.currentThread().getName() + "结果  " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                //子线程
                //main thread1
                String body = response.body().string();
                Gson gson = new Gson();
                try {
                    JSONObject jresult = new JSONObject(body);
                    String err = jresult.getString("err");
                    if (err.equals("00000")) {
                        JSONArray jarr = jresult.getJSONArray("dat");
                        int size = jarr.length();
                        final List<MinePrize> minePrizes = new ArrayList<>();
                        for (int index = 0; index < size; ++index) {
                            JSONObject jprize = jarr.getJSONObject(index);
                            MinePrize prize = gson.fromJson(jprize.toString(), MinePrize.class);
                            minePrizes.add(prize);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!EncodeAndStringTool.isListEmpty(minePrizes)) {
                                    if (minePrizes.size() > 0) {
                                        openPrizeAnimator(minePrizes);
                                    }
                                }
                            }
                        });
                    } else if (err.equals("U2006")) {
                        JSONObject jr = new JSONObject(body);
                        JSONObject object = jr.getJSONObject("dat");
                        String title = object.getString("title");
                        String content = object.getString("content");
                        long time = object.getLong("time");
                        String url = object.getString("url");
                        String picture = object.getString("picture");
                        String buttonCaption = object.getString("buttonCaption");
                        sharedBean = new SharedBean();
                        sharedBean.setTitle(title);
                        sharedBean.setContent(content);
                        sharedBean.setPicture(picture);
                        sharedBean.setUrl(url);
                        sharedBean.setTime(String.valueOf(time));
                        sharedBean.setButtonCaption(buttonCaption);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showQuZhouPop();
                            }
                        });
                    } else {
                        String msg = jresult.getString("msg");
                        ErrorCodeTools.errorCodePrompt(IntegralDrawActivity.this, err, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 开宝箱动画
     *
     * @param minePrizes
     */
    private void openPrizeAnimator(final List<MinePrize> minePrizes) {
        rlPrizeBg.setVisibility(View.VISIBLE);
        for (int i = 0; i < minePrizes.size(); i++) {
            if (!EncodeAndStringTool.isObjectEmpty(minePrizes.get(i))) {
                final MinePrize minePrize = minePrizes.get(i);
                if (!EncodeAndStringTool.isObjectEmpty(minePrize)) {
                    //开奖图片
                    ImageLoaderManager.LoadImage(IntegralDrawActivity.this, minePrize.getOpenPicture(), ivPrizePic, R.mipmap.zw02);
                    if (minePrize.getType() == 7 || minePrize.getType() == 1) {
                        tvTitleName1.setText(minePrize.getName() + "  " + minePrize.getAmount() + "元");//标题
                    } else if (minePrize.getType() == 2) {
                        String amount = minePrize.getAmount();
                        if (minePrize.getAmount().contains(".")) {
                            amount = minePrize.getAmount().substring(0, minePrize.getAmount().indexOf('.'));
                        }
                        tvTitleName1.setText(minePrize.getName() + "  " + amount);
                    } else {
                        tvTitleName1.setText(minePrize.getName());
                    }

                    //更新积分信息
                    getInfo();

                    tvPrizeContent.setText(minePrize.getDescription());//内容
                    if (!EncodeAndStringTool.isStringEmpty(minePrize.getExpireTime()) && !minePrize.getExpireTime().equals("0")) {
                        tvPrizeDate.setText(Html.fromHtml("奖品的有效期至:<font color='#FBC859'>" + TimeTool.getCommTime(minePrize.getExpireTime(), TimeTool.FORMAT_DATE_TIME_SECOND) + "</font>"));//+ TimeTool.getTime(minePrize.getValidTime()) + "-"
                    }

                    //加载奖品放大动画效果
                    AnimatorSet animatorSet = new AnimatorSet();  //组合动画
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlPrize, "scaleX", 0, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(rlPrize, "scaleY", 0, 1f);
                    animatorSet.setDuration(300);
                    animatorSet.setInterpolator(new DecelerateInterpolator());
                    animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                    ivTreasureBox.setEnabled(false);//宝箱和按钮设置不可点击状态
                    btnOpenTreasureBox.setEnabled(false);
                    animatorSet.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //恢复按钮点击事件
                            btnOpenTreasureBox.setEnabled(true);
                            ivTreasureBox.setEnabled(true);
                        }
                    }, 2000);


                    animatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            //开始动画
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //动画结束
                                    rlPrizeBg.setVisibility(View.GONE);
                                    BxPrizeAdapter adapter = new BxPrizeAdapter(IntegralDrawActivity.this, minePrizes);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(IntegralDrawActivity.this);
                                    rvPrize1.setLayoutManager(layoutManager);
                                    rvPrize1.setAdapter(adapter);

                                    //点击立即兑换 、立即提现
                                    adapter.setOnItemClickLitsener(new BxPrizeAdapter.OnItemChildClickListener() {
                                        @Override
                                        public void onItemChildClick(View view, int position) {
                                            MinePrize minePrize = minePrizes.get(position);
                                            if (minePrize.getActionType().equals("action.withdraw")) {
                                                if (Integer.parseInt(SaveUserInfo.getInstance(IntegralDrawActivity.this).getUserInfo("cert")) == 1) {
                                                    //红包
                                                    List<MinePrize.minePrize> redPrizeList = new ArrayList<>();
                                                    for (int i = 0; i < minePrizes.get(position).getMinePrizes().size(); i++) {
                                                        MinePrize.minePrize minePrize1 = minePrizes.get(position).getMinePrizes().get(i);
                                                        redPrizeList.add(minePrize1);
                                                    }
                                                    if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                                                        Intent intent = new Intent(IntegralDrawActivity.this, RedWithDrawActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putParcelableArrayList("redPrize", (ArrayList<? extends Parcelable>) redPrizeList);
                                                        bundle.putString("redId", minePrize.getId());
                                                        bundle.putString("from", "box");
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    //显示实名认证弹窗
                                                    showAuthPop();
                                                }
                                            } else if (minePrize.getActionType().equals("action.h5.url")) {
                                                //实物
                                                Intent intent = new Intent(IntegralDrawActivity.this, WebPrizeActivity.class);
                                                intent.putExtra("id", minePrize.getId());
                                                intent.putExtra("url", minePrize.getH5Url());
                                                intent.putExtra("name", minePrize.getName());
                                                startActivity(intent);
                                            } else if (minePrize.getActionType().equals("action.bp.use")) {
                                                //积分
                                                startActivity(new Intent(IntegralDrawActivity.this, IntegralExchangeActivity.class));
                                            }

                                        }
                                    });
                                    ivTreasureBox.setImageResource(R.mipmap.bxopen);
                                    if (status == 0)

                                    {
                                        ivTreasureBox.setEnabled(true);
                                        btnOpenTreasureBox.setEnabled(true);
                                    }
                                }
                            }, 2000);

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            //取消动画
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            //重复动画
                        }
                    });
                }
            }
        }
    }


    CommonPopupWindow popupWindow;

    /**
     * 衢州活动弹框
     */

    private void showQuZhouPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(IntegralDrawActivity.this).inflate(R.layout.download_quzhou_app, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //取值范围0.0f-1.0f 值越小越暗
//        .setAnimationStyle(R.style.AnimUp)//设置动画
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(IntegralDrawActivity.this)
                .setView(R.layout.download_quzhou_app)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(false)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(rlIntegralDraw, Gravity.CENTER, 0, 0);

    }

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

        popupWindow.showAtLocation(rlIntegralDraw, Gravity.CENTER, 0, 0);
    }

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.download_quzhou_app://衢州活动弹窗
                TextView tvQuzhouTitle = view.findViewById(R.id.tv_quzhou_title);
                TextView tvQuzhouContent = view.findViewById(R.id.tv_quzhou_content);
                TextView tvPrizeTime = view.findViewById(R.id.tv_prize_expire_time);
                Button btnGoForDownload = view.findViewById(R.id.btn_go_for_download);
                ImageView ivClose = view.findViewById(R.id.iv_close);
                if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                    OvalImageView ivQuzhouPrize = view.findViewById(R.id.iv_quzhou_prize);
                    tvQuzhouTitle.setText(sharedBean.getTitle());
                    tvQuzhouContent.setText(sharedBean.getContent());
                    tvPrizeTime.setText("奖品有效期至:" + TimeTool.getCommTime(sharedBean.getTime(), TimeTool.FORMAT_DATE_TIME_SECOND));
                    if (!EncodeAndStringTool.isStringEmpty(sharedBean.getButtonCaption())) {
                        btnGoForDownload.setText(sharedBean.getButtonCaption());
                    } else {
                        btnGoForDownload.setText("前往下载并注册");
                    }
                    ImageLoaderManager.LoadImage(IntegralDrawActivity.this, sharedBean.getPicture(), ivQuzhouPrize, R.mipmap.zw01);
                    btnGoForDownload.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            Uri uri = Uri.parse(sharedBean.getUrl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                    ivClose.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                finish();
                            }
                        }
                    });
                }
                break;
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
                        startActivity(new Intent(IntegralDrawActivity.this, RealNameAuthActivity.class));
                    }
                });
                break;
            default:
                break;
        }
    }

    //获取积分信息
    private void getInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getExchangeMain()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<IntegralExchangeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<IntegralExchangeBean> dataResponse) {
                        Log.i(TAG, "loadAppShared==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {
                            IntegralExchangeBean integralExchangeBean = dataResponse.getDat();
                            tvJiFenBalance.setText(integralExchangeBean.getUserBp() + "");
                        } else {
                            ErrorCodeTools.errorCodePrompt(IntegralDrawActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}