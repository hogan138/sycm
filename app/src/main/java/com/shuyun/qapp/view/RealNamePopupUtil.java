package com.shuyun.qapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.OnMultiClickListener;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 实名认证弹框
 */
public class RealNamePopupUtil {
    private static CommonPopupWindow popupWindow;

    /**
     * 实名认证popupWindow
     */
    public static void showAuthPop(final Context context, View view, final String describe) {
        if (popupWindow != null && popupWindow.isShowing())
            return;
        View upView = LayoutInflater.from(context).inflate(R.layout.real_name_auth_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.real_name_auth_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.real_name_auth_popupwindow:
                                ImageView ivClose1 = view.findViewById(R.id.iv_close_icon1);
                                TextView tv_hint = view.findViewById(R.id.tv_hint);
                                tv_hint.setText(describe);
                                Button btnRealNameAuth = view.findViewById(R.id.btn_real_name_auth1);
                                ivClose1.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (popupWindow != null && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                            popupWindow = null;
                                        }
                                    }
                                });
                                btnRealNameAuth.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (popupWindow != null && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                            popupWindow = null;
                                        }
                                        startActivity(new Intent(context, RealNameAuthActivity.class));
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
}
