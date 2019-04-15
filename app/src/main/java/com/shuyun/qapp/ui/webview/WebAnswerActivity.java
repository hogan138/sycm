package com.shuyun.qapp.ui.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.view.AnswerSharePopupUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * H5答题页webview
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class WebAnswerActivity extends BaseActivity {

    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //答题标题
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.wv_answer_home)
    WebView wvAnswerHome;
    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();
    @BindView(R.id.ll_h5)
    LinearLayout llH5;

    private Long groupId; //题组Id
    private String h5Url; //h5url
    private String splash = "";
    private Context mContext;

    //进入标记
    String umeng_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        SharedPrefrenceTool.put(mContext, "boxId", "");//清空答题免登录返回宝箱id

        //记录标记
        umeng_from = SaveUserInfo.getInstance(this).getUserInfo("umeng_from");

        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮,如果安装了,需要显示分享按钮
         */
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {
            ivRightIcon.setVisibility(View.GONE);
        } else {
            ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
        }

        Intent intent = getIntent();
        splash = intent.getStringExtra("from");
        groupId = intent.getLongExtra("groupId", 0);
        h5Url = intent.getStringExtra("h5Url");

        //是否参与邀请分享 1——参与邀请
        Integer share = (Integer) SharedPrefrenceTool.get(this, "share", (Integer) 0);
        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        answerHomeBean.setGroupId(groupId);
        answerHomeBean.setShare(share);
        String deviceId = SmAntiFraud.getDeviceId();
        answerHomeBean.setDeviceId(deviceId);

        //友盟页面统计
        if (!EncodeAndStringTool.isStringEmpty(umeng_from) && umeng_from.equals("home")) { //首页
            String pages = "app_home_group_${" + groupId + "},app_home_group_box_${" + groupId + "},app_home_group_again_${" + groupId + "}";
            answerHomeBean.setPages(pages);
        } else if (!EncodeAndStringTool.isStringEmpty(umeng_from) && umeng_from.equals("classify")) { //分类
            String pages = "app_group_class_${" + groupId + "},app_group_class_box_${" + groupId + "},app_group_class_again_${" + groupId + "}";
            answerHomeBean.setPages(pages);
        } else { //其他
            answerHomeBean.setPages("");
        }


        WebSettings settings = wvAnswerHome.getSettings();
        settings.setJavaScriptEnabled(true);
        wvAnswerHome.setWebChromeClient(new WebChromeClient());//解决答题时无法弹出dialog问题.
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvAnswerHome.addJavascriptInterface(new JsInterationUtil(answerHomeBean, (Activity) mContext, wvAnswerHome, llH5, splash, "anwser", tvCommonTitle, ivRightIcon, groupId), "android");
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (!EncodeAndStringTool.isStringEmpty(h5Url)) {
            wvAnswerHome.loadUrl(h5Url);
        } else {
            wvAnswerHome.loadUrl(AppConst.ANSWER);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            //登录成功，调用h5方法,，传递boxId给h5
            String boxId = (String) SharedPrefrenceTool.get(mContext, "boxId", "");
            if (!EncodeAndStringTool.isStringEmpty(boxId)) { //答题登录
                sendLoginCallBack("exam", boxId);
            }
        } else if (resultCode == RESULT_OK && requestCode == 2) { //练习场登录
            //登录成功，调用h5方法,，传递登录信息给h5
            sendLoginCallBack("practice", null);
        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_answer;
    }


    @OnClick({R.id.iv_common_left_icon, R.id.iv_right_icon,})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left_icon:
                if (!EncodeAndStringTool.isStringEmpty(splash)) {
                    if (splash.equals("splash")) {
                        if (wvAnswerHome.canGoBack()) {
                            JsInterationUtil.WebViewCanBack();
                        } else {
                            finish();
                            Intent intent = new Intent(this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    if (wvAnswerHome.canGoBack()) {
                        JsInterationUtil.WebViewCanBack();
                    } else {
                        JsInterationUtil.WebViewNoBack();
                    }
                }
                break;
            case R.id.iv_right_icon:
                AnswerSharePopupUtil.showSharedPop(mContext, llH5, groupId);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (!EncodeAndStringTool.isStringEmpty(splash)) {
            if (splash.equals("splash")) {
                if (wvAnswerHome.canGoBack()) {
                    JsInterationUtil.WebViewCanBack();
                } else {
                    super.onBackPressed();
                    Intent intent = new Intent(this, HomePageActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            if (wvAnswerHome.canGoBack()) {
                JsInterationUtil.WebViewCanBack();
            } else {
                JsInterationUtil.WebViewNoBack();
            }
        }
    }

    /**
     * 回调给H5 宝箱ID
     *
     * @param boxId
     */
    public void sendLoginCallBack(String action, String boxId) {
        final JSONObject rel = new JSONObject();
        rel.put("action", action);
        rel.put("boxId", boxId);
        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        answerHomeBean.setGroupId(groupId);
        answerHomeBean.setDeviceId(SmAntiFraud.getDeviceId());
        rel.put("loginInfo", JSON.toJSONString(answerHomeBean));
        wvAnswerHome.post(new Runnable() {
            @Override
            public void run() {
                wvAnswerHome.evaluateJavascript("javascript:jsLoginCallback(" + rel.toString() + "); ", null);
            }
        });
    }

}
