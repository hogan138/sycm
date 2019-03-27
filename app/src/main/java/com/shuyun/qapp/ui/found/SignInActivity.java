package com.shuyun.qapp.ui.found;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SignInBean;
import com.shuyun.qapp.bean.TaskBeans;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 签到
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.iv_signIn_logo)
    ImageView ivSignInLogo;
    @BindView(R.id.tv_signIn_day)
    TextView tvSignInDay;
    @BindView(R.id.tv_sign_date_one)
    TextView tvSignDateOne;
    @BindView(R.id.iv_sign_select_one)
    ImageView ivSignSelectOne;
    @BindView(R.id.tv_sign_content_one)
    TextView tvSignContentOne;
    @BindView(R.id.line_one)
    View lineOne;
    @BindView(R.id.tv_sign_date_two)
    TextView tvSignDateTwo;
    @BindView(R.id.iv_sign_select_two)
    ImageView ivSignSelectTwo;
    @BindView(R.id.tv_sign_content_two)
    TextView tvSignContentTwo;
    @BindView(R.id.line_two)
    View lineTwo;
    @BindView(R.id.tv_sign_date_three)
    TextView tvSignDateThree;
    @BindView(R.id.iv_sign_select_three)
    ImageView ivSignSelectThree;
    @BindView(R.id.tv_sign_content_three)
    TextView tvSignContentThree;
    @BindView(R.id.line_three)
    View lineThree;
    @BindView(R.id.tv_sign_date_four)
    TextView tvSignDateFour;
    @BindView(R.id.iv_sign_select_four)
    ImageView ivSignSelectFour;
    @BindView(R.id.tv_sign_content_four)
    TextView tvSignContentFour;
    @BindView(R.id.line_four)
    View lineFour;
    @BindView(R.id.tv_sign_date_five)
    TextView tvSignDateFive;
    @BindView(R.id.iv_sign_select_five)
    ImageView ivSignSelectFive;
    @BindView(R.id.tv_sign_content_five)
    TextView tvSignContentFive;
    @BindView(R.id.line_five)
    View lineFive;
    @BindView(R.id.tv_sign_date_six)
    TextView tvSignDateSix;
    @BindView(R.id.iv_sign_select_six)
    ImageView ivSignSelectSix;
    @BindView(R.id.tv_sign_content_six)
    TextView tvSignContentSix;
    @BindView(R.id.line_six)
    View lineSix;
    @BindView(R.id.tv_sign_date_seven)
    TextView tvSignDateSeven;
    @BindView(R.id.iv_sign_select_seven)
    ImageView ivSignSelectSeven;
    @BindView(R.id.tv_sign_content_seven)
    TextView tvSignContentSeven;
    @BindView(R.id.ll_add_date)
    LinearLayout llAddDate;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    Context mContext;

    //签到Id
    String nextTaskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("签到");
        ivBack.setOnClickListener(this);
        ivSignInLogo.setOnClickListener(this);

        //获取用户签到信息
        getSignInInfo();

        //获取任务信息
        getTaskInfo();

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_sign_in;
    }


    //获取用户签到信息
    public void getSignInInfo() {
        RemotingEx.doRequest("signInInfo", ApiServiceBean.getSingInInfo(), null, this);
    }

    //签到
    public void SignIn() {
        RemotingEx.doRequest("signIn", ApiServiceBean.SingIn(), new Object[]{nextTaskId}, this);
    }

    //获取任务信息
    public void getTaskInfo() {
        RemotingEx.doRequest("taskInfo", ApiServiceBean.taskInfo(), null, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_signIn_logo:
                //签到
                SignIn();
                break;
            default:
                break;
        }
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
        if ("signInInfo".equals(action)) {
            SignInBean signInBean = (SignInBean) response.getDat();
            tvMyScore.setText(signInBean.getBp().toString()); //我的积分
            tvSignInDay.setText(signInBean.getDays().toString()); //连续签到天数
            //是否签到
            if (signInBean.isSignDay()) {
                ivSignInLogo.setBackgroundResource(R.mipmap.has_signin_logo);
                ivSignInLogo.setEnabled(false);
            } else {
                ivSignInLogo.setBackgroundResource(R.mipmap.un_signin_logo);
                ivSignInLogo.setEnabled(true);
                nextTaskId = signInBean.getNextTaskId();
            }

            //显示签到信息
            showSignInfo(signInBean);
        } else if ("signIn".equals(action)) {
            ivSignInLogo.setBackgroundResource(R.mipmap.has_signin_logo);
            ivSignInLogo.setEnabled(false);
            //获取用户签到信息
            getSignInInfo();
        } else if ("taskInfo".equals(action)) {
            //获取任务信息
            TaskBeans taskBeans = (TaskBeans) response.getDat();
            initTab(taskBeans);
        }
    }

    //初始化tab
    public void initTab(TaskBeans taskBeans) {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < taskBeans.getDatas().size(); i++) {
            mTitleList.add(taskBeans.getDatas().get(i).getTabTitle());
        }
        mFragmentList.add(NewTaskFragment.newInstance(taskBeans.getDatas().get(0).getTasks(), llMain));
        mFragmentList.add(DayTaskFragment.newInstance(taskBeans.getDatas().get(1).getTasks(), llMain));
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        //设置适配器
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
        //将tablayout与fragment关联
        tabLayout.setupWithViewPager(vp);
    }

    //显示签到信息
    @SuppressLint("NewApi")
    public void showSignInfo(SignInBean signInBean) {
        for (int i = 0; i < signInBean.getDatas().size(); i++) {
            String day = signInBean.getDatas().get(i).getDay();
            String remark = signInBean.getDatas().get(i).getRemark();
            Boolean is_selected = signInBean.getDatas().get(i).isSelected();
            if (i == 0) {
                //第一天
                tvSignDateOne.setText(day);
                tvSignContentOne.setText(remark);
                if (is_selected) {
                    ivSignSelectOne.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectOne.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 1) {
                //第二天
                tvSignDateTwo.setText(day);
                tvSignContentTwo.setText(remark);
                if (is_selected) {
                    ivSignSelectTwo.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectTwo.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 2) {
                //第三天
                tvSignDateThree.setText(day);
                tvSignContentThree.setText(remark);
                if (is_selected) {
                    ivSignSelectThree.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectThree.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 3) {
                //第四天
                tvSignDateFour.setText(day);
                tvSignContentFour.setText(remark);
                if (is_selected) {
                    ivSignSelectFour.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectFour.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 4) {
                //第五天
                tvSignDateFive.setText(day);
                tvSignContentFive.setText(remark);
                if (is_selected) {
                    ivSignSelectFive.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectFive.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 5) {
                //第六天
                tvSignDateSix.setText(day);
                tvSignContentSix.setText(remark);
                if (is_selected) {
                    ivSignSelectSix.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectSix.setBackgroundResource(R.mipmap.sign_select_no);
                }
            } else if (i == 6) {
                //第七天
                tvSignDateSeven.setText(day);
                tvSignContentSeven.setText(remark);
                if (is_selected) {
                    ivSignSelectSeven.setBackgroundResource(R.mipmap.sign_select_on);
                } else {
                    ivSignSelectSeven.setBackgroundResource(R.mipmap.sign_select_no);
                }
            }

            //处理连续签到连接线
            Boolean selected1 = signInBean.getDatas().get(0).isSelected();
            Boolean selected2 = signInBean.getDatas().get(1).isSelected();
            Boolean selected3 = signInBean.getDatas().get(2).isSelected();
            Boolean selected4 = signInBean.getDatas().get(3).isSelected();
            Boolean selected5 = signInBean.getDatas().get(4).isSelected();
            Boolean selected6 = signInBean.getDatas().get(5).isSelected();
            Boolean selected7 = signInBean.getDatas().get(6).isSelected();

            //第一根线
            if (selected1 && selected2) {
                lineOne.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineOne.setBackgroundColor(getColor(R.color.color_07));
            }
            //第二根线
            if (selected2 && selected3) {
                lineTwo.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineTwo.setBackgroundColor(getColor(R.color.color_07));
            }
            //第三根线
            if (selected3 && selected4) {
                lineThree.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineThree.setBackgroundColor(getColor(R.color.color_07));
            }
            //第四根线
            if (selected4 && selected5) {
                lineFour.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineFour.setBackgroundColor(getColor(R.color.color_07));
            }
            //第五根线
            if (selected5 && selected6) {
                lineFive.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineFive.setBackgroundColor(getColor(R.color.color_07));
            }
            //第六根线
            if (selected6 && selected7) {
                lineSix.setBackgroundColor(getColor(R.color.color_ca));
            } else {
                lineSix.setBackgroundColor(getColor(R.color.color_07));
            }

        }

    }

    /**
     * 拉起微信授权页,调用微信登录界面
     */
    public static void wxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        SykscApplication.mWxApi.sendReq(req);
    }
}
