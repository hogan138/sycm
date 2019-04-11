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
import com.shuyun.qapp.bean.TaskBeans;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现任务适配器
 */

public class FoundTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<TaskBeans.DatasBean.TasksBean> tasksBeans;

    public FoundTaskAdapter(List<TaskBeans.DatasBean.TasksBean> tasksBeans, Context mContext) {
        this.tasksBeans = tasksBeans;
        this.mContext = mContext;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.found_new_task_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            TaskBeans.DatasBean.TasksBean tasksBean = tasksBeans.get(position);
            GlideUtils.LoadImage1(mContext, tasksBean.getPicture(), ((MyViewHolder) holder).ivLogo);
            ((MyViewHolder) holder).tvTitle.setText(tasksBean.getName());
            ((MyViewHolder) holder).tvContent.setText(tasksBean.getPurpose());
            ((MyViewHolder) holder).tvCount.setText(tasksBean.getPrize());
            ((MyViewHolder) holder).tvEnter.setText(tasksBean.getActionLabel());
            String action = tasksBean.getAction();
            if (action.equals(AppConst.ACTION_RECEIVE)) {//可领取
                ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_btn_bg);
                ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#ffffff"));
                ((MyViewHolder) holder).tvEnter.setEnabled(true);
            } else if (action.equals(AppConst.ACTION_FINISH)) { //已完成
                ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.gray_stroke_btn_bg);
                ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#ffffff"));
                ((MyViewHolder) holder).tvEnter.setEnabled(false);
            } else {//其他
                ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_stroke_btn_bg);
                ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#0194ec"));
                ((MyViewHolder) holder).tvEnter.setEnabled(true);
            }

            if (position == tasksBeans.size() - 1) {
                ((MyViewHolder) holder).viewLine.setVisibility(View.GONE);
            } else {
                ((MyViewHolder) holder).viewLine.setVisibility(View.VISIBLE);
            }

            ((MyViewHolder) holder).tvEnter.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemChildClickLitsener.onItemChildClick(((MyViewHolder) holder).tvEnter, position);
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return (tasksBeans == null) ? 0 : tasksBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_enter)
        TextView tvEnter;
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.view_line)
        View viewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    OnItemChildClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemChildClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemChildClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }

}
