package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 大家都在答题组列表
 */

public class HotGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupBean> groupBeans;

    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;

    public HotGroupAdapter(List<GroupBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        if (ONE_ITEM == viewType) {
            View view = mInflater.inflate(R.layout.hot_group_item, parent, false);
            holder = new MyViewHolder(view);
        } else {
            View v = mInflater.inflate(R.layout.hot_group_item1, parent, false);
            holder = new MyViewHolder1(v);
        }
        return holder;

    }

    @Override
    public int getItemViewType(int position) {
        if (AppConst.i == 3) {
            AppConst.i = 0;
            return TWO_ITEM;
        } else {
            AppConst.i++;
            return ONE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GroupBean groupBean = groupBeans.get(position);
        try {
            if (holder instanceof MyViewHolder) {
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
            } else {
                ((MyViewHolder1) holder).tvGroupTitle.setText(groupBean.getName() + "");
                ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), ((MyViewHolder1) holder).ivGroupBg, R.mipmap.zw01);

                //百分比
                if (!EncodeAndStringTool.isListEmpty(groupBean.getTags())) {
                    ((MyViewHolder1) holder).llInfo.setVisibility(View.VISIBLE);
                    ((MyViewHolder1) holder).title1.setText(groupBean.getTags().get(0).getTagName());
                    ((MyViewHolder1) holder).title2.setText(groupBean.getTags().get(1).getTagName());
                    ((MyViewHolder1) holder).title3.setText(groupBean.getTags().get(2).getTagName());
                    ((MyViewHolder1) holder).tvScore.setText(groupBean.getTags().get(0).getRemark());
                    ((MyViewHolder1) holder).tvCash.setText(groupBean.getTags().get(1).getRemark());
                    ((MyViewHolder1) holder).tvRightNumber.setText(groupBean.getTags().get(2).getRemark());
                } else {
                    ((MyViewHolder1) holder).llInfo.setVisibility(View.GONE);
                }

                //是否消耗答题
                if (!EncodeAndStringTool.isStringEmpty(groupBean.getOpportunityLabel())) {
                    ((MyViewHolder1) holder).tvReduceNumber.setVisibility(View.VISIBLE);
                    ((MyViewHolder1) holder).tvReduceNumber.setText(groupBean.getOpportunityLabel());
                } else {
                    ((MyViewHolder1) holder).tvReduceNumber.setVisibility(View.GONE);
                }

                //答题攻略
                if (!EncodeAndStringTool.isStringEmpty(groupBean.getTag())) {
                    ((MyViewHolder1) holder).tvStrategy.setVisibility(View.VISIBLE);
                    ((MyViewHolder1) holder).tvStrategy.setText(groupBean.getTag());
                } else {
                    ((MyViewHolder1) holder).tvStrategy.setVisibility(View.GONE);
                }
                /**
                 * 同时不为null才可以点击
                 */
                if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
                    ((MyViewHolder1) holder).rlItem.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((MyViewHolder1) holder).rlItem, position);
                        }
                    });
                }
            }
        } catch (Exception e) {

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
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.title2)
        TextView title2;
        @BindView(R.id.title3)
        TextView title3;
        @BindView(R.id.ll_info)
        LinearLayout llInfo;
        @BindView(R.id.tv_score)
        TextView tvScore; //获得积分
        @BindView(R.id.tv_cash)
        TextView tvCash; //获得现金
        @BindView(R.id.tv_right_number)
        TextView tvRightNumber; //正确率
        @BindView(R.id.tv_strategy)
        TextView tvStrategy; //答题攻略
        @BindView(R.id.tv_reduce_number)
        TextView tvReduceNumber; //不消耗答题次数

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    class MyViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.tv_strategy)
        TextView tvStrategy; //答题攻略
        @BindView(R.id.tv_reduce_number)
        TextView tvReduceNumber; //不消耗答题次数
        @BindView(R.id.title1)
        TextView title1;
        @BindView(R.id.title2)
        TextView title2;
        @BindView(R.id.title3)
        TextView title3;
        @BindView(R.id.ll_info)
        LinearLayout llInfo;
        @BindView(R.id.tv_score)
        TextView tvScore; //消耗积分
        @BindView(R.id.tv_cash)
        TextView tvCash; //获得现金
        @BindView(R.id.tv_right_number)
        TextView tvRightNumber; //正确率

        public MyViewHolder1(View itemView) {
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
