package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/5/11.
 * 红包提现 红包列表
 */

public class RedPacketAdapter1 extends RecyclerView.Adapter<RedPacketAdapter1.ViewHolder> {
    private Context mContext;
    private LayoutInflater layoutInflater;

    private List<MinePrize.ChildMinePrize> ChildMinePrizes;

    public RedPacketAdapter1(Context mContext, List<MinePrize.ChildMinePrize> ChildMinePrizes) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.ChildMinePrizes = ChildMinePrizes;
        notifyDataSetChanged();
    }

    @Override
    public RedPacketAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.red_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MinePrize.ChildMinePrize ChildMinePrize = ChildMinePrizes.get(position);
        ImageLoaderManager.LoadImage(mContext, ChildMinePrize.getPicture(), holder.ivRedPacket, R.mipmap.zw02);
        holder.tvMoneyNum.setText(ChildMinePrize.getAmount());
        holder.itemView.setSelected(ChildMinePrize.selected);
        /**
         * 红包点击事件
         */
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    ChildMinePrize.selected = !ChildMinePrize.selected;
                    holder.itemView.setSelected(ChildMinePrize.selected);
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return ChildMinePrizes == null ? 0 : ChildMinePrizes.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_red_packet)
        ImageView ivRedPacket;//红包图片
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
