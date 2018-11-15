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
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.utils.ImageLoaderManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类题组左侧适配器
 */

public class GroupTreeAdapter extends RecyclerView.Adapter<GroupTreeAdapter.ViewHolder> {
    private Context mContext;
    //题组分类集合
    private List<GroupClassifyBean> classifyBeans;
    private LayoutInflater inflater;

    private int selectedPosition = 0;

    public GroupTreeAdapter(List<GroupClassifyBean> classifyBeans, Context mContext) {
        this.mContext = mContext;
        this.classifyBeans = classifyBeans;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public GroupTreeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.group_question_sort_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GroupTreeAdapter.ViewHolder holder, final int position) {
        GroupClassifyBean classifyBean = classifyBeans.get(position);
        holder.tvSortName.setText(classifyBean.getName());
        ImageLoaderManager.LoadImage(mContext, classifyBean.getPicture(), holder.ivItem, R.mipmap.zw01);
        if (classifyBeans.get(position).isFlag()) {
            holder.rlSort.setSelected(true);
            holder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.color_35));
        } else {
            holder.tvSortName.setTextColor(mContext.getResources().getColor(R.color.color_35));
            holder.rlSort.setSelected(false);
        }
        /**
         * 同时不为null才可以点击
         */
        if (mOnItemClickListener != null && null != classifyBean.getChildren()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition == position)
                        return;
                    selectedPosition = position;
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (classifyBeans == null) ? 0 : classifyBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_sort_item)
        ImageView ivItem;//题组分类图标
        @BindView(R.id.tv_sort_name)
        TextView tvSortName;//题组分类名字
        @BindView(R.id.rl_sort)
        RelativeLayout rlSort;//点击或选中换背景的效果

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
