package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundPropsExchangeAdapter;
import com.shuyun.qapp.adapter.WithdrawInfoAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MessageEvent;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.ScoreExchangeBeans;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.login.VerifyCodeActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改个人信息
 */
public class ChangePersonalInfoActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.rl_change_head_icon)
    RelativeLayout rlChangeHeadIcon;//修改头像item
    @BindView(R.id.rl_bind_phone_num)
    RelativeLayout rlBindPhoneNum;//绑定手机item
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.ll_change_personal_info)
    LinearLayout llChangePersonalInfo;
    @BindView(R.id.iv_header_photo)
    ImageView ivHeaderPhoto;//头像
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;//手机号码
    @BindView(R.id.rl_modify_password)
    RelativeLayout rlModifyPassword;
    @BindView(R.id.rl_bind_wechat)
    RelativeLayout rlBindWechat;//微信绑定
    @BindView(R.id.tv_bind_status)
    TextView tvBindStatus;//微信绑定状态
    @BindView(R.id.ll_real_name_auth)
    LinearLayout llRealNameAuth;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;
    @BindView(R.id.tv_real_title)
    TextView tvRealTitle;
    @BindView(R.id.tv_real_description)
    TextView tvRealDescription;
    @BindView(R.id.tv_real_status)
    TextView tvRealStatus; //实名认证状态
    @BindView(R.id.rv_add_withdraw)
    RecyclerView rvAddWithdraw;

    private boolean mIsShowing = false;

    private WithdrawInfoAdapter withdrawInfoAdapter;

    private Context mContext;

    //实名信息
    String real_info = "";
    /**
     * 底部gridView数据
     */
    private List<Map<String, Object>> dataList;
    //图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("基本信息修改");
        mContext = this;

        /**
         * 检测微信是否安装,如果没有安装,需不显示微信绑定状态;如果安装了微信则显示微信绑定状态.
         */
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {
            rlBindWechat.setVisibility(View.GONE);
        } else {
            rlBindWechat.setVisibility(View.VISIBLE);
        }

        MyActivityManager.getInstance().pushOneActivity(this);

        EventBus.getDefault().register(this);//注册Eventbus
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_change_personal_info;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        int head = Integer.parseInt(SaveUserInfo.getInstance(this).getUserInfo("icon"));
        if (head > 0) {
            ivHeaderPhoto.setImageResource(icon[head - 1]);//本地头像图片
        } else {
            //网络头像图片
            ImageLoaderManager.LoadImage(this, SaveUserInfo.getInstance(this).getUserInfo("icon1"), ivHeaderPhoto, R.mipmap.head);
        }
        String phone = SaveUserInfo.getInstance(this).getUserInfo("phone");
        tvPhoneNum.setText(phone.replace(phone.substring(3, 9), "******"));
        handler.post(runnable);

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Integer.parseInt(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("wxBind")) == 1) {
                tvBindStatus.setText("已绑定");
            } else if (Integer.parseInt(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("wxBind")) == 0) {
                tvBindStatus.setText("未绑定");
            }
            handler.postDelayed(runnable, 500);
        }
    };


    @OnClick({R.id.iv_back, R.id.rl_change_head_icon, R.id.rl_bind_phone_num, R.id.rl_bind_wechat, R.id.rl_modify_password,
            R.id.ll_real_name_auth, R.id.btn_contact_our})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_change_head_icon:
                //更换头像
                initPopup();
                break;
            case R.id.rl_bind_phone_num:
                Intent intent = new Intent(this, ChangePhoneNumActivity.class);
                intent.putExtra("bind_phone", SaveUserInfo.getInstance(this).getUserInfo("phone"));
                startActivity(intent);
                break;
            case R.id.rl_bind_wechat:
                if (Integer.parseInt(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("wxBind")) == 0) {
                    //未绑定拉起微信页
                    wxLogin();
                } else if (Integer.parseInt(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("wxBind")) == 1) {
                    //微信绑定弹窗
                    showWechatPop();
                }
                break;
            case R.id.rl_modify_password: //修改密码
                Intent modify = new Intent(this, VerifyCodeActivity.class);
                modify.putExtra("phone", SaveUserInfo.getInstance(this).getUserInfo("phone"));
                modify.putExtra("name", "modifyPwd");
                startActivity(modify);
                break;
            case R.id.ll_real_name_auth: //实名认证
                startActivity(new Intent(this, RealNameAuthActivity.class));
                break;
            case R.id.btn_contact_our: //联系客服
                Intent i = new Intent(this, WebH5Activity.class);
                i.putExtra("url", SaveUserInfo.getInstance(this).getUserInfo("contactUs_url"));
                i.putExtra("name", "联系客服");//名称 标题
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void initPopup() {
        View pop = View.inflate(this, R.layout.change_header_popupwindow, null);
        GridView gvIcon = pop.findViewById(R.id.gv_head_icon);
        initIconData();
        String[] from = {"img"};
        int[] to = {R.id.iv_icon};
        SimpleAdapter adapter = new SimpleAdapter(this, dataList, R.layout.head_icon_item, from, to);
        gvIcon.setAdapter(adapter);

        final PopupWindow popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style_bottom);
        mIsShowing = false;
        if (!mIsShowing) {
            popupWindow.showAtLocation(llChangePersonalInfo, Gravity.BOTTOM, 0, 0);
            mIsShowing = true;
        }
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadChangeHeader(position + 1);
                ivHeaderPhoto.setImageResource(icon[position]);
                if (popupWindow != null && mIsShowing) {
                    popupWindow.dismiss();
                    mIsShowing = false;
                }
            }
        });
    }

    /**
     * 更换头像
     *
     * @param position
     */
    private void loadChangeHeader(int position) {
        RemotingEx.doRequest(ApiServiceBean.changeHeader(), new Object[]{position}, new OnRemotingCallBackListener<Integer>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Integer> dataResponse) {
                if (dataResponse.isSuccees()) {
                    Integer headId = dataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(headId)) {
                        //首选项存位置信息;用户头像id
                        SharedPrefrenceTool.put(ChangePersonalInfoActivity.this, "headerId", headId);
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(ChangePersonalInfoActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }

    private void initIconData() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            dataList.add(map);
        }
    }

    CommonPopupWindow commonPopupWindow;

    /**
     * 绑定微信popupWindow
     */
    public void showWechatPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) && commonPopupWindow.isShowing())
            return;

        View upView = LayoutInflater.from(this).inflate(R.layout.bind_wechat_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.bind_wechat_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        commonPopupWindow.showAtLocation(llChangePersonalInfo, Gravity.CENTER, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.bind_wechat_popupwindow:
                ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
                TextView tvNickName = (TextView) view.findViewById(R.id.tv_nick_name);
                ImageView ivWechatHead = (ImageView) view.findViewById(R.id.iv_wechat_head);
                Button btnChangeBindWechat = (Button) view.findViewById(R.id.btn_change_bind_wechat);
                if (!EncodeAndStringTool.isStringEmpty(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("nickname"))) {
                    tvNickName.setText(SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("nickname"));//昵称
                }

                GlideUtils.LoadCircleImage(ChangePersonalInfoActivity.this, SaveUserInfo.getInstance(ChangePersonalInfoActivity.this).getUserInfo("wxHeader"), ivWechatHead);
                ivClose.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btnChangeBindWechat.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                        wxLogin();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 拉起微信授权页,调用微信登录界面
     */
    private void wxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        SykscApplication.mWxApi.sendReq(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();

        loadMineHomeData1();

        KeyboardUtils.hideSoftInput(ChangePersonalInfoActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void loadMineHomeData1() {
        RemotingEx.doRequest(ApiServiceBean.getMineHomeData(), new OnRemotingCallBackListener<MineBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<MineBean> listDataResponse) {
                if (listDataResponse.isSuccees()) {
                    MineBean mineBean = listDataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                        try {
                            //实名认证
                            MineBean.CertBaseBean certBaseBean = mineBean.getCertBase();
                            Long status = certBaseBean.getStatus();
                            boolean enabled = certBaseBean.isEnabled();
                            //实名信息
                            tvRealTitle.setText(certBaseBean.getTitle());
                            tvRealStatus.setText(certBaseBean.getStateName());
                            tvRealDescription.setText(certBaseBean.getMessage());
                            //更改颜色
                            if (status == 3) {
                                tvRealStatus.setTextColor(Color.parseColor("#0194EC"));
                            } else {
                                tvRealStatus.setTextColor(Color.parseColor("#F53434"));
                            }
                            //是否可以点击
                            if (enabled) {
                                llRealNameAuth.setEnabled(true);
                            } else {
                                llRealNameAuth.setEnabled(false);
                            }

                            //提现信息
                            final List<MineBean.WithdrawBaseBean> withdrawBaseBeanList = mineBean.getWithdrawBase();
                            //初始化适配器
                            withdrawInfoAdapter = new WithdrawInfoAdapter(withdrawBaseBeanList, ChangePersonalInfoActivity.this);
                            withdrawInfoAdapter.setOnItemClickLitsener(new WithdrawInfoAdapter.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    MineBean.WithdrawBaseBean withdrawBaseBean = withdrawBaseBeanList.get(position);
                                    Long bankType = withdrawBaseBean.getBankType();
                                    if (bankType == 1) {//支付宝提现信息
                                        startActivity(new Intent(mContext, AddWithdrawInfoActivity.class));
                                    } else if (bankType == 2) {//微信提现信息
                                        //拉起微信
                                        SaveUserInfo.getInstance(mContext).setUserInfo("bind_weixin_addwithdraw", "add");
                                        wxLogin();
                                    }
                                }
                            });
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChangePersonalInfoActivity.this);
                            linearLayoutManager.setSmoothScrollbarEnabled(true);
                            linearLayoutManager.setAutoMeasureEnabled(true);
                            rvAddWithdraw.setLayoutManager(linearLayoutManager);
                            //解决数据加载不完的问题
                            rvAddWithdraw.setHasFixedSize(true);
                            rvAddWithdraw.setNestedScrollingEnabled(false);
                            rvAddWithdraw.setAdapter(withdrawInfoAdapter);
                        } catch (Exception e) {

                        }
                    } else {
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(ChangePersonalInfoActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if ("wx_commit_success".equals(messageEvent.getMessage())) {
            //获取任务信息
            loadMineHomeData1();
        }
    }

}
