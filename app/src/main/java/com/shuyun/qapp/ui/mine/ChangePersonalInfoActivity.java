package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.login.ChangePasswordActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改个人信息
 */
public class ChangePersonalInfoActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.rl_change_head_icon)
    RelativeLayout rlChangeHeadIcon;//修改头像item
    @BindView(R.id.rl_bind_phone_num)
    RelativeLayout rlBindPhoneNum;//绑定手机item
    @BindView(R.id.rl_real_name_auth)
    RelativeLayout rlRealNameAuth;//实名认证item
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
    private static final String TAG = "ChangePersonalInfoActiv";
    @BindView(R.id.tv_status)
    TextView tvStatus;//是否实名认证
    @BindView(R.id.rl_modify_password)
    RelativeLayout rlModifyPassword;
    @BindView(R.id.rl_bind_wechat)
    RelativeLayout rlBindWechat;//微信绑定
    @BindView(R.id.tv_bind_status)
    TextView tvBindStatus;//微信绑定状态


    private boolean mIsShowing = false;

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

        /**
         * 检测微信是否安装,如果没有安装,需不显示微信绑定状态;如果安装了微信则显示微信绑定状态.
         */
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            rlBindWechat.setVisibility(View.GONE);
        } else {
            rlBindWechat.setVisibility(View.VISIBLE);
        }

        MyActivityManager.getInstance().pushOneActivity(this);
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
            ImageLoaderManager.LoadImage(this,
                    SaveUserInfo.getInstance(this).getUserInfo("icon1"), ivHeaderPhoto, R.mipmap.head);
        }

        tvPhoneNum.setText(SaveUserInfo.getInstance(this).getUserInfo("phone"));
        int certInfo = Integer.parseInt(SaveUserInfo.getInstance(this).getUserInfo("cert"));
        String CertInfo = SaveUserInfo.getInstance(this).getUserInfo("certinfo");
        if (1 == certInfo) {
            tvStatus.setText(CertInfo);
        } else if (2 == certInfo) {
            tvStatus.setText("审核中");
        } else if (0 == certInfo) {
            tvStatus.setText("未实名认证");
        } else {
            tvStatus.setText("未通过");
        }


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


    @OnClick({R.id.iv_back, R.id.rl_change_head_icon, R.id.rl_bind_phone_num, R.id.rl_bind_wechat, R.id.rl_modify_password, R.id.rl_real_name_auth})
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
            case R.id.rl_modify_password:
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                intent1.putExtra("modify", "modify");
                startActivity(intent1);
                break;
            case R.id.rl_real_name_auth:
                int certInfo = Integer.parseInt(SaveUserInfo.getInstance(this).getUserInfo("cert"));
                if (certInfo == 1) {
                    ToastUtil.showToast(ChangePersonalInfoActivity.this, "实名认证成功后，暂不可再次修改");
                } else if (certInfo == 2) {
                    ToastUtil.showToast(ChangePersonalInfoActivity.this, "实名认证正在审核中,请您耐心等待");
                } else {
                    startActivity(new Intent(this, RealNameAuthActivity.class));
                }
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
        ApiService apiService = BasePresenter.create(8000);
        apiService.changeHeader(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<Integer> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            Integer headId = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(headId)) {
                                //首选项存位置信息;用户头像id
                                SharedPrefrenceTool.put(ChangePersonalInfoActivity.this, "headerId", headId);
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(ChangePersonalInfoActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

        View upView = LayoutInflater.from(this).inflate(R.layout.bind_wechat_pop, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.bind_wechat_pop)
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
            case R.layout.bind_wechat_pop:
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
        MyApplication.mWxApi.sendReq(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);
        initData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
