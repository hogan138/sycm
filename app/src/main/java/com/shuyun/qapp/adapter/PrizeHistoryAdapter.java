package com.shuyun.qapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ExchangeHistoryBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 宝贝兑换记录
 */

public class PrizeHistoryAdapter extends RecyclerView.Adapter<PrizeHistoryAdapter.MyViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<ExchangeHistoryBean.TreasureUserChangeDataListBean> treasureUserChangeDataListBeanList;
    private LayoutInflater inflater;

    public PrizeHistoryAdapter(List<ExchangeHistoryBean.TreasureUserChangeDataListBean> treasureUserChangeDataListBeanList, Context mContext) {
        this.treasureUserChangeDataListBeanList = treasureUserChangeDataListBeanList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prize_history_record, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ExchangeHistoryBean.TreasureUserChangeDataListBean treasureUserChangeDataListBean = treasureUserChangeDataListBeanList.get(position);
        holder.tvDate.setText(TimeUtils.millis2String(treasureUserChangeDataListBean.getChangeTime()).replace("-", "/"));
        holder.tvNumber.setText(treasureUserChangeDataListBean.getUserAccount());
        holder.tvNo.setText(treasureUserChangeDataListBean.getTicketNum());

        //全部兑换奖券记录
        if (treasureUserChangeDataListBean.getIsMine() == 1 && treasureUserChangeDataListBean.getIsWin() == 1) {
            //我中奖
            holder.tvDate.setTextColor(Color.parseColor("#ffb21e"));
            holder.tvNumber.setTextColor(Color.parseColor("#ffb21e"));
            holder.tvNo.setTextColor(Color.parseColor("#ffb21e"));
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setBackgroundResource(R.mipmap.winning_logo);
        } else if (treasureUserChangeDataListBean.getIsMine() == 1 && treasureUserChangeDataListBean.getIsWin() == 0) {
            //我的
            holder.tvDate.setTextColor(Color.parseColor("#ffb21e"));
            holder.tvNumber.setTextColor(Color.parseColor("#ffb21e"));
            holder.tvNo.setTextColor(Color.parseColor("#ffb21e"));
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setBackgroundResource(R.mipmap.my_record_logo);
        } else if (treasureUserChangeDataListBean.getIsWin() == 1 && treasureUserChangeDataListBean.getIsMine() == 0) {
            //中奖的用户
            holder.tvDate.setTextColor(Color.parseColor("#f05939"));
            holder.tvNumber.setTextColor(Color.parseColor("#f05939"));
            holder.tvNo.setTextColor(Color.parseColor("#f05939"));
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setBackgroundResource(R.mipmap.winning_logo);
        } else {
            //未中奖
            holder.tvDate.setTextColor(Color.parseColor("#333333"));
            holder.tvNumber.setTextColor(Color.parseColor("#333333"));
            holder.tvNo.setTextColor(Color.parseColor("#333333"));
            holder.ivLogo.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (treasureUserChangeDataListBeanList == null) ? 0 : treasureUserChangeDataListBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.iv_logo)
        ImageView ivLogo;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
