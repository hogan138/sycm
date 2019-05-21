package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MarkBannerItem2;

import java.util.ArrayList;
import java.util.List;

import cn.kevin.banner.BannerAdapter;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.ImageLoader;

/**
 * 新版首页banner
 * 创建人：${ganquan}
 */
public class MarkBannerAdapter2 extends BannerAdapter {
    private ImageLoader imageLoader;
    private Handler mHandler = new Handler();
    private Activity mContext;

    private List<RelativeLayout> views = new ArrayList<>();

    public MarkBannerAdapter2(ImageLoader imageLoader, Activity mContext) {
        super(imageLoader);
        this.mContext = mContext;
        this.imageLoader = imageLoader;
    }

    @Override
    protected View createView(Context context, IBannerItem item) {

        MarkBannerItem2 markBannerItem = (MarkBannerItem2) item;

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.mark2_banner_layout, null);

        ImageView itemView = view.findViewById(R.id.imageView);
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (imageLoader != null) {
            imageLoader.onDisplayImage(context, itemView, item.ImageUrl());
        }

        TextView tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText("【" + markBannerItem.getName() + "】");

        TextView tv_description = view.findViewById(R.id.tv_description);
        tv_description.setText(markBannerItem.getDescription());

        view.setTag(false);
        views.add(view);
        return view;
    }

    //清除view
    public void clearViews() {
        views.clear();
    }

    //刷新logo配置
//    public void refreshAdConfig(IBannerItem bannerItem, int i) {
//        final View parentView = views.get(i);
//        if (parentView == null)
//            return;
//        if ((Boolean) parentView.getTag())
//            return;
//        parentView.setTag(true);
//        //任意位置logo
//        MarkBannerItem2 item1 = (MarkBannerItem2) bannerItem;
//        List<GroupBean.AdConfigs> configs = item1.getAdConfigs();
//        if (configs == null)
//            configs = new ArrayList<>();
//
//        final RelativeLayout layout = parentView.findViewById(R.id.rl_add_imageview);
//        for (GroupBean.AdConfigs adConfigs : configs) {
//            Long type = adConfigs.getType();
//            if (AppConst.TYPE_HOME_GROUP != type) {
//                continue;
//            }
//            final AnyPositionBean anyPositionBean = new AnyPositionBean();
//            anyPositionBean.setType(adConfigs.getType());
//            anyPositionBean.setLocation(adConfigs.getLocation());
//            anyPositionBean.setWidth(adConfigs.getWidth());
//            anyPositionBean.setHeight(adConfigs.getHeight());
//            anyPositionBean.setPadding(adConfigs.getPadding());
//            anyPositionBean.setMargin(adConfigs.getMargin());
//            anyPositionBean.setShadow(adConfigs.getShadow());
//            anyPositionBean.setShadowColor(adConfigs.getShadowColor());
//            anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
//            anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
//            anyPositionBean.setImageUrl(adConfigs.getImageUrl());
//            //执行
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    AnyPositionImgManage.execute(anyPositionBean, layout, mContext);
//                }
//            }, 0);
//        }
//    }

}
