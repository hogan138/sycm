package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AuthNameBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 实名认证界面
 */
public class RealNameAuthActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommontitle;//标题
    @BindView(R.id.btn_confirm)
    Button btnConfirm;//确定
    @BindView(R.id.et_realName)
    EditText etRealName;//真实名字
    @BindView(R.id.et_id_card)
    EditText etIdCard;//身份证
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    @BindView(R.id.tv_count)
    TextView tvCount;

    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommontitle.setText("实名认证");

        phone = SaveUserInfo.getInstance(this).getUserInfo("phone");
        //电话号码
        tvPhone.setText(phone);

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_real_name_auth;
    }

    /**
     * 实名认证
     *
     * @param realName 真实名字
     * @param etIdCard 身份证
     */
    private void loadAuthNameData(String realName, String etIdCard) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.realNameAuth(realName, etIdCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AuthNameBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AuthNameBean> authNameBeanDataResponse) {
                        if (authNameBeanDataResponse.isSuccees()) {
                            AuthNameBean authNameBean = authNameBeanDataResponse.getDat();
                            //跳转到认证结果界面;
                            if (!EncodeAndStringTool.isObjectEmpty(authNameBean)) {
                                Intent intent = new Intent(RealNameAuthActivity.this, AuthResultActivity.class);
                                intent.putExtra("authName_result", authNameBean);
                                startActivity(intent);
                                SaveUserInfo.getInstance(RealNameAuthActivity.this).setUserInfo("certinfo", authNameBean.getCertInfo());
                                SaveUserInfo.getInstance(RealNameAuthActivity.this).setUserInfo("cert", String.valueOf(authNameBean.getStatus()));
                                finish();
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(RealNameAuthActivity.this, authNameBeanDataResponse.getErr(), authNameBeanDataResponse.getMsg());
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

    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.btn_contact_our})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                String realName = etRealName.getText().toString().trim();
                String idCard = etIdCard.getText().toString().trim();
                if ((!EncodeAndStringTool.isStringEmpty(realName)) && (!EncodeAndStringTool.isStringEmpty(idCard))) {
                    if (RegularTool.isIDCard(idCard)) {
                        loadAuthNameData(realName, idCard);
                    } else {
                        ToastUtil.showToast(this, "您输入的身份证号码有误,请重新输入!");
                    }
                } else {
                    ToastUtil.showToast(this, "您还没有输入姓名或身份证号,请重新输入!");
                }
                break;
            case R.id.btn_contact_our:
                break;
            default:
                break;
        }
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

    // 只允许字母、数字和汉字
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
