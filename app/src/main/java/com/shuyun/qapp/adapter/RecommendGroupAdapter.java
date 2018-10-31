package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐题组列表
 */

public class RecommendGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupBean> groupBeans;

    public RecommendGroupAdapter(List<GroupBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.hot_group_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GroupBean groupBean = groupBeans.get(position);
        try {
            ((MyViewHolder) holder).tvGroupTitle.setText(groupBean.getName() + "");
            ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), ((MyViewHolder) holder).ivGroupBg, R.mipmap.zw01);
            //百分比
            if (!EncodeAndStringTool.isListEmpty(groupBean.getTags())) {
                ((MyViewHolder) holder).llInfo.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).title1.setText(groupBean.getTags().get(0).getTagName());
                ((MyViewHolder) holder).title2.setText(groupBean.getTags().get(1).getTagName());
                ((MyViewHolder) holder).title3.setText(groupBean.getTags().get(2).getTagName());
                ((MyViewHolder) holder).tvScore.setText(groupBean.getTags().get(0).getRemark());
                ((MyViewHolder) holder).tvCash.setText(groupBean.getTags().get(1).getRemark());
                ((MyViewHolder) holder).tvRightNumber.setText(groupBean.getTags().get(2).getRemark());
            } else {
                ((MyViewHolder) holder).llInfo.setVisibility(View.GONE);
            }

            //是否消耗答题次数
            if (!EncodeAndStringTool.isStringEmpty(groupBean.getOpportunityLabel())) {
                ((MyViewHolder) holder).tvReduceNumber.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvReduceNumber.setText(groupBean.getOpportunityLabel());
            } else {
                ((MyViewHolder) holder).tvReduceNumber.setVisibility(View.GONE);
            }

            //答题攻略
            if (!EncodeAndStringTool.isStringEmpty(groupBean.getTag())) {
                ((MyViewHolder) holder).tvStrategy.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).tvStrategy.setText(groupBean.getTag());
            } else {
                ((MyViewHolder) holder).tvStrategy.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }

        /**
         * 同时不为null才可以点击
         */
        if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
            ((MyViewHolder) holder).rlItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(((MyViewHolder) holder).rlItem, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (groupBeans == null) ? 0 : groupBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        RoundImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.ll_info)
        LinearLayout llInfo;
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.title2)
        TextView title2;
        @BindView(R.id.title3)
        TextView title3;
        @BindView(R.id.tv_score)
        TextView tvScore; //获得积分
        @BindView(R.id.tv_cash)
        TextView tvCash; //获得现金
        @BindView(R.id.tv_right_number)
        TextView tvRightNumber; //正确率
        @BindView(R.id.tv_strategy)
        TextView tvStrategy; // 答题攻略
        @BindView(R.id.tv_reduce_number)
        TextView tvReduceNumber; //不消耗答题次数

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    GroupTreeAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(GroupTreeAdapter.OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }
}
