package com.shuyun.qapp.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.FloatWindowBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.GlideUtils;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: FloatImageviewManage
 * @Description: 作用描述
 * @Author: ganquan
 * @CreateDate: 2019/4/2 14:05
 */
public class FloatImageviewManage {

    static ImageView imageView; //浮窗图片

    //添加图片
    public static void execute(final FloatWindowBean anyPositionBean, RelativeLayout rootView, final Activity context) {
        try {
            rootView.removeAllViews();
            //获取布局宽高
            rootView.requestLayout();
            int rH = rootView.getMeasuredHeight();
            int rW = rootView.getMeasuredWidth();

            int location = 6; //位置
            Long shadow = anyPositionBean.getShadow(); //是否有阴影

            //初始化数据
            //图片间距
            String[] ps = anyPositionBean.getPadding().split(",");
            int[] padds = new int[]{Integer.valueOf(ps[0]), Integer.valueOf(ps[1]), Integer.valueOf(ps[2]), Integer.valueOf(ps[3])};

            //广告间距
            String[] ps1 = anyPositionBean.getMargin().split(",");
            int[] layout_margin = new int[]{Integer.valueOf(ps1[0]), Integer.valueOf(ps1[1]), Integer.valueOf(ps1[2]), Integer.valueOf(ps1[3])};
            int x = 0, y = 0, w = (int) dp2px(anyPositionBean.getWidth() + padds[0] + padds[2]), h = (int) dp2px(anyPositionBean.getHeight() + padds[1] + padds[3]);
            //计算xy
            if (location == 1) {//左上角
                x = (int) dp2px(layout_margin[0]);
                y = (int) dp2px(layout_margin[1]);
            } else if (location == 2) {//左上中
                x = (rW - w) / 2;
                y = (int) dp2px(layout_margin[1]);
            } else if (location == 3) {//右上角
                x = (int) (rW - w - dp2px(layout_margin[2]));
                y = (int) dp2px(layout_margin[1]);
            } else if (location == 4) {//左中
                x = (int) dp2px(layout_margin[0]);
                y = (rH - h) / 2;
            } else if (location == 5) {//中间
                x = (rW - w) / 2;
                y = (rH - h) / 2;
            } else if (location == 6) {//右中
                x = (int) (rW - w - dp2px(layout_margin[2]));
                y = (rH - h) / 2;
            } else if (location == 7) {//左下角
                x = (int) dp2px(layout_margin[0]);
                y = (int) (rH - h - dp2px(layout_margin[3]));
            } else if (location == 8) {//左下中
                x = (rW - w) / 2;
                y = (int) (rH - h - dp2px(layout_margin[3]));
            } else if (location == 9) {//右下角
                x = (int) (rW - w - dp2px(layout_margin[2]));
                y = (int) (rH - h - dp2px(layout_margin[3]));
            }

            View childView = LayoutInflater.from(context).inflate(R.layout.item_any_position_img, null);
            RelativeLayout layoutView = childView.findViewById(R.id.layout);
            //设置位置
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            params.leftMargin = x;
            params.topMargin = y;
            layoutView.setLayoutParams(params);

            imageView = childView.findViewById(R.id.image);
            GlideUtils.LoadImage(context, anyPositionBean.getPicture(), imageView);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            imageParams.setMargins((int) dp2px(padds[0]), (int) dp2px(padds[1]), (int) dp2px(padds[2]), (int) dp2px(padds[3]));
            imageView.setLayoutParams(imageParams);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String action = anyPositionBean.getAction();
                    String content = anyPositionBean.getContent();
                    String h5Url = anyPositionBean.getH5Url();
                    if (AppConst.H5.equals(action) || AppConst.INVITE.equals(action)) {
                        Intent intent = new Intent(context, WebH5Activity.class);
                        intent.putExtra("url", h5Url);
                        intent.putExtra("from", "home");
                        intent.putExtra("name", "全名共进");//名称 标题
                        context.startActivity(intent);
                    } else {
                        LoginJumpUtil.dialogSkip(action, context, content, h5Url, (long) 0);
                    }

                }
            });

            //设置阴影
            RelativeLayout shadowView = childView.findViewById(R.id.shadow);
            if (shadow == 1) {
                shadowView.setVisibility(View.VISIBLE);
                GradientDrawable drawable = new GradientDrawable();
                String[] ds = anyPositionBean.getShadowRadius().split(",");
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                float[] fs = new float[]{dp2px(Float.valueOf(ds[0])),
                        dp2px(Float.valueOf(ds[0])),
                        dp2px(Float.valueOf(ds[1])),
                        dp2px(Float.valueOf(ds[1])),
                        dp2px(Float.valueOf(ds[2])),
                        dp2px(Float.valueOf(ds[2])),
                        dp2px(Float.valueOf(ds[3])),
                        dp2px(Float.valueOf(ds[3]))};
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

    //恢复显示
    public static void showImageview() {
        try {
            Animation anim = new RotateAnimation(-90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            TranslateAnimation translateAnim = new TranslateAnimation(Animation.ABSOLUTE, dp2px(35), Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(anim);
            set.addAnimation(translateAnim);
            set.setFillAfter(true);// 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            translateAnim.setDuration(500);
            imageView.startAnimation(set);
        } catch (Exception e) {

        }
    }

    //靠边隐藏
    public static void hideImageview() {
        try {
            Animation anim = new RotateAnimation(0f, -90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            TranslateAnimation translateAnim = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dp2px(35), Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(anim);
            set.addAnimation(translateAnim);
            set.setFillAfter(true);// 设置保持动画最后的状态
            anim.setDuration(500); // 设置动画时间
            translateAnim.setDuration(500);
            imageView.startAnimation(set);
        } catch (Exception e) {

        }
    }

}
