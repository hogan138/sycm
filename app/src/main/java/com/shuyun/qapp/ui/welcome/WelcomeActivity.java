package com.shuyun.qapp.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.PermissionsActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.RSAUtils;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;
import static com.umeng.socialize.net.dplus.CommonNetImpl.TAG;

/**
 * 启动页
 */
public class WelcomeActivity extends Activity {
    private static int LOGIN_MODE = 1;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mPermissionsChecker = new PermissionsChecker(this);

        //腾讯应用分析
        StatConfig.init(this);
        String appkey = "Aqc1105860265";
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, appkey,
                    com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Log.e("MTA", "MTA start failed");
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
                            AdBean adBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(adBean.getAd())) {
                                //加载广告页
                                Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
                                startActivity(intent);
                                finish();
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

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长

        StatService.onPause(this);
    }
}
