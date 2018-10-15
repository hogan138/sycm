package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.view.MyGalleryView;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 系统设置
 */
public class SystemSettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rl_use_info)
    RelativeLayout rlUseInfo;//用户信息
    @BindView(R.id.btn_exit_login)
    Button btnExitLogin;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.rl_version)
    RelativeLayout rlVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("系统设置");
        MyActivityManager.getInstance().pushOneActivity(this);

        tvVersion.setText("V" + APKVersionCodeTools.getVerName(this));

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_system_setting;
    }

    @OnClick({R.id.iv_back, R.id.btn_exit_login, R.id.rl_use_info, R.id.rl_about_us, R.id.rl_feed_back, R.id.rl_version, R.id.rl_other_set})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_use_info:
                Intent intent = new Intent(SystemSettingActivity.this, ChangePersonalInfoActivity.class);
                startActivity(intent);
                //用户信息
                break;
            case R.id.rl_about_us:
                //关于我们
                Intent i = new Intent(SystemSettingActivity.this, WebPublicActivity.class);
                i.putExtra("name", "about");
                startActivity(i);
                break;
            case R.id.rl_feed_back:
                //反馈建议
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.rl_version:
                updateVersion();
                break;
            case R.id.rl_other_set:
                //其他设置 TODO
                startActivity(new Intent(this, OtherSettingActivity.class));
                break;
            case R.id.btn_exit_login:
                exitLoginDialog();
                break;
            //反馈建议
            default:
                break;
        }
    }

    //更新版本
    private void updateVersion() {
        long curTime = System.currentTimeMillis();
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + APKVersionCodeTools.getVerName(this) + AppConst.APP_KEY;
        Log.e("签名", signString);
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        /**
         * code值
         */
        String signCode = getCode(myCode);

        final ApiService apiService = BasePresenter.create(8000);
        apiService.updateVersion(APKVersionCodeTools.getVerName(this), AppConst.DEV_ID, AppConst.APP_ID, AppConst.V, curTime, signCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AppVersionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AppVersionBean> loginResponse) {
                        if (loginResponse.isSuccees()) {
                            AppVersionBean appVersionBean = loginResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(appVersionBean)) {
                                int mode = appVersionBean.getMode();
                                if (mode == 0) {
                                    Toast.makeText(SystemSettingActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                                } else if (mode == 1) {
                                    updateDialog(appVersionBean.getUrl());
                                } else if (mode == 2) {

                                }
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(SystemSettingActivity.this, loginResponse.getErr(), loginResponse.getMsg());
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

    private void updateDialog(final String url) {
        new CircleDialog.Builder(this)
                .setTitle("监测到新版本")
                .setText("已经监测到新版本")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("前往更新", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .setNegative("暂不更新", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

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

    private void exitLoginDialog() {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("退出登录？")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("取消", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .setNegative("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //清空数据
                        SharedPrefrenceTool.clear(SystemSettingActivity.this);

                        //设置别名为空
                        JPushInterface.setAlias(SystemSettingActivity.this, new Random().nextInt(), "");

                        MyActivityManager.getInstance().finishAllActivity();//销毁所有页面
                        startActivity(new Intent(SystemSettingActivity.this, LoginActivity.class));
                        finish();

                        //清空缓存
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.group_count", "");
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.real_count", "");
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.h5_count", "");
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.invite_count", "");
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.integral_count", "");
                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("action.answer.against_count", "");
//                        //活动到期时间
//                        SaveUserInfo.getInstance(SystemSettingActivity.this).setUserInfo("get_activity_time", "");
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
