package com.shuyun.qapp.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.manager.ActivityCallManager;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.manager.HeartBeatManager;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.BindPhoneNumActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;
import com.zzhoujay.richtext.RichText;

import static com.shuyun.qapp.net.SykscApplication.getAppContext;

/**
 * 2018/6/9
 * ganquan
 * base基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        //设置布局
        setContentView(intiLayout());

        //改变底部导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    //在activity或者fragment中添加友盟统计
    @Override
    protected void onResume() {
        super.onResume();

        StatService.onResume(this); //腾讯统计

        HeartBeatManager.instance().start(this);

        if (!(this instanceof HomePageActivity
                || this instanceof WebAnswerActivity
                || this instanceof WebH5Activity))
            return;
        ActivityCallManager.instance().setActivity(this);
    }

    //在activity或者fragment中添加友盟统计
    @Override
    protected void onPause() {
        super.onPause();

        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            JsInterationUtil.setShow(false);
            //防止内存泄露
            UMShareAPI.get(this).release();

            //释放富文本内存
            RichText.recycle();
        } catch (Exception e) {

        }
    }

    /**
     * 非正常登陆回调
     *
     * @param data
     */
    public void callBack(Object data) {
        if (data == null) {
            //获取全局的对象
            data = LoginDataManager.instance().getOverallData();
            LoginDataManager.instance().setOverallData(null);
        }

        if (data instanceof LoginResponse) {
            LoginResponse loginResp = (LoginResponse) data;
            SharedPrefrenceTool.put(getAppContext(), "token", loginResp.getToken());
            SharedPrefrenceTool.put(getAppContext(), "expire", loginResp.getExpire());//token的有效期
            SharedPrefrenceTool.put(getAppContext(), "key", loginResp.getKey());//对称加密的秘钥。
            SharedPrefrenceTool.put(getAppContext(), "bind", loginResp.getBind());//是否绑定用户。
            SharedPrefrenceTool.put(getAppContext(), "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
            SaveUserInfo.getInstance(getAppContext()).setUserInfo("cert", loginResp.getCertification());
            AppConst.loadToken(this);
            if (!EncodeAndStringTool.isStringEmpty(loginResp.getInvite())) {
                SharedPrefrenceTool.put(getAppContext(), "invite", loginResp.getInvite());
            }
            if (0 == loginResp.getBind()) {
                Intent intent = new Intent(this, BindPhoneNumActivity.class);
                intent.putExtra("login_response", loginResp);
                startActivity(intent);
            } else if (1 == loginResp.getBind()) {
                //跳转下一级页面
                if (AppConst.isNormalLogin(this)) {
                    //正常登录
                    startActivity(new Intent(this, HomePageActivity.class));
                } else {
                    //启用游客模式
                    LoginDataManager.instance().handler(this, new Object[]{loginResp.getBoxId()});
                }

            }
        } else {
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 清理数据
     */
    public void clear() {

    }
}
