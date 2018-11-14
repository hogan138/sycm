package com.shuyun.qapp.ui.webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.login.PermissionsActivity;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公用的静态webview
 */
public class WebPublicActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.animation_iv)
    ImageView animationIv;

    // 打电话所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,

    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    private String bulletin;
    AnimationDrawable animationDrawable;

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra("name");
        if ("useragree".equals(name)) {
            tvCommonTitle.setText("用户协议");
        } else if ("about".equals(name)) {
            tvCommonTitle.setText("关于我们");
        } else if ("rule".equals(name)) {
            tvCommonTitle.setText("活动规则");
        } else if ("rules".equals(name)) {
            tvCommonTitle.setText("活动规则");
            Intent intent = getIntent();
            bulletin = intent.getStringExtra("bulletin");
        }

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_contact_us;
    }


    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        /**
         * 接收js返回的手机号码,调用打电话业务
         *
         * @param phoneNum
         */
        @JavascriptInterface
        public void OpenTel(String phoneNum) {
            Log.i(TAG, "OpenTel: " + phoneNum);
            //调用打电话业务
            call(phoneNum);
        }

        /**
         * 接收js返回的规则
         *
         * @param
         */
        @JavascriptInterface
        public String actRule() {//String backParams
            return bulletin;
        }
    }

    /**
     * 调用打电话业务
     *
     * @param phoneNum
     */
    private void call(final String phoneNum) {
        WebPublicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(WebPublicActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }


    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        StatService.onResume(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInteration(), "android");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if ("useragree".equals(name)) {
            webView.loadUrl(AppConst.USER_AGREEMENT); //用户协议
        } else if ("about".equals(name)) {
            webView.loadUrl(AppConst.ABOUT_US); //关于我们
        } else if ("rule".equals(name)) {
            webView.loadUrl(SaveUserInfo.getInstance(WebPublicActivity.this).getUserInfo("h5_rule"));//积分夺宝规则
        } else if ("rules".equals(name)) {
            webView.loadUrl(AppConst.LOOK_RULES);//积分开宝箱规则
        }

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });
        mPermissionsChecker = new PermissionsChecker(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
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

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

}
