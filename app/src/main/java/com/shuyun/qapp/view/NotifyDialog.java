package com.shuyun.qapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.utils.OnMultiClickListener;

/**
 * 首页通知弹框
 */
public class NotifyDialog {

    //开启通知弹出框
    public static void dialogShow(final Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.open_notification_popup, null);
        RelativeLayout rl_close = view.findViewById(R.id.rl_close);
        TextView tvOpenNotification = view.findViewById(R.id.tv_open_notification);
        final Dialog dialog = builder.create();
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
        tvOpenNotification.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);

                        localIntent.setClassName("com.android.settings",
                                "com.android.settings.InstalledAppDetails");

                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                mContext.getPackageName());
                    }
                    mContext.startActivity(localIntent);
                }
            }
        });
    }
}
