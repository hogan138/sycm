package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ListDataSave;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.SaveErrorTxt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 其他设置
 */
public class OtherSettingActivity extends BaseActivity {

    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;//返回按钮图片
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回按钮可点击区域
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.rl_account_logout)
    RelativeLayout rlAccountLogout;

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    public int intiLayout() {
        return R.layout.activity_other_setting;
    }


    @OnClick({R.id.iv_back, R.id.rl_account_logout})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.rl_account_logout://账户注销
                verifyCondition();
                break;
            default:
                break;
        }
    }

    //判断账户注销前置条件
    private void verifyCondition() {

        final ApiService apiService = BasePresenter.create(8000);
        apiService.verifyCondition()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse loginResponse) {
                        if (loginResponse.getErr().equals("00000")) {
                            startActivity(new Intent(OtherSettingActivity.this, AccountLogoutActivity.class));
                        } else if (loginResponse.getErr().equals("CERT01")) {
                            Toast.makeText(OtherSettingActivity.this, "未实名认证，暂不支持注销账号", Toast.LENGTH_SHORT).show();
                        } else if (loginResponse.getErr().equals("CERT02")) {
                            startActivity(new Intent(OtherSettingActivity.this, LogOutResultActivity.class));
                        } else if (loginResponse.getErr().equals("CERT03")) {
                            Toast.makeText(OtherSettingActivity.this, "该账户近三个月内有修改密码或手机号记录", Toast.LENGTH_SHORT).show();
                        } else if (loginResponse.getErr().equals("99999")) {
                            Toast.makeText(OtherSettingActivity.this, "接口返回未知错误", Toast.LENGTH_SHORT).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        tvCommonTitle.setText("其他设置");

        Log.e("token", AppConst.TOKEN);
    }
}
