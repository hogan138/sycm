package com.shuyun.qapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnyPositionBean;
import com.shuyun.qapp.utils.GlideUtils;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: AnyPositionImgManage
 * @Description: 任意位置imageview
 * @Author: ganquan
 * @CreateDate: 2019/1/7 10:38
 */
public class AnyPositionImgManage {

    //执行

    /**
     *
     * @param anyPositionBean
     * @param rootView
     * @param context 被销毁时会报异常
     */
    public static void execute(AnyPositionBean anyPositionBean, RelativeLayout rootView, Context context) {
        try {
            rootView.removeAllViews();
            //获取布局宽高
            rootView.requestLayout();
            int rH = rootView.getMeasuredHeight();
            int rW = rootView.getMeasuredWidth();

            Long location = anyPositionBean.getLocation(); //位置
            Long shadow = anyPositionBean.getShadow(); //是否有阴影

            //初始化数据
            //图片间距
            String[] ps = anyPositionBean.getPadding().split(",");
            int[] padds = new int[]{Integer.valueOf(ps[0]), Integer.valueOf(ps[1]), Integer.valueOf(ps[2]), Integer.valueOf(ps[3])};

            //广告间距
            String[] ps1 = anyPositionBean.getMargin().split(",");
            int[] layout_margin = new int[]{Integer.valueOf(ps1[0]), Integer.valueOf(ps1[1]), Integer.valueOf(ps1[2]), Integer.valueOf(ps1[3])};
            int x = 0, y = 0, w = (int) dp2px(context, anyPositionBean.getWidth() + padds[0] + padds[2]), h = (int) dp2px(context, anyPositionBean.getHeight() + padds[1] + padds[3]);
            //计算xy
            if (location == 1) {//左上角
                x = (int) dp2px(context, layout_margin[0]);
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 2) {//左上中
                x = (rW - w) / 2;
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 3) {//右上角
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (int) dp2px(context, layout_margin[1]);
            } else if (location == 4) {//左中
                x = (int) dp2px(context, layout_margin[0]);
                y = (rH - h) / 2;
            } else if (location == 5) {//中间
                x = (rW - w) / 2;
                y = (rH - h) / 2;
            } else if (location == 6) {//右中
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (rH - h) / 2;
            } else if (location == 7) {//左下角
                x = (int) dp2px(context, layout_margin[0]);
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            } else if (location == 8) {//左下中
                x = (rW - w) / 2;
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            } else if (location == 9) {//右下角
                x = (int) (rW - w - dp2px(context, layout_margin[2]));
                y = (int) (rH - h - dp2px(context, layout_margin[3]));
            }

            View childView = LayoutInflater.from(context).inflate(R.layout.item_any_position_img, null);
            RelativeLayout layoutView = childView.findViewById(R.id.layout);
            //设置位置
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            params.leftMargin = x;
            params.topMargin = y;
            layoutView.setLayoutParams(params);

            ImageView imageView = childView.findViewById(R.id.image);
            GlideUtils.LoadImage(context, anyPositionBean.getImageUrl(), imageView);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            imageParams.setMargins((int) dp2px(context, padds[0]), (int) dp2px(context, padds[1]), (int) dp2px(context, padds[2]), (int) dp2px(context, padds[3]));
            imageView.setLayoutParams(imageParams);

            //设置阴影
            RelativeLayout shadowView = childView.findViewById(R.id.shadow);
            if (shadow == 1) {
                shadowView.setVisibility(View.VISIBLE);
                GradientDrawable drawable = new GradientDrawable();
                String[] ds = anyPositionBean.getShadowRadius().split(",");
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                float[] fs = new float[]{dp2px(context, Float.valueOf(ds[0])),
                        dp2px(context, Float.valueOf(ds[0])),
                        dp2px(context, Float.valueOf(ds[1])),
                        dp2px(context, Float.valueOf(ds[1])),
                        dp2px(context, Float.valueOf(ds[2])),
                        dp2px(context, Float.valueOf(ds[2])),
                        dp2px(context, Float.valueOf(ds[3])),
                        dp2px(context, Float.valueOf(ds[3]))};
                drawable.setCornerRadii(fs);
                drawable.setColor(Color.parseColor(anyPositionBean.getShadowColor()));
                shadowView.setBackground(drawable);
                shadowView.setAlpha(Float.valueOf(anyPositionBean.getShadowAlpha()));

            } else {
                shadowView.setVisibility(View.GONE);
            }

            rootView.addView(childView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
