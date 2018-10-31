package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 首页分类
 */

public class HomeSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<GroupClassifyBean> groupClassifyBeans;

    public HomeSortAdapter(List<GroupClassifyBean> groupClassifyBeans, Context mContext) {
        this.groupClassifyBeans = groupClassifyBeans;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.home_sort_group_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GroupClassifyBean groupClassifyBean = groupClassifyBeans.get(position);
        try {
            ((MyViewHolder) holder).tvTitle.setText(groupClassifyBean.getName());

            /**
             * 同时不为null才可以点击
             */
            if (!EncodeAndStringTool.isObjectEmpty(mOnItemClickListener)) {
                ((MyViewHolder) holder).tvLookMore.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(((MyViewHolder) holder).tvLookMore, position);
                    }
                });
            }

            List<GroupClassifyBean.ChildrenBean> childrenBeanList = groupClassifyBean.getChildren();
            if (!EncodeAndStringTool.isListEmpty(childrenBeanList)) {   //大家都在答
                try {
                    ((MyViewHolder) holder).rvSortGroup.setHasFixedSize(true);
                    ((MyViewHolder) holder).rvSortGroup.setNestedScrollingEnabled(false);
                    SortHomeGroupAdapter hotGroupAdapter = new SortHomeGroupAdapter(childrenBeanList, mContext);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
                    ((MyViewHolder) holder).rvSortGroup.setLayoutManager(gridLayoutManager);
                    ((MyViewHolder) holder).rvSortGroup.setAdapter(hotGroupAdapter);
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (groupClassifyBeans == null) ? 0 : groupClassifyBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_look_more)
        TextView tvLookMore;
        @BindView(R.id.rv_sort_group)
        RecyclerView rvSortGroup;

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
