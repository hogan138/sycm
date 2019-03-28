package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 70周年Fragment
 */
public class SeventyYearFragment extends Fragment {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    Unbinder unbinder;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;

    private Activity mContext;

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seventyyear, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("建国70周年");

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            answerHomeBean.setToken(AppConst.TOKEN);
            answerHomeBean.setRandom(AppConst.RANDOM);
            answerHomeBean.setV(AppConst.V);
            answerHomeBean.setSalt(AppConst.SALT);
            answerHomeBean.setAppSecret(AppConst.APP_KEY);
            String deviceId = SmAntiFraud.getDeviceId();
            answerHomeBean.setDeviceId(deviceId);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.setWebChromeClient(new WebChromeClient());//解决答题时无法弹出dialog问题.
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.addJavascriptInterface(new JsInterationUtil(answerHomeBean, mContext, webView, llMain, tvCommonTitle, ivRightIcon, tvRight), "android");
            webView.loadUrl(SaveUserInfo.getInstance(mContext).getUserInfo("home_tab_url"));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

