package com.shuyun.qapp.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.ui.answer.WebAnswerActivity;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.WebBannerActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;
import static com.umeng.socialize.net.dplus.CommonNetImpl.TAG;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_advertising)
    ImageView ivAdvertising;
    @BindView(R.id.tv_skip)
    TextView tvSkip;

    CountDownTimer timer;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getAd();

        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

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
                .subscribe(new Observer<DataResponse<AdBean>>() {//<AdBean>
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AdBean> dataResponse) {//<AdBean>
                        if (dataResponse.isSuccees()) {
                            final AdBean adBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(adBean.getAd())) {
                                ImageLoaderManager.LoadImage(SplashActivity.this, adBean.getAd().get(0).getUrl(), ivAdvertising, R.mipmap.zw01);
                                final int model = adBean.getAd().get(0).getModel();
                                final String content = adBean.getAd().get(0).getContent();
                                ivAdvertising.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (model == 3) {//题组跳转
                                            if (!EncodeAndStringTool.isStringEmpty(content)) {
                                                Long expire = (Long) SharedPrefrenceTool.get(SplashActivity.this, "expire", System.currentTimeMillis());//token的有效时间
                                                long currentTimeMillis = System.currentTimeMillis();
                                                if (!AppConst.isLogon() || currentTimeMillis >= expire) {
                                                    //拉起登录界面
                                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    return;
                                                } else {
                                                    Intent intent = new Intent(SplashActivity.this, WebAnswerActivity.class);
                                                    intent.putExtra("groupId", Integer.parseInt(content));
                                                    intent.putExtra("from", "splash");
                                                    intent.putExtra("h5Url", adBean.getExamUrl());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                timer.cancel();
                                            }
                                        } else if (model == 2) {//内部链接
                                            if (!EncodeAndStringTool.isStringEmpty(content)) {
                                                Intent intent = new Intent(SplashActivity.this, WebBannerActivity.class);
                                                intent.putExtra("url", content);
                                                intent.putExtra("from", "splash");
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
                                        }
                                    }
                                });

                                timer = new CountDownTimer(4 * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        time = millisUntilFinished / 1000;
                                    }

                                    @Override
                                    public void onFinish() {
                                        //倒计时3秒后跳出进入首页
                                        if (time <= 1) {
                                            Long expire = (Long) SharedPrefrenceTool.get(SplashActivity.this, "expire", System.currentTimeMillis());//token的有效时间
                                            long currentTimeMillis = System.currentTimeMillis();
                                            if (!AppConst.isLogon() || currentTimeMillis >= expire) {
                                                //拉起登录界面
                                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                                timer.cancel();
                                                return;
                                            } else {
                                                Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                                                startActivity(intent);
                                                finish();
                                                timer.cancel();
                                            }
                                        }
                                    }
                                }.start();

                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(SplashActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        StatService.onResume(this);
    }

    @OnClick({R.id.tv_skip})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                Long expire = (Long) SharedPrefrenceTool.get(SplashActivity.this, "expire", System.currentTimeMillis());//token的有效时间
                long currentTimeMillis = System.currentTimeMillis();
                if (!AppConst.isLogon() || currentTimeMillis >= expire) {
                    //拉起登录界面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
                timer.cancel();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    //在activity或者fragment中添加友盟统计

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
    }

}
