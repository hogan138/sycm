package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 大家都在答题组列表
 */

public class HotGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupBean> groupBeans;

    public HotGroupAdapter(List<GroupBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.hot_group_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GroupBean groupBean = groupBeans.get(position);
        try {
            ((MyViewHolder) holder).tvGroupTitle.setText(groupBean.getName() + "");
            ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), ((MyViewHolder) holder).ivGroupBg, R.mipmap.zw01);

            /**
             * 同时不为null才可以点击
             */
            if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
                ((MyViewHolder) holder).rlItem.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(((MyViewHolder) holder).rlItem, position);

                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (groupBeans == null) ? 0 : groupBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

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
