package com.shuyun.qapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: AddImageviewManage
 * @Description: 任意位置添加imagevieww
 * @Author: ganquan
 * @CreateDate: 2018/12/28 9:59
 */
public class AddImageviewManage {


    //添加logo
    @SuppressLint("NewApi")
    public static void addLogo(RelativeLayout rl, int position, Context context) {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = dp2px(60);
        layoutParams.height = dp2px(25);
        int padding = dp2px(5);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.pahys_logo);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        /**
         * 外部矩形弧度
         */
        int LeftTopRadio = dp2px(8);
        int RightTopRadio = dp2px(8);
        int RightBottomRadio = dp2px(8);
        int LeftBottomRadio = dp2px(8);
        float[] outerRadian = new float[]{LeftTopRadio, LeftTopRadio, RightTopRadio, RightTopRadio, RightBottomRadio, RightBottomRadio, LeftBottomRadio, LeftBottomRadio};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadian, null, null);
        ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
        drawable.getPaint().setColor(Color.parseColor("#80333333"));
        drawable.getPaint().setStyle(Paint.Style.FILL);
        imageView.setBackground(drawable);
        rl.removeAllViews();
        if (position == 0) {
        } else if (position == 1) {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (position == 2) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else if (position == 3) {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (position == 4) {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if (position == 5) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (position == 6) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (position == 7) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (position == 8) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        if (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || position == 7 || position == 8) {
            imageView.setLayoutParams(layoutParams);
            rl.addView(imageView);
        }


//        GradientDrawable gd = new GradientDrawable();//创建drawable
//        gd.setColor(Color.parseColor("#80333333"));//内部填充颜色
//        gd.setCornerRadius(30);//  圆角半径

//        imageView.setBackgroundDrawable(gd);

    }

}
