package com.shuyun.qapp.ui.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 启动页
 */
public class WelcomeActivity extends Activity {
    private static int LOGIN_MODE = 1;

    @BindView(R.id.fl_main)
    FrameLayout flMain;

    CountDownTimer timer; //倒计时
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //腾讯应用分析
        StatConfig.init(this);
        String appkey = "Aqc1105860265";
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, appkey,
                    StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Log.e("MTA", "MTA start failed");
        }

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        //底部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
        }

        //保存登录状态
        SaveUserInfo.getInstance(this).setUserInfo("LOGIN_MODE", String.valueOf(LOGIN_MODE));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取广告
                getAd();
            }
        }, 1600);
    }

    AdBean adBean; //广告bean

    private void getAd() {
        long curTime = System.currentTimeMillis();
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + AppConst.APP_KEY;
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        /**
         * code值
         */
        String signCode = getCode(myCode);
        ApiService apiService = BasePresenter.create(8000);
        apiService.getAd(AppConst.DEV_ID, AppConst.APP_ID, AppConst.V, curTime + "", signCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AdBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AdBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            adBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(adBean.getAd())) {
                                //加载广告页
                                showPop();

                                //倒计时结束
                                initTime();

                            } else {
                                //进入首页
                                Intent intent = new Intent(WelcomeActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WelcomeActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));

                        //进入首页
                        Intent intent = new Intent(WelcomeActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 广告弹框
     */
    public void showPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View view = inflater.inflate(R.layout.activity_splash, null);
        ImageView ivAdvertising = view.findViewById(R.id.iv_advertising);
        TextView tv_skip = view.findViewById(R.id.tv_skip);
        ImageLoaderManager.LoadImage(WelcomeActivity.this, adBean.getAd().get(0).getUrl(), ivAdvertising, R.mipmap.zw01);
        final Long model = adBean.getAd().get(0).getModel();
        final String content = adBean.getAd().get(0).getContent();
        final Long isLogin = adBean.getAd().get(0).getIsLogin();
        ivAdvertising.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (model == 3) {//题组跳转
                    if (!EncodeAndStringTool.isStringEmpty(content)) {
                        Intent intent = new Intent(WelcomeActivity.this, WebAnswerActivity.class);
                        intent.putExtra("groupId", Integer.parseInt(content));
                        intent.putExtra("from", "splash");
                        intent.putExtra("h5Url", adBean.getExamUrl());
                        intent.putExtra("isLogin", isLogin);
                        startActivity(intent);
                        finish();
                        timer.cancel();
                    }
                } else if (model == 2) {//内部链接
                    if (!EncodeAndStringTool.isStringEmpty(content)) {
                        Intent intent = new Intent(WelcomeActivity.this, WebH5Activity.class);
                        intent.putExtra("url", content);
                        intent.putExtra("name", "全民共进");
                        intent.putExtra("from", "splash");
                        intent.putExtra("isLogin", isLogin);
                        startActivity(intent);
                        finish();
                        timer.cancel();
                    }
                } else if (model == 1) {//外部链接
                    if (!EncodeAndStringTool.isStringEmpty(content)) {
                        Uri uri = Uri.parse(content);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        finish();
                        timer.cancel();
                    }
                } else if (model == 0) {
                    skip();
                }
            }
        });
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        dialog.getWindow().setWindowAnimations(R.style.popwin_style_alpha);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
            dialog.getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
        }

    }

    private void initTime() {
        timer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                //倒计时3秒后跳出进入首页
                skip();
            }
        }.start();
    }

    //跳转
    private void skip() {
        Intent intent = new Intent(WelcomeActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长

        StatService.onPause(this);
    }
}
