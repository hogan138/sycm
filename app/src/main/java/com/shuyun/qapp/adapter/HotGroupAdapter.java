package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/5/2.
 * 热门题组列表
 */

public class HotGroupAdapter extends RecyclerView.Adapter<HotGroupAdapter.MyViewHolder> {
    private Context mContext;
    //题组分类集合
    private List<GroupBean> groupBeans;
    private LayoutInflater inflater;

    public HotGroupAdapter(List<GroupBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public HotGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hot_group_item, parent, false);
        HotGroupAdapter.MyViewHolder holder = new HotGroupAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HotGroupAdapter.MyViewHolder holder, final int position) {
        GroupBean groupBean = groupBeans.get(position);
        holder.tvGroupTitle.setText(groupBean.getName() + "");
        ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);
        /**
         * 同时不为null才可以点击
         */
        if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
            holder.ivGroupBg.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.ivGroupBg, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (groupBeans == null) ? 0 : groupBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        RoundImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    GroupTreeAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(GroupTreeAdapter.OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }
}
