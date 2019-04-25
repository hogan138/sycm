package com.shuyun.qapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基本信息-提现信息adapter
 */

public class WithdrawInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<MineBean.WithdrawBaseBean> withdrawBaseBeanList;

    public WithdrawInfoAdapter(List<MineBean.WithdrawBaseBean> withdrawBaseBeanList, Context mContext) {
        this.withdrawBaseBeanList = withdrawBaseBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.item_withdraw_info, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MineBean.WithdrawBaseBean withdrawBaseBean = withdrawBaseBeanList.get(position);
        try {
            ((MyViewHolder) holder).tvWithdrawTitle.setText(withdrawBaseBean.getTitle());
            ((MyViewHolder) holder).tvWithdrawStatus.setText(withdrawBaseBean.getStateName());
            ((MyViewHolder) holder).tvWithdrawDescription.setText(withdrawBaseBean.getMessage());
            Long status_withdraw = withdrawBaseBean.getStatus();
            boolean enabled_withdraw = withdrawBaseBean.isEnabled();
            //更改颜色
            if (status_withdraw == 3) {
                ((MyViewHolder) holder).tvWithdrawStatus.setTextColor(Color.parseColor("#0194EC"));
            } else {
                ((MyViewHolder) holder).tvWithdrawStatus.setTextColor(Color.parseColor("#F53434"));
            }
            //是否可以点击
            if (enabled_withdraw) {
                ((MyViewHolder) holder).llWithdrawInfo.setEnabled(true);
            } else {
                ((MyViewHolder) holder).llWithdrawInfo.setEnabled(false);
            }
            ((MyViewHolder) holder).llWithdrawInfo.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(((MyViewHolder) holder).llWithdrawInfo, position);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (withdrawBaseBeanList == null) ? 0 : withdrawBaseBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_withdraw_title)
        TextView tvWithdrawTitle;
        @BindView(R.id.tv_withdraw_status)
        TextView tvWithdrawStatus;
        @BindView(R.id.tv_withdraw_description)
        TextView tvWithdrawDescription;
        @BindView(R.id.ll_withdraw_info)
        LinearLayout llWithdrawInfo;

        public MyViewHolder(View itemView) {
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
