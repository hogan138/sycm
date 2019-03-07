package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.HomeBottomInfoBean;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页底部信息
 */

public class HomeBottomInfoAdapter extends RecyclerView.Adapter<HomeBottomInfoAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeBottomInfoBean.ResultBean> resultBeans;
    private LayoutInflater layoutInflater;

    public HomeBottomInfoAdapter(List<HomeBottomInfoBean.ResultBean> resultBeans, Context mContext) {
        this.resultBeans = resultBeans;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.home_bottom_info_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HomeBottomInfoBean.ResultBean resultBean = resultBeans.get(position);
        try {

            holder.tvNumber.setText(resultBean.getLabel() + " " + resultBean.getValue());

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (resultBeans == null) ? 0 : resultBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.tv_number)
        TextView tvNumber;

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
