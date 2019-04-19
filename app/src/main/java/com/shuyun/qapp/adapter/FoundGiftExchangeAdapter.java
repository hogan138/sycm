package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ScoreExchangeBeans;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现---积分兑换礼品
 */

public class FoundGiftExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<ScoreExchangeBeans.PresentsBean> presentsBeanList;

    public FoundGiftExchangeAdapter(List<ScoreExchangeBeans.PresentsBean> presentsBeanList, Context mContext) {
        this.presentsBeanList = presentsBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.found_gift_exchange_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ScoreExchangeBeans.PresentsBean presentsBean = presentsBeanList.get(position);
        try {
            GlideUtils.LoadImage1(mContext, presentsBean.getPicture(), ((MyViewHolder) holder).ivLogo);
            ((MyViewHolder) holder).tvTitle.setText(presentsBean.getName());
            ((MyViewHolder) holder).tvConten.setText(presentsBean.getPurpose());
            ((MyViewHolder) holder).tvJoin.setText(presentsBean.getActionLabel());

            String action = presentsBean.getAction();
            if (action.equals("action.exchange")) {
                //可兑换
                ((MyViewHolder) holder).tvJoin.setBackgroundResource(R.drawable.blue_btn_bg);
                ((MyViewHolder) holder).tvJoin.setEnabled(true);
                ((MyViewHolder) holder).llItem.setEnabled(true);
            } else if (action.equals("action.replenishment")) {
                //不可兑换
                ((MyViewHolder) holder).tvJoin.setBackgroundResource(R.drawable.exchange_gray_btn_bg);
                ((MyViewHolder) holder).tvJoin.setEnabled(false);
                ((MyViewHolder) holder).llItem.setEnabled(false);
            }

            ((MyViewHolder) holder).llItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(((MyViewHolder) holder).llItem, position);
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (presentsBeanList == null) ? 0 : presentsBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_conten)
        TextView tvConten;
        @BindView(R.id.tv_join)
        TextView tvJoin;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    FoundGiftExchangeAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(FoundGiftExchangeAdapter.OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }

}
