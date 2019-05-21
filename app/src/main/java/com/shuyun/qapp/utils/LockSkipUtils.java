package com.shuyun.qapp.utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.view.ActionJumpUtil;

/**
 * @Package: com.shuyun.qapp.utils
 * @ClassName: LockSkipUtils
 * @Description: 奖品锁弹框
 * @Author: ganquan
 * @CreateDate: 2019/5/15 11:48
 */
public class LockSkipUtils {
    //lock弹框
    public static void ExchangeTipDialog(final Activity activity, final MinePrize minePrize) {
        new CircleDialog.Builder((FragmentActivity) activity)
                .setTitle("提示")
                .setText(minePrize.getLockConfig().getMsg())
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#9B9B9B");
                    }
                })
                .setPositive(minePrize.getLockConfig().getButton(), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        try {
                            String action = minePrize.getLockConfig().getAction();
                            String content = minePrize.getLockConfig().getContent();
                            String h5Url = minePrize.getLockConfig().getH5Url();
                            ActionJumpUtil.dialogSkip(action, activity, content, h5Url, 0L);
                        } catch (Exception e) {

                        }
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
