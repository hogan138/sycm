package com.shuyun.qapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/5/11.
 * 红包提现 红包列表
 */

public class RedPacketAdapter extends RecyclerView.Adapter<RedPacketAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater layoutInflater;

    private List<MinePrize> minePrizes;

    public RedPacketAdapter(Context mContext, List<MinePrize> minePrizes) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.minePrizes = minePrizes;
        notifyDataSetChanged();
    }

    @Override
    public RedPacketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.red_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MinePrize minePrize = minePrizes.get(position);
        holder.tvMoneyNum.setText(minePrize.getAmount());
        holder.itemView.setSelected(minePrize.selected);
        if (holder.itemView.isSelected()) {
            holder.tvMoneyNum.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.tvMoneyNum.setTextColor(Color.parseColor("#0194ec"));
        }
        /**
         * 红包点击事件
         */
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    minePrize.selected = !minePrize.selected;
                    holder.itemView.setSelected(minePrize.selected);
                    if (holder.itemView.isSelected()) {
                        holder.tvMoneyNum.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        holder.tvMoneyNum.setTextColor(Color.parseColor("#0194ec"));
                    }
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return minePrizes == null ? 0 : minePrizes.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_money_num)
        TextView tvMoneyNum;//金额

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
