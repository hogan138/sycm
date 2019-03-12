package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 70周年Adapter
 */

public class SeventyYearAdapter extends RecyclerView.Adapter<SeventyYearAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<ActivityTabBean.ResultBean> activityBeanList;

    public SeventyYearAdapter(Context context, List<ActivityTabBean.ResultBean> activityBeanList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityBeanList = activityBeanList;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.seventy_year_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ActivityTabBean.ResultBean resultBean = activityBeanList.get(position);
        ImageLoaderManager.LoadImage(context, resultBean.getBaseImage(), holder.ivBgImage, R.mipmap.zw01);

        if (resultBean.isStop()) {
            //已结束
            holder.ivEnd.setVisibility(View.VISIBLE);
            holder.tvJoin.setBackgroundResource(R.drawable.seventyyear_btn_bg);
            holder.tvJoin.setText("活动回顾");
        } else {
            //正常
            holder.ivEnd.setVisibility(View.GONE);
            holder.tvJoin.setBackgroundResource(R.drawable.blue_btn_bg);
            holder.tvJoin.setText("立即参与");
        }

        holder.rlActivityItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.rlActivityItem, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return activityBeanList == null ? 0 : activityBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_activity_item)
        RelativeLayout rlActivityItem;
        @BindView(R.id.iv_bg_image)
        RoundImageView ivBgImage;
        @BindView(R.id.tv_join)
        TextView tvJoin;
        @BindView(R.id.iv_end)
        ImageView ivEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    OnItemClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }

}
