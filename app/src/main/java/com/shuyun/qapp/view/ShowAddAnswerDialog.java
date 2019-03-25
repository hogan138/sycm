package com.shuyun.qapp.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: ShowAddAnswerDialog
 * @Description: 增加答题次数弹框
 * @Author: ganquan
 * @CreateDate: 2019/3/25 9:30
 */
public class ShowAddAnswerDialog {

    private static CommonPopupWindow popupWindow;
    private static TextView tvRemainderTime;
    private static Button btnGetImmedicate;
    private static ImageView add_answernum_logo;
    private static CountDownTimer timer;

    private static String remainderTime;
    private static AnswerOpptyBean answerOpptyBean;

    /**
     * 增加答题次数弹窗
     */
    public static void showAddAnswerNum(final Context mContext, View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.add_answer_num_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.add_answer_num_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.add_answer_num_popupwindow:
                                ImageView ivClose0 = view.findViewById(R.id.iv_close_icon0);
                                add_answernum_logo = view.findViewById(R.id.iv_logo);
                                btnGetImmedicate = view.findViewById(R.id.btn_get_immedicate);
                                tvRemainderTime = view.findViewById(R.id.tv_remainder_time);
                                loadAnswerOpptyRemainder();
                                ivClose0.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (popupWindow != null && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                            try {
                                                timer.cancel();
                                            } catch (Exception e) {

                                            }
                                        }
                                    }
                                });
                                btnGetImmedicate.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (NetworkUtils.isAvailableByPing()) {
                                            loadAnswerOppty();
                                        } else {
                                            Toast.makeText(mContext, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    /**
     * 答题机会领取
     * U0004  答题机会已到上限
     */
    private static void loadAnswerOpptyRemainder() {
        RemotingEx.doRequest(ApiServiceBean.getAnswerOpptyRemainder(), null, new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<String> dataResponse) {
                remainderTime = (String) dataResponse.getDat();
                if (!EncodeAndStringTool.isStringEmpty(remainderTime)) {
                    if (remainderTime.equals("0")) {
                        btnGetImmedicate.setEnabled(true);
                        add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_s);
                    } else {
                        btnGetImmedicate.setEnabled(false);
                        add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_n);
                        long time = Long.parseLong(remainderTime);
                        countDown(time);
                    }
                }
            }
        });
    }

    /**
     * 领取答题机会
     * U0005
     */
    private static void loadAnswerOppty() {
        RemotingEx.doRequest(ApiServiceBean.getAnswerOppty(), null, new OnRemotingCallBackListener<AnswerOpptyBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<AnswerOpptyBean> dataResponse) {
                answerOpptyBean = (AnswerOpptyBean) dataResponse.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(answerOpptyBean)) {
                    btnGetImmedicate.setEnabled(false);
                    add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_n);
                    answerOpptyBean.getRemainder();
                    countDown(answerOpptyBean.getRemainder());
                }
            }
        });
    }

    /**
     * 领取答题机会倒计时
     *
     * @param remainderTime
     */
    private static void countDown(long remainderTime) {
        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(remainderTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);//初始化Formatter的转换格式。
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished);
                String time = "需等待" + hms + "后获取次数";
                tvRemainderTime.setText(time);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
