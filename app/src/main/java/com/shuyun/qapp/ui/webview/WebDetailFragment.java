package com.shuyun.qapp.ui.webview;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 宝贝详情h5
 */
public class WebDetailFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.webView_prize)
    WebView webViewPrize;
    @BindView(R.id.animation_iv)
    ImageView animationIv;

    AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_prize_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();

        return view;
    }

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineFragment"); //统计页面，"MainScreen"为页面名称，可自定义

        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        String deviceId = SmAntiFraud.getDeviceId();
        answerHomeBean.setDeviceId(deviceId);

        WebSettings settings = webViewPrize.getSettings();
        settings.setJavaScriptEnabled(true);
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webViewPrize.setWebChromeClient(new WebChromeClient());//解决答题时无法弹出dialog问题.
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewPrize.addJavascriptInterface(new JsInteration(), "android");
        webViewPrize.loadUrl(SaveUserInfo.getInstance(getActivity()).getUserInfo("h5_url_prize"));
        webViewPrize.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });


    }

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        /**
         * 登录返回的token需要传给H5
         *
         * @return
         */
        @JavascriptInterface
        public String answerLogin() {
            String answerHome = null;
            if (!EncodeAndStringTool.isObjectEmpty(answerHomeBean)) {
                answerHome = new Gson().toJson(answerHomeBean);
            }
            Log.e(TAG, answerHome.toString());
            return answerHome.toString();
        }

        /**
         * 倒计时结束布局设置成开奖信息
         */
        @JavascriptInterface
        public void overTime() {
            try {
                //已开奖
                SaveUserInfo.getInstance(getActivity()).setUserInfo("ScheduleStatus", "1");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().findViewById(R.id.ll_look_result).setVisibility(View.VISIBLE);
                        getActivity().findViewById(R.id.ll_exchange).setVisibility(View.GONE);
                    }
                });

            } catch (Exception e) {

            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
