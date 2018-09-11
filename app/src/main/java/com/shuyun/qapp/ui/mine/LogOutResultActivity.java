package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.SaveErrorTxt;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogOutResultActivity extends BaseActivity {

    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("账号注销");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_log_out_result;
    }


    @OnClick({R.id.iv_back, R.id.btn_logout_sure, R.id.btn_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.btn_logout_sure:
                //确定
                finish();
                break;
            case R.id.btn_cancel:
                removeCondition();
                break;
            default:
                break;
        }
    }

    //撤回注销申请
    private void removeCondition() {

        final ApiService apiService = BasePresenter.create(8000);
        apiService.removeCondition()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse loginResponse) {
                        if (loginResponse.isSuccees()) {
                            if (loginResponse.getErr().equals("00000")) {
                                finish();
                                Toast.makeText(LogOutResultActivity.this, "撤回注销申请成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogOutResultActivity.this, "撤回注销申请失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(LogOutResultActivity.this, loginResponse.getErr(), loginResponse.getMsg());
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
}
