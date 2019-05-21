package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页猜你喜欢题组列表
 */

public class HomeLikeGroupAdapter extends RecyclerView.Adapter<HomeLikeGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<NewHomeSelectBean.GuessBean> guessBeanList;
    private LayoutInflater layoutInflater;
    private int recommendWidth = 0, recommendHeight = 0;

    public HomeLikeGroupAdapter(List<NewHomeSelectBean.GuessBean> guessBeanList, Context mContext) {
        this.guessBeanList = guessBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.home_like_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NewHomeSelectBean.GuessBean guessBean = guessBeanList.get(position);
        try {
            holder.tvGroupTitle.setText(guessBean.getName());
            //获取屏幕宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //计算猜你喜欢高度宽高比
            recommendWidth = ((int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(3)) / 2;
            recommendHeight = (int) Math.ceil(recommendWidth * (200f / 372f));
            ViewGroup.LayoutParams bannerParams = holder.ivGroupBg.getLayoutParams();
            bannerParams.height = recommendHeight;
            holder.ivGroupBg.setLayoutParams(bannerParams);
            ImageLoaderManager.LoadImage(mContext, guessBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            /**
             * 同时不为null才可以点击
             */
            holder.llItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(holder.llItem, position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (guessBeanList == null) ? 0 : guessBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        ImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.ll_item)
        LinearLayout llItem;

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
