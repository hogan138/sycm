package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现热门题组
 */

public class FoundHotGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<FoundDataBean.TablesBean.GroupsBean> groupsBeanList;

    public FoundHotGroupAdapter(List<FoundDataBean.TablesBean.GroupsBean> groupsBeanList, Context mContext) {
        this.groupsBeanList = groupsBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        View view = mInflater.inflate(R.layout.found_hot_group_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        FoundDataBean.TablesBean.GroupsBean groupsBean = groupsBeanList.get(position);
        try {
            ((MyViewHolder) holder).tvTitle.setText(groupsBean.getTitle());

            List<FoundDataBean.TablesBean.GroupsBean.ChildrenBean> childrenBeanList = groupsBean.getChildren();
            if (!EncodeAndStringTool.isListEmpty(childrenBeanList)) {
                try {
                    //解决数据加载不完的问题
                    ((MyViewHolder) holder).rvHotGroup.setHasFixedSize(true);
                    ((MyViewHolder) holder).rvHotGroup.setNestedScrollingEnabled(false);
                    FoundGroupAdapter foundGroupAdapter = new FoundGroupAdapter(childrenBeanList, mContext);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
                        @Override
                        public boolean canScrollVertically() { //禁止layout垂直滑动
                            return false;
                        }
                    };
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    ((MyViewHolder) holder).rvHotGroup.setLayoutManager(linearLayoutManager);
                    ((MyViewHolder) holder).rvHotGroup.setAdapter(foundGroupAdapter);
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (groupsBeanList == null) ? 0 : groupsBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rv_hot_group)
        RecyclerView rvHotGroup;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
