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
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.ScoreExchangeBeans;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现---积分兑换道具
 */

public class FoundPropsExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<ScoreExchangeBeans.PropsBean> propsBeanList;

    public FoundPropsExchangeAdapter(List<ScoreExchangeBeans.PropsBean> propsBeanList, Context mContext) {
        this.propsBeanList = propsBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.found_props_exchange_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ScoreExchangeBeans.PropsBean propsBean = propsBeanList.get(position);
        try {
            GlideUtils.LoadImage1(mContext, propsBean.getPicture(), ((MyViewHolder) holder).ivLogo);
            ((MyViewHolder) holder).tvTitle.setText(propsBean.getName());
            ((MyViewHolder) holder).tvConten.setText(propsBean.getPurpose());
            ((MyViewHolder) holder).tvJoin.setText(propsBean.getActionLabel());
            ((MyViewHolder) holder).tvJoin.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(((MyViewHolder) holder).tvJoin, position);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (propsBeanList == null) ? 0 : propsBeanList.size();
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
