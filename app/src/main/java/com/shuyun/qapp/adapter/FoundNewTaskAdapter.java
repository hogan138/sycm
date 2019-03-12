package com.shuyun.qapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupClassifyBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现新手任务
 */

public class FoundNewTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupClassifyBean> groupClassifyBeans;

    public FoundNewTaskAdapter(List<GroupClassifyBean> groupClassifyBeans, Context mContext) {
        this.groupClassifyBeans = groupClassifyBeans;
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
        GroupClassifyBean groupClassifyBean = groupClassifyBeans.get(position);
//        ((MyViewHolder) holder).tvTitle.setText(groupClassifyBean.getName());
        if (position == 0) {
            ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_stroke_btn_bg);
            ((MyViewHolder) holder).tvEnter.setText("立即前往");
            ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#0194ec"));
        } else if (position == 1) {
            ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_btn_bg);
            ((MyViewHolder) holder).tvEnter.setText("领取奖励");
            ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#ffffff"));
        } else if (position == 2) {
            ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.gray_stroke_btn_bg);
            ((MyViewHolder) holder).tvEnter.setText("已完成");
            ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#999999"));
        }
        if (position == 3) {
            ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_stroke_btn_bg);
            ((MyViewHolder) holder).tvEnter.setText("立即前往");
            ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#0194ec"));
        } else if (position == 4) {
            ((MyViewHolder) holder).tvEnter.setBackgroundResource(R.drawable.blue_btn_bg);
            ((MyViewHolder) holder).tvEnter.setText("领取奖励");
            ((MyViewHolder) holder).tvEnter.setTextColor(Color.parseColor("#ffffff"));
        }

    }

    @Override
    public int getItemCount() {
        return (groupClassifyBeans == null) ? 0 : groupClassifyBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_count_content)
        TextView tvCountContent;
        @BindView(R.id.tv_enter)
        TextView tvEnter;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
