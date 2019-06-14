package com.shuyun.qapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnyPositionBean;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.AnyPositionImgManage;
import com.shuyun.qapp.view.OvalImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 大家都在答题组列表
 */

public class HotGroupAdapter extends RecyclerView.Adapter<HotGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupBean> groupBeans;
    private LayoutInflater layoutInflater;
    private Handler mHandler = new Handler();

    public HotGroupAdapter(List<GroupBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
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
        GroupBean groupBean = groupBeans.get(position);
        holder.rlAddImageview.removeAllViews();
        try {
            holder.tvGroupTitle.setText(groupBean.getName() + "");
            ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            /**
             * 同时不为null才可以点击
             */
            holder.rlItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(holder.rlItem, position);
                }
            });

            //任意位置logo
            List<GroupBean.AdConfigs> list = groupBean.getAdConfigs();
            if (list == null)
                list = new ArrayList<>();

            for (GroupBean.AdConfigs adConfigs : list) {
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
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (groupBeans == null) ? 0 : groupBeans.size();
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

    OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }
}
