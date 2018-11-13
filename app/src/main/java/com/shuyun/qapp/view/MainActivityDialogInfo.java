package com.shuyun.qapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;

/**
 * 首页活动弹框
 */
public class MainActivityDialogInfo {

    public static void info(ConfigDialogBean configDialogBean, Context mContext, View view) {
        if (AppConst.GROUP.equals(configDialogBean.getBtnAction())) {
            //题组
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.group_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.group_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.group_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.group_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.group_count")) + 1) + "");
                }
            }
        } else if (AppConst.REAL.equals(configDialogBean.getBtnAction())) {
            //实名认证
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.real_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.real_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.real_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.real_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.real_count")) + 1) + "");
                }
            }
        } else if (AppConst.H5.equals(configDialogBean.getBtnAction())) {
            //h5页面
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.h5_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.h5_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.h5_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.h5_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.h5_count")) + 1) + "");
                }
            }

        } else if (AppConst.INVITE.equals(configDialogBean.getBtnAction())) {
            //邀请
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.invite_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.invite_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.invite_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.invite_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.invite_count")) + 1) + "");
                }
            }
        } else if (AppConst.INTEGRAL.equals(configDialogBean.getBtnAction())) {
            //积分兑换
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.integral_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.integral_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.integral_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral_count")) + 1) + "");
                }
            }
        } else if (AppConst.AGAINST.equals(configDialogBean.getBtnAction())) {
            //答题对战
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.answer.against_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.answer.against_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.answer.against_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.answer.against_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.answer.against_count")) + 1) + "");
                }
            }
        } else if (AppConst.OPEN_BOX.equals(configDialogBean.getBtnAction())) {
            //积分开宝箱
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.open.box_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.integral.open.box_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.open.box_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.integral.open.box_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.open.box_count")) + 1) + "");
                }
            }
        } else if (AppConst.TREASURE.equals(configDialogBean.getBtnAction())) {
            //积分夺宝
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.treasure_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.integral.treasure_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.treasure_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.integral.treasure_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral.treasure_count")) + 1) + "");
                }
            }
        } else if (AppConst.TASK.equals(configDialogBean.getBtnAction())) {
            //每日任务
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.day.task_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.day.task_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.day.task_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.day.task_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.day.task_count")) + 1) + "");
                }
            }
        } else if (AppConst.WITHDRAW_INFO.equals(configDialogBean.getBtnAction())) {
            //提现信息
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.withdraw.info_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.withdraw.info_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.withdraw.info_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.withdraw.info_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.withdraw.info_count")) + 1) + "");
                }
            }
        } else if (AppConst.H5_EXTERNAL.equals(configDialogBean.getBtnAction())) {
            //跳转外部h5
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.h5.external_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.h5.external_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.h5.external_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.h5.external_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.h5.external_count")) + 1) + "");
                }
            }
        } else if (AppConst.DEFAULT.equals(configDialogBean.getBtnAction())) {
            //默认不跳转
            if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.default_count").equals("")) {
                activitydialog(configDialogBean,mContext,view);
                SaveUserInfo.getInstance(mContext).setUserInfo("action.default_count", "" + 1);
            } else {
                if (configDialogBean.getCount() > Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.default_count"))) {
                    activitydialog(configDialogBean,mContext,view);
                    SaveUserInfo.getInstance(mContext).setUserInfo("action.default_count", (Integer.decode(SaveUserInfo.getInstance(mContext).getUserInfo("action.default_count")) + 1) + "");
                }
            }
        }
    }


    //活动弹框
    static Dialog dialog;

    private static void activitydialog(final ConfigDialogBean configDialogBean, final Context mContext, final View llHomeFragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.open_activity_popup, null);
        RoundImageView iv_bg = view.findViewById(R.id.iv_bg);
        RelativeLayout rl_close = view.findViewById(R.id.rl_close);
        RelativeLayout rl_all = view.findViewById(R.id.rl_open_notification);
        Button btn_enter = view.findViewById(R.id.btn_enter);
        try {
            //背景图
            ImageLoaderManager.LoadImage(mContext, configDialogBean.getBaseImage(), iv_bg, R.mipmap.zw01);
            if (configDialogBean.isShowBtn() == true) {
                //显示按钮
                btn_enter.setVisibility(View.VISIBLE);
                btn_enter.setText(configDialogBean.getBtnLabel());
            } else {
                //隐藏按钮
                btn_enter.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
        dialog = builder.create();

        if (dialog.isShowing()) {
            return;
        }

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        rl_close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btn_enter.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    try {
                        H5JumpUtil.dialogSkip(configDialogBean.getBtnAction(), configDialogBean.getContent(), configDialogBean.getH5Url(), mContext, llHomeFragment);
                    } catch (Exception e) {

                    }
                }
            }
        });
        rl_all.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    try {
                        H5JumpUtil.dialogSkip(configDialogBean.getBtnAction(), configDialogBean.getContent(), configDialogBean.getH5Url(), mContext, llHomeFragment);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
}
