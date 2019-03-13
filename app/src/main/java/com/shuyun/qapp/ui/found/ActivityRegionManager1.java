package com.shuyun.qapp.ui.found;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.shuyun.qapp.view.RoundImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 发现活动区域封装
 */
public class ActivityRegionManager1 {

    public static RelativeLayout getView(final Activity context, FoundDataBean.RegionBean data, final View layout) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        float density = dm.density;
        int width = dm.widthPixels; //屏幕宽度
        width = (int) (width / density);

        int totalHeight = Integer.valueOf(data.getHeight()); //总高度
        RelativeLayout relativeLayout = new RelativeLayout(context);
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dp2px(context, totalHeight));
        relativeLayout.setLayoutParams(lp);

        try {
            if (totalHeight <= 0)
                return relativeLayout;

            int totalRow = Integer.valueOf(data.getRow()); //总行数
            int totalColumn = Integer.valueOf(data.getColumn()); //总列数

            int spacing = 10; //间距
            int columnW = (width - 30 - (totalColumn - 1) * spacing) / totalColumn; //每列宽度
            //int rowH = (totalHeight - (totalRow - 1) * spacing) / totalRow; //每行高度

            //遍历datas
            List<FoundDataBean.RegionBean.DatasBean> datasBeans = data.getDatas();
            int xList[] = new int[totalColumn];
            int yList[] = new int[totalRow];
            for (int i = 0; i < totalColumn; i++) {
                xList[i] = i * columnW + i * spacing;
            }

            //降序
            Collections.sort(datasBeans, new Comparator<FoundDataBean.RegionBean.DatasBean>() {
                @Override
                public int compare(FoundDataBean.RegionBean.DatasBean t1, FoundDataBean.RegionBean.DatasBean t2) {
                    return t1.getRow().compareTo(t2.getRow());
                }
            });
            int yDiff = 0;
            for (int i = 0, j = datasBeans.size(); i < j; i++) {
                FoundDataBean.RegionBean.DatasBean datasBean = datasBeans.get(i);
                int rowD = Integer.valueOf(datasBean.getRow());//所在行
                int colD = Integer.valueOf(datasBean.getColumn());//所在列
                if (colD > 1)
                    continue;
                int heightD = Integer.valueOf(datasBean.getHeight()); //行高
                yList[rowD - 1] = yDiff + (rowD - 1) * spacing;
                yDiff += heightD;
            }

            for (FoundDataBean.RegionBean.DatasBean datasBean : datasBeans) {
                int rowD = Integer.valueOf(datasBean.getRow());//所在行
                int colD = Integer.valueOf(datasBean.getColumn());//所在列
                int rowspan = Integer.valueOf(datasBean.getRowspan());//跨行
                int colspan = Integer.valueOf(datasBean.getColspan());//跨列
                int heightD = Integer.valueOf(datasBean.getHeight()); //行高

                String template = datasBean.getTemplate();// 模板
                String name = datasBean.getTitle(); //标题
                String remark = datasBean.getRemark();//描述
                String icon = datasBean.getIcon(); //图片地址
                String count = datasBean.getCount();//积分数量

                //计算 top left
                int x = xList[colD - 1];
                int y = yList[rowD - 1];
                View view = null;
                if ("1".equals(template)) { //邀请宝箱
                    view = LayoutInflater.from(context).inflate(R.layout.item_main_invite_box, null);
                    view.setTag(datasBean);
                    RelativeLayout rl_main1 = view.findViewById(R.id.rl_main);
                    TextView tv_name1 = view.findViewById(R.id.tv_name);
                    TextView tv_remark1 = view.findViewById(R.id.tv_remark);
                    ImageView iv_icon1 = view.findViewById(R.id.iv_icon);
                    //赋值
                    tv_name1.setText(name);
                    tv_remark1.setText(remark);
                    GlideUtils.LoadImageWithSize(context, icon, dp2px(context, 33), dp2px(context, 25), iv_icon1);
                    rl_main1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogSkip(view, context);
                        }
                    });
                } else if ("2".equals(template)) { //答题对战
                    view = LayoutInflater.from(context).inflate(R.layout.item_main_against, null);
                    view.setTag(datasBean);
                    RelativeLayout rl_main2 = view.findViewById(R.id.rl_main);
                    TextView tv_name2 = view.findViewById(R.id.tv_name);
                    TextView tv_remark2 = view.findViewById(R.id.tv_remark);
                    ImageView iv_icon2 = view.findViewById(R.id.iv_icon);
                    //赋值
                    tv_name2.setText(name);
                    tv_remark2.setText(remark);
                    GlideUtils.LoadImageWithSize(context, icon, dp2px(context, 28), dp2px(context, 52), iv_icon2);
                    rl_main2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogSkip(view, context);
                        }
                    });
                } else if ("3".equals(template)) { //每日任务
                    view = LayoutInflater.from(context).inflate(R.layout.item_main_task, null);
                    view.setTag(datasBean);
                    RelativeLayout rl_main3 = view.findViewById(R.id.rl_main);
                    TextView tv_name3 = view.findViewById(R.id.tv_name);
                    TextView tv_remark3 = view.findViewById(R.id.tv_remark);
                    TextView tv_score = view.findViewById(R.id.tv_score);
                    ImageView iv_icon3 = view.findViewById(R.id.iv_icon);
                    //赋值
                    if (Integer.parseInt(count) == 0) {
                        tv_score.setVisibility(View.GONE);
                    } else {
                        tv_score.setVisibility(View.VISIBLE);
                        tv_score.setText("+" + count);
                    }
                    tv_name3.setText(name);
                    tv_remark3.setText(remark);
                    GlideUtils.LoadImageWithSize(context, icon, dp2px(context, 33), dp2px(context, 25), iv_icon3);
                    rl_main3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogSkip(view, context);
                        }
                    });
                } else if ("4".equals(template)) { //图片模板
                    view = LayoutInflater.from(context).inflate(R.layout.item_main_picture, null);
                    view.setTag(datasBean);
                    RelativeLayout rl_main4 = view.findViewById(R.id.rl_main);
                    RoundImageView roundImageView = view.findViewById(R.id.iv_bg);
                    new GlideImageLoader().onDisplayImage(context, roundImageView, icon);
                    rl_main4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogSkip(view, context);
                        }
                    });
                }
                //添加View
                if (view == null)
                    continue;

                int widthCol = (colspan + 1 >= totalColumn) ? width - 30 : columnW * (colspan + 1);
                ViewGroup.LayoutParams childLp = new RelativeLayout.LayoutParams(dp2px(context, widthCol), dp2px(context, heightD));
                view.setLayoutParams(childLp);
                //设置x, y
                setLayout(view, dp2px(context, x), dp2px(context, y));
                relativeLayout.addView(view);
            }
        } catch (Exception e) {

        }
        return relativeLayout;
    }

    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    private static void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 登录或跳转
     *
     * @param view
     */
    private static void dialogSkip(View view, Activity context) {
        FoundDataBean.RegionBean.DatasBean selectedItem = (FoundDataBean.RegionBean.DatasBean) view.getTag();
        LoginDataManager.instance().addData(LoginDataManager.HOME_ACTIVITY_LOGIN, selectedItem);

        final String action = selectedItem.getAction(); //跳转action
        final String h5Url = selectedItem.getH5Url(); //跳转地址
        final String content = selectedItem.getContent();//题组id
        final Long isLogin = selectedItem.getIsLogin();//是否需要登录
        LoginJumpUtil.dialogSkip(action, context, content, h5Url, isLogin);
    }
}
