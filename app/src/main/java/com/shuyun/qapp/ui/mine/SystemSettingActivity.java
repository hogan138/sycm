package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SaveUserInfo1;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 系统设置
 */
public class SystemSettingActivity extends BaseActivity implements OnRemotingCallBackListener<AppVersionBean> {

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
//    @BindView(R.id.mNiceVideoPlayer)
//    NiceVideoPlayer mNiceVideoPlayer;
//    @BindView(R.id.iv_gif)
//    ImageView ivGif;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("系统设置");
        MyActivityManager.getInstance().pushOneActivity(this);

        tvVersion.setText("V" + APKVersionCodeTools.getVerName(this));

//        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
//        String videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
////        videoUrl = Environment.getExternalStorageDirectory().getPath().concat("/办公室小野.mp4");
//        mNiceVideoPlayer.setUp(videoUrl, null);
//        TxVideoPlayerController controller = new TxVideoPlayerController(this);
//        controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");
//        controller.setLenght(98000);
//        Glide.with(this)
//                .load("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg")
//                .placeholder(R.drawable.img_default)
//                .crossFade()
//                .into(controller.imageView());
//        mNiceVideoPlayer.setController(controller);

//        Glide.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1550485084238&di=62e9b299c82396d93ad0a2e0d5c0922c&imgtype=0&src=http%3A%2F%2Fs9.rr.itc.cn%2Fr%2FwapChange%2F20161_23_9%2Fa4nrzg2161334442352.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivGif);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_system_setting;
    }

    @OnClick({R.id.iv_back, R.id.btn_exit_login, R.id.rl_use_info, R.id.rl_about_us, R.id.rl_version, R.id.rl_other_set})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_use_info:
                //更改用户信息
                Intent intent = new Intent(mContext, ChangePersonalInfoActivity.class);
                startActivity(intent);
                //用户信息
                break;
            case R.id.rl_about_us:
                //关于我们
                Intent i = new Intent(mContext, WebPublicActivity.class);
                i.putExtra("name", "about");
                startActivity(i);
                break;
            case R.id.rl_version:
                //版本更新
                updateVersion();
                break;
            case R.id.rl_other_set:
                //其他设置
                startActivity(new Intent(this, OtherSettingActivity.class));
                break;
            case R.id.btn_exit_login:
                //退出登录
                exitLoginDialog();
                break;
            default:
                break;
        }
    }

    //更新版本
    private void updateVersion() {
        String dName = APKVersionCodeTools.getVerName(this);
        long curTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(AppConst.DEV_ID)
                .append(AppConst.APP_ID)
                .append(AppConst.V)
                .append(curTime)
                .append(dName)
                .append(AppConst.APP_KEY);
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(sb.toString());
        String signCode = getCode(myCode);

        RemotingEx.doRequest(ApiServiceBean.updateVersion(), new Object[]{dName, AppConst.DEV_ID, AppConst.APP_ID, AppConst.V, curTime, signCode}, this);
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
                        logout();
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

    /**
     * 退出登录
     */
    private void logout() {
        //阿里推送解除绑定别名
        AliPushBind.UnbindPush(new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {

                //友盟统计登出
                MobclickAgent.onProfileSignOff();

                if ("1".equals(SaveUserInfo1.getInstance(mContext).getUserInfo("tourists"))) {
                    //启用游客模式
                    Intent intent = new Intent(mContext, HomePageActivity.class);
                    intent.putExtra(AppConst.APP_ACTION_PARAM, AppConst.APP_ACTION_LOGOUT);
                    startActivity(intent);
                } else {
                    //不启用游客模式
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                //清空数据
                clearData();

                MyActivityManager.getInstance().finishAllActivity();//销毁所有页面
            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> response) {

            }
        });
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<AppVersionBean> response) {
        if (response.isSuccees()) {
            AppVersionBean appVersionBean = response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(appVersionBean)) {
                Long mode = appVersionBean.getMode();
                if (mode == 0) {
                    Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                } else if (mode == 1) {
                    updateDialog(appVersionBean.getUrl());
                } else if (mode == 2) {

                }
            }
        } else {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
        }
    }

    //清除缓存数据
    private void clearData() {

        //清空数据
        SharedPrefrenceTool.clear(mContext);
        AppConst.loadToken(mContext);

        //清空缓存
        SaveUserInfo.clear(mContext);
    }
}
