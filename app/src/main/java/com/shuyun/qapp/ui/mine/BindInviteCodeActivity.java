package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 绑定邀请码
 */

public class BindInviteCodeActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("绑定邀请码");

        rlBack.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        ivClear.setOnClickListener(this);

        //邀请码EditText设置变化监听事件
        addListener(etInviteCode, ivClear);

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_bind_invite_code;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                KeyboardUtils.hideSoftInput(this);
                finish();
                break;
            case R.id.iv_clear:
                etInviteCode.setText("");
                ivClear.setVisibility(View.GONE);
                btnCommit.setEnabled(false);
                break;
            case R.id.btn_commit:
                String code = etInviteCode.getText().toString();
                RemotingEx.doRequest("bindInviteCode", ApiServiceBean.bindInviteCode(), new Object[]{code}, this);
                break;
            default:
                break;
        }
    }


    /**
     * 给editext设置监听
     *
     * @param editText 需要监听的EditText控件
     * @param clearPic 清空数据的图片
     *                 考虑如何和登录页监听抽取出同样的方法
     */
    protected void addListener(final EditText editText, final ImageView clearPic) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入文本之前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入文字后的状态
                String et = editText.getText().toString().trim();
                /**
                 * 如果输入文字内容不为空,则显示清空editText内容的图标;
                 */
                if (!EncodeAndStringTool.isStringEmpty(et)) {
                    clearPic.setVisibility(View.VISIBLE);
                    btnCommit.setEnabled(true);
                } else {
                    clearPic.setVisibility(View.GONE);
                    btnCommit.setEnabled(false);

                }
            }
        });
        /**
         * 焦点变化监听
         */
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /**
                 * 如果输入文字内容不为空,而且获得焦点,则显示清空editText内容的图标;
                 * 否则不显示清空editText内容的图标;
                 */
                String et = editText.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(et) && hasFocus) {//不等于空,且得到焦点
                    clearPic.setVisibility(View.VISIBLE);
                } else if (!EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {//不等于空,且失去焦点
                    clearPic.setVisibility(View.GONE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && hasFocus) {
                    clearPic.setVisibility(View.GONE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {
                    clearPic.setVisibility(View.GONE);
                }

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
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if (action.equals("bindInviteCode")) {
            //绑定邀请码
            ToastUtil.showToast(mContext, "提交成功");
        }
    }
}
