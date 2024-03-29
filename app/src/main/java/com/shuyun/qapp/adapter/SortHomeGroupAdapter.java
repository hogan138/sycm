package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnyPositionBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.AnyPositionImgManage;
import com.shuyun.qapp.view.OvalImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 首页分类二级适配器
 */

public class SortHomeGroupAdapter extends RecyclerView.Adapter<SortHomeGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupClassifyBean.ChildrenBean> childrenBeanList;
    private LayoutInflater layoutInflater;
    private Handler mHandler = new Handler();

    public SortHomeGroupAdapter(List<GroupClassifyBean.ChildrenBean> childrenBeanList, Context mContext) {
        this.childrenBeanList = childrenBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hot_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GroupClassifyBean.ChildrenBean childrenBean = childrenBeanList.get(position);
        holder.rlAddImageview.removeAllViews();
        try {
            holder.tvGroupTitle.setText(childrenBean.getName() + "");
            ImageLoaderManager.LoadImage(mContext, childrenBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);
            /**
             * 同时不为null才可以点击
             */
            holder.rlItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", childrenBean.getId());
                    intent.putExtra("h5Url", childrenBean.getH5Url());
                    startActivity(intent);
                }
            });

            //任意logo
            List<GroupClassifyBean.AdConfigs> list = childrenBean.getAdConfigs();
            if (list == null)
                list = new ArrayList<>();
            for (GroupClassifyBean.AdConfigs adConfigs : list) {
                Long type = adConfigs.getType();
                if (AppConst.TYPE_HOME_GROUP != type) {
                    continue;
                }
                final AnyPositionBean anyPositionBean = new AnyPositionBean();
                anyPositionBean.setType(adConfigs.getType());
                anyPositionBean.setLocation(adConfigs.getLocation());
                anyPositionBean.setWidth(adConfigs.getWidth());
                anyPositionBean.setHeight(adConfigs.getHeight());
                anyPositionBean.setPadding(adConfigs.getPadding());
                anyPositionBean.setMargin(adConfigs.getMargin());
                anyPositionBean.setShadow(adConfigs.getShadow());
                anyPositionBean.setShadowColor(adConfigs.getShadowColor());
                anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
                anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
                anyPositionBean.setImageUrl(adConfigs.getImageUrl());
                //执行
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnyPositionImgManage.execute(anyPositionBean, holder.rlAddImageview, mContext);
                    }
                }, 0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (childrenBeanList == null) ? 0 : childrenBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.rl_add_imageview)
        RelativeLayout rlAddImageview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
