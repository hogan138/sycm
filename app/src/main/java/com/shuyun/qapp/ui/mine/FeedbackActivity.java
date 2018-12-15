package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.NetworkUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FeedBackSuggestBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 反馈
 */
public class FeedbackActivity extends BaseActivity implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_real_name1)
    EditText etRealName1;//真实姓名
    @BindView(R.id.et_contact)
    EditText etContact;//联系方式
    @BindView(R.id.et_feed_content)
    EditText etFeedContent;//反馈内容
    @BindView(R.id.btn_commit)
    Button btnCommit;//提交

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("反馈建议");

        etFeedContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    enterKeyword();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_feedback;
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                enterKeyword();
                break;
        }
    }


    private void enterKeyword() {
        String realName = etRealName1.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String feedContent = etFeedContent.getText().toString().trim();
        if (EncodeAndStringTool.isStringEmpty(realName) || EncodeAndStringTool.isStringEmpty(contact) || EncodeAndStringTool.isStringEmpty(feedContent)) {
            ToastUtil.showToast(this, "您没有输入真实名字、联系方式或反馈内容,请您重新输入!");
        } else {
            FeedBackSuggestBean feedBackSuggestBean = new FeedBackSuggestBean();
            feedBackSuggestBean.setName(realName);
            feedBackSuggestBean.setPhone(contact);
            feedBackSuggestBean.setContent(feedContent);

            if (NetworkUtils.isAvailableByPing()) {
                loadFeedBack(feedBackSuggestBean);
            } else {
                Toast.makeText(this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 反馈建议99999
     */
    private void loadFeedBack(FeedBackSuggestBean feedBackSuggestBean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(feedBackSuggestBean));
        RemotingEx.doRequest(ApiServiceBean.getFeedBack(), new Object[]{body}, this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(this, response.getErr(), response.getMsg());
            return;
        }

        ToastUtil.showToast(this, "反馈成功!");
        finish();
    }
}
