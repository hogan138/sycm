package com.shuyun.qapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;

/**
 * 首页活动弹框
 */
public class MainActivityDialogInfo {
    //活动弹框
    private static Dialog dialog = null;
    private static View view = null;

    public static void info(ConfigDialogBean configDialogBean, Activity mContext) {
        if (configDialogBean.getCount() == null || configDialogBean.getCount() == 0)
            return;
        String action = configDialogBean.getBtnAction();
        Long beanCount = configDialogBean.getCount();
        Integer _count = 1;
        String bName = null;
        if (AppConst.GROUP.equals(action)) {//题组
            bName = "action.group_count";
        } else if (AppConst.REAL.equals(action)) {//实名认证
            bName = "action.real_count";
        } else if (AppConst.H5.equals(action)) {//h5页面
            bName = "action.h5_count";
        } else if (AppConst.INVITE.equals(action)) { //邀请
            bName = "action.invite_count";
        } else if (AppConst.INTEGRAL.equals(action)) {//积分中心
            bName = "action.integral_count";
        } else if (AppConst.AGAINST.equals(action)) {//答题对战
            bName = "action.answer.against_count";
        } else if (AppConst.OPEN_BOX.equals(action)) {//积分开宝箱
            bName = "action.integral.open.box_count";
        } else if (AppConst.TREASURE.equals(action)) {//积分夺宝
            bName = "action.integral.treasure_count";
        } else if (AppConst.TASK.equals(action)) { //每日任务
            bName = "action.day.task_count";
        } else if (AppConst.WITHDRAW_INFO.equals(action)) { //提现信息
            bName = "action.withdraw.info_count";
        } else if (AppConst.H5_EXTERNAL.equals(action)) {//跳转外部h5
            bName = "action.h5.external_count";
        } else if (AppConst.DEFAULT.equals(action)) { //默认不跳转
            bName = "action.default_count";
        } else if (AppConst.ACTION_SIGN.equals(action)) { //签到
            bName = "action.sign_count";
        } else if (AppConst.ACTION_INTEGRAL_GOODS.equals(action)) { //积分兑换
            bName = "action.integral_good_count";
        }

        if (bName == null)
            return;
        String localKey = bName + "_" + configDialogBean.getId();
        String count = SaveUserInfo.getInstance(mContext).getUserInfo(localKey);
        if ("".equals(count))
            _count = 0;
        else
            _count = Integer.valueOf(count);
        if (beanCount > _count) {
            activitydialog(configDialogBean, mContext);
            _count += 1;
            SaveUserInfo.getInstance(mContext).setUserInfo(localKey, _count.toString());
        }
    }

    private static void activitydialog(final ConfigDialogBean configDialogBean, final Activity mContext) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.open_activity_popup, null);
        view.setTag(configDialogBean);
        RoundImageView iv_bg = view.findViewById(R.id.iv_bg);
        RelativeLayout rl_close = view.findViewById(R.id.rl_close);
        RelativeLayout rl_all = view.findViewById(R.id.rl_open_notification);
        Button btn_enter = view.findViewById(R.id.btn_enter);
        try {
            //背景图
            ImageLoaderManager.LoadImage(mContext, configDialogBean.getBaseImage(), iv_bg, R.mipmap.zw01);
            if (configDialogBean.isShowBtn()) {
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

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        rl_close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                    view = null;
                }
            }
        });
        btn_enter.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialogSkip(view, mContext);
                    dialog = null;
                    view = null;
                }
            }
        });
        rl_all.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialogSkip(view, mContext);
                    dialog = null;
                    view = null;
                }
            }
        });
    }

    /**
     * 登录或跳转
     *
     * @param view
     */
    private static void dialogSkip(View view, Activity context) {
        ConfigDialogBean selectedItem = (ConfigDialogBean) view.getTag();
        LoginDataManager.instance().addData(LoginDataManager.HOME_DIALOG_LOGIN, selectedItem);
        final String action = selectedItem.getBtnAction(); //跳转action
        final String h5Url = selectedItem.getH5Url(); //跳转地址
        final String content = selectedItem.getContent();//题组id
        final Long isLogin = selectedItem.getIsLogin();//是否需要登录
        ActionJumpUtil.dialogSkip(action, context, content, h5Url, isLogin);
    }

}
