package com.shuyun.qapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.google.gson.Gson;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.alipay.AlipayTradeManager;
import com.shuyun.qapp.bean.AuthHeader;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.H5JumpBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.ReturnDialogBean;
import com.shuyun.qapp.bean.SharePublicBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.CashRecordActivity;
import com.shuyun.qapp.ui.mine.NewRedWithdrawActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;
import com.shuyun.qapp.view.SharePopupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.utils
 * @ClassName: JsInterationUtil
 * @Description: 把js的注入和方法统一管理
 * @Author: ganquan
 * @CreateDate: 2019/3/21 11:39
 */
public class JsInterationUtil implements CommonPopupWindow.ViewInterface {

    private static final String TAG = "JsInteration";

    //票务id
    String id = "";
    static Activity activity;
    //顶部标题文字
    TextView tvCommonTitle;
    //右边分享按钮
    ImageView ivRightIcon;
    //页面根布局
    static View view;
    //左边文字
    TextView tvRight;
    //Webview
    static WebView webView;
    //经纬度、名称
    private String Longitude;
    private String Latitude;
    private String Name;

    //公用页面规则
    private String bulletin;

    //积分兑换详情页面
    View ll_look_result;
    View ll_exchange;

    private static boolean show = false;
    static ReturnDialogBean returnDialogBean;

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    String main_box;//开宝箱
    private MinePrize minePrize;

    private BoxBean boxBean;

    public JsInterationUtil() {
    }

    //开宝箱页面
    public JsInterationUtil(WebAnswerHomeBean answerHomeBean, Activity activity, View view, String main_box, MinePrize minePrize, BoxBean boxBean, TextView tvCommonTitle, WebView webView) {
        this.answerHomeBean = answerHomeBean;
        this.activity = activity;
        this.view = view;
        this.main_box = main_box;
        this.minePrize = minePrize;
        this.boxBean = boxBean;
        this.tvCommonTitle = tvCommonTitle;
        this.webView = webView;
    }


    //积分兑换详情页面
    public JsInterationUtil(WebAnswerHomeBean answerHomeBean, Activity activity, View ll_look_result, View ll_exchange) {
        this.answerHomeBean = answerHomeBean;
        this.activity = activity;
        this.ll_look_result = ll_look_result;
        this.ll_exchange = ll_exchange;
    }


    //公用静态webview
    public JsInterationUtil(Activity activity, String bulletin) {
        this.activity = activity;
        this.bulletin = bulletin;
    }

    //h5页面
    public JsInterationUtil(String id, Activity activity, TextView tvCommonTitle, ImageView ivRightIcon, View view, TextView tvRight, WebView webView, WebAnswerHomeBean answerHomeBean) {
        this.id = id;
        this.activity = activity;
        this.tvCommonTitle = tvCommonTitle;
        this.ivRightIcon = ivRightIcon;
        this.view = view;
        this.tvRight = tvRight;
        this.webView = webView;
        this.answerHomeBean = answerHomeBean;
    }

    @JavascriptInterface
    public String getTicketData(String jsParams) {
        AuthHeader authHeader = new AuthHeader();
        authHeader.setAuthorization(AppConst.TOKEN);
        authHeader.setSycm(AppConst.sycm());
        if (!EncodeAndStringTool.isStringEmpty(id)) {
            authHeader.setId(id);
        }
        return JSON.toJSONString(authHeader);
    }

    /**
     * 衢州活动弹窗和商户引流跳外部链接
     *
     * @param url
     */
    @JavascriptInterface
    public void openWeb(final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * JS调用此方法,关闭H5页面进入App页面
     */
    @JavascriptInterface
    public void closeWeb(String jsParams) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        });
    }

    /**
     * 头部标题
     *
     * @param page  是否显示分享图标1:显示;其他值不显示
     * @param title 标题
     * @param id    答题Id
     */
    @JavascriptInterface
    public void header(final int page, final String title, String id) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCommonTitle.setText(title);
            }
        });

    }


    /**
     * 是否显示右边分享按钮
     */
    @JavascriptInterface
    public void showShare(final String config) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharePublicBean sharedBean = new Gson().fromJson(config, SharePublicBean.class);
                if (sharedBean.isShow()) {
                    if (!SykscApplication.mWxApi.isWXAppInstalled()) {
                        ivRightIcon.setVisibility(View.GONE);
                    } else {
                        ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
                        ivRightIcon.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                SharePopupUtil.showSharedPop(config, activity, view);
                            }
                        });
                    }
                } else {
                    ivRightIcon.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 顶部管理按钮显示隐藏
     */
    @JavascriptInterface
    public void headBtn(final String title, final String name, final String type, final String funname) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCommonTitle.setText(title);
                if (type.equals("0")) {
                    //不显示
                    tvRight.setVisibility(View.GONE);
                } else if (type.equals("1")) {
                    //显示
                    tvRight.setVisibility(View.VISIBLE);
                    tvRight.setText(name);
                    tvRight.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            webView.loadUrl("javascript:" + funname + "()");
                        }
                    });
                }
            }
        });
    }

    @JavascriptInterface
    public String sendKey() {
        AuthHeader authHeader = new AuthHeader();
        authHeader.setAuthorization(AppConst.TOKEN);
        authHeader.setSycm(AppConst.sycm());
        return JSON.toJSONString(authHeader);
    }

    /**
     * h5页面调用网络出现错误码
     */
    @JavascriptInterface
    public void newLoginDate(final String err, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ErrorCodeTools.errorCodePrompt(activity, err, msg);
            }
        });
    }

    /**
     * 调起本地地图
     */
    @JavascriptInterface
    public void gotomap(String name, String longitude, String latitude) {
        Longitude = longitude;
        Latitude = latitude;
        Name = name;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (JumpTomap.isAvilible(activity, "com.tencent.map")
                        || JumpTomap.isAvilible(activity, "com.baidu.BaiduMap")
                        || JumpTomap.isAvilible(activity, "com.autonavi.minimap")) {
                    showToMap();
                } else {
                    showDialog();
                }
            }
        });
    }

    /**
     * 登录返回的token需要传给H5
     *
     * @return
     */
    @JavascriptInterface
    public String answerLogin() {
        String answerHome = null;
        if (!EncodeAndStringTool.isObjectEmpty(answerHomeBean)) {
            answerHome = JSON.toJSONString(answerHomeBean);
        }
        Log.e(TAG, answerHome);
        return answerHome;
    }

    /**
     * 我的奖品开宝箱
     *
     * @return
     */
    @JavascriptInterface
    public String prizeData() {
        String prizeBox = null;
        if (!EncodeAndStringTool.isStringEmpty(main_box) && "main_box".equals(main_box)) {
            prizeBox = JSON.toJSONString(boxBean);
        } else {
            if (!EncodeAndStringTool.isObjectEmpty(minePrize)) {
                prizeBox = JSON.toJSONString(minePrize);
            }
        }
        Log.i(TAG, prizeBox);
        return prizeBox;
    }


    /**
     * 提现成功
     */
    @JavascriptInterface
    public void goWebview(final String h5url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(h5url);
            }
        });
    }


    /**
     * JS调用此方法,关闭H5页面进入首页面
     */
    @JavascriptInterface
    public void goMain() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                Intent intent = new Intent(activity, HomePageActivity.class);
                intent.putExtra("from", "h5");
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 提现失败，进入我的账户
     */
    @JavascriptInterface
    public void goAccount(final String err) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.startActivity(new Intent(activity, CashRecordActivity.class));
            }
        });
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
     * 接收Js传的参数调用分享业务
     *
     * @param shareMode
     */
    @JavascriptInterface
    public void Share(String shareMode) {
        Log.i(TAG, "Share: " + shareMode);
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }


    /**
     * 公共分享方法
     */
    @JavascriptInterface
    public void doShare(final String config) {
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharePopupUtil.showSharedPop(config, activity, view);
                }
            });
        }
    }

    /**
     * 接收js返回的手机号码,调用发短信业务
     *
     * @param phoneNum
     */
    @JavascriptInterface
    public void sms(String phoneNum) {
        //调用发短信业务
        doSendSMSTo(phoneNum);
    }


    /**
     * 返回弹框h5调用方法
     */
    @JavascriptInterface
    public void pageLoad(String data) {
        returnDialogBean = new Gson().fromJson(data, ReturnDialogBean.class);
        show = returnDialogBean.isShow();
        Log.e("data", data);
    }

    /**
     * 与js交互跳转到不同界面
     */
    @JavascriptInterface
    public void doJump(String data) {
        final H5JumpBean h5JumpBean = new Gson().fromJson(data, H5JumpBean.class);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginJumpUtil.dialogSkip(h5JumpBean.getBtnAction(),
                        activity,
                        h5JumpBean.getContent(),
                        h5JumpBean.getH5Url(),
                        Long.valueOf(0));
            }
        });
        Log.e("data", data);
    }

    /**
     * 开宝箱奖品跳转
     */
    @JavascriptInterface
    public void gotoprize(final String prizeData) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("奖品返回", prizeData);
//                    MinePrize minePrize = JSONObject.parseObject(prizeData.toString(), MinePrize.class);
                MinePrize minePrize = new Gson().fromJson(prizeData, MinePrize.class);
                if (minePrize.getActionType().equals("action.h5.url")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(activity).getUserInfo("cert")) == 1) {
                        //实物
                        Intent intent = new Intent(activity, WebH5Activity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        activity.startActivity(intent);
                    } else {
                        //显示实名认证弹窗
                        RealNamePopupUtil.showAuthPop(activity, view, activity.getString(R.string.real_gift_describe));
                    }
                } else if (minePrize.getActionType().equals("action.withdraw")) {
                    //红包
                    if (Integer.parseInt(SaveUserInfo.getInstance(activity).getUserInfo("cert")) == 1) {
                        if (!EncodeAndStringTool.isListEmpty(minePrize.getMinePrizes())) {
                            List<MinePrize.ChildMinePrize> redPrizeList = new ArrayList<>();
                            for (int i = 0; i < minePrize.getMinePrizes().size(); i++) {
                                MinePrize.ChildMinePrize childMinePrize1 = minePrize.getMinePrizes().get(i);
                                if (minePrize.getMinePrizes().get(i).getActionType().equals("action.withdraw")) {
                                    redPrizeList.add(childMinePrize1);
                                }
                            }
                            if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                                Intent intent = new Intent(activity, NewRedWithdrawActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("redPrize", (ArrayList<? extends Parcelable>) redPrizeList);
                                bundle.putString("redId", minePrize.getId());
                                bundle.putString("from", "box");
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                            }
                        }
                    } else {
                        //显示实名认证弹窗
                        RealNamePopupUtil.showAuthPop(activity, view, activity.getString(R.string.real_gift_describe));
                    }
                } else if (minePrize.getActionType().equals("action.bp.use")) {
                    //积分
                    activity.startActivity(new Intent(activity, IntegralExchangeActivity.class));
                } else if (minePrize.getActionType().equals("action.alipay.coupon")) {
                    //优惠券
                    if (Integer.parseInt(SaveUserInfo.getInstance(activity).getUserInfo("cert")) == 1) {
                        //调用使用优惠券接口
                        RemotingEx.doRequest(ApiServiceBean.useCoupon(), new Object[]{minePrize.getId()}, null);
                        AlipayTradeManager.instance().showBasePage(activity, minePrize.getH5Url());
                    } else {
                        //显示实名认证弹窗
                        RealNamePopupUtil.showAuthPop(activity, view, activity.getString(R.string.real_gift_describe));
                    }
                }
            }
        });

    }

    /**
     * 接收js返回的规则
     *
     * @param
     */
    @JavascriptInterface
    public String actRule() {
        return bulletin;
    }

    /**
     * 倒计时结束布局设置成开奖信息
     */
    @JavascriptInterface
    public void overTime() {
        try {
            //已开奖
            SaveUserInfo.getInstance(activity).setUserInfo("ScheduleStatus", "1");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_look_result.setVisibility(View.VISIBLE);
                    ll_exchange.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {

        }
    }

    /**
     * 答题h5调用app登录
     */
    @JavascriptInterface
    public void jsLogin(String data) {
        final JSONObject rel = JSON.parseObject(data);
        if ("exam".equals(rel.getString("action"))) {
            LoginDataManager.instance().addData(LoginDataManager.ANSWER_LOGIN, new JSONObject());
            //答题
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startActivityForResult(new Intent(activity, LoginActivity.class).putExtra("examId", rel.getString("examId")), 1);
                }
            });
        } else if ("practice".equals(rel.getString("action"))) { //练习场登录
            LoginDataManager.instance().addData(LoginDataManager.PRACTICE_LOGIN, new JSONObject());
            //答题
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startActivityForResult(new Intent(activity, LoginActivity.class), 2);
                }
            });
        }
    }

    /**
     * h5调用是否实名认证
     */
    @JavascriptInterface
    public boolean jsCertState() {
        if (Integer.parseInt(SaveUserInfo.getInstance(activity).getUserInfo("cert")) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 实名认证弹框
     */
    @JavascriptInterface
    public void jsCertDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RealNamePopupUtil.showAuthPop(activity, view, activity.getString(R.string.real_openbox_describe));
            }
        });
    }


    /**
     * 调用系统已有程序发短信功能
     *
     * @param phoneNumber
     */
    public void doSendSMSTo(String phoneNumber) {
        String[] smsInfo = phoneNumber.split("[$]");
        if (PhoneNumberUtils.isGlobalPhoneNumber(smsInfo[0])) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + smsInfo[0]));
            intent.putExtra("sms_body", smsInfo[1]);
            activity.startActivity(intent);
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    /**
                     * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
                     */
                    if (!SykscApplication.mWxApi.isWXAppInstalled()) {

                    } else {
                        InviteSharePopupUtil.showSharedPop(activity, view);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 调用打电话业务
     *
     * @param phoneNum
     */
    private void call(final String phoneNum) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                PhoneUtils.dial(phoneNum);
            }
        });
    }

    /**
     * 调用本机地图弹窗
     */
    private CommonPopupWindow popupWindow;

    public void showToMap() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(activity).inflate(R.layout.share_map_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(activity)
                .setView(R.layout.share_map_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style_bottom)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.share_map_popupwindow:
                TextView tv_tencent = view.findViewById(R.id.tv_tencent);
                TextView tv_baidu = view.findViewById(R.id.tv_baidu);
                TextView tv_gaode = view.findViewById(R.id.tv_gaode);
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                if (JumpTomap.isAvilible(activity, "com.tencent.map")) {
                    //腾讯地图
                    tv_tencent.setVisibility(View.VISIBLE);
                    tv_tencent.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToTencent(activity, Name, Latitude, Longitude);
                        }
                    });
                } else {
                    tv_tencent.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(activity, "com.baidu.BaiduMap")) {
                    //百度地图
                    tv_baidu.setVisibility(View.VISIBLE);
                    tv_baidu.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToBaidu(activity, Name);
                        }
                    });
                } else {
                    tv_baidu.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(activity, "com.autonavi.minimap")) {
                    //高德地图
                    tv_gaode.setVisibility(View.VISIBLE);
                    tv_gaode.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToGaode(activity, Name);
                        }
                    });
                } else {
                    tv_gaode.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


    private void showDialog() {
        new CircleDialog.Builder((FragmentActivity) activity)
                .setTitle("提示")
                .setText("您好，未在您的手机中检测到主流地图类App，安装主流地图App后，将打开地图自动导航至目的地")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }

    //webview能返回退出弹框
    public static void WebViewCanBack() {
        if (show) {
            exitDialog(returnDialogBean);
        } else {
            webView.goBack();
        }
    }

    //webview不能返回退出弹框
    public static void WebViewNoBack() {
        if (show) {
            exitDialog(returnDialogBean);
        } else {
            activity.finish();
        }
    }

    /**
     * 中途退出弹窗
     */
    private static void exitDialog(final ReturnDialogBean returnDialogBean) {
        new CircleDialog.Builder((FragmentActivity) activity)
                .setTitle(returnDialogBean.getTitle())
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = 40;
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setText(returnDialogBean.getContent())
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textSize = 40;
                        params.textColor = Color.parseColor("#666666");
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative(returnDialogBean.getBtns().get(0).getLabel(), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        String action = returnDialogBean.getBtns().get(0).getAction();
                        if ("return.previous.page".equals(action)) {
                            //返回上一页
                            if (webView.canGoBack()) {
                                webView.goBack();
                            } else {
                                activity.finish();
                            }
                        } else if ("continue.to.perform".equals(action)) {
                            //继续执行

                        } else if ("determined.to.leave".equals(action)) {
                            //确定离开
                            activity.finish();
                        } else if ("open.new.page".equals(action)) {
                            //新开页面
                            webView.loadUrl(returnDialogBean.getBtns().get(0).getH5Url());
                        }
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
                    }
                })
                .setPositive(returnDialogBean.getBtns().get(1).getLabel(), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        String action = returnDialogBean.getBtns().get(1).getAction();
                        if ("return.previous.page".equals(action)) {
                            //返回上一页
                            if (webView.canGoBack()) {
                                webView.goBack();
                            } else {
                                activity.finish();
                            }
                        } else if ("continue.to.perform".equals(action)) {
                            //继续执行
                        } else if ("determined.to.leave".equals(action)) {
                            //确定离开
                            activity.finish();
                        } else if ("open.new.page".equals(action)) {
                            //新开页面
                            webView.loadUrl(returnDialogBean.getBtns().get(1).getH5Url());
                        }
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#0194ec");
                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }


}
