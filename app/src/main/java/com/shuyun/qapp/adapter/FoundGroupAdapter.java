package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 发现热门题组二级适配器
 */

public class FoundGroupAdapter extends RecyclerView.Adapter<FoundGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<FoundDataBean.TablesBean.GroupsBean.ChildrenBean> childrenBeanList;
    private LayoutInflater layoutInflater;

    public FoundGroupAdapter(List<FoundDataBean.TablesBean.GroupsBean.ChildrenBean> childrenBeanList, Context mContext) {
        this.childrenBeanList = childrenBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hot_group_found_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FoundDataBean.TablesBean.GroupsBean.ChildrenBean childrenBean = childrenBeanList.get(position);
        holder.rlAddImageview.removeAllViews();
        try {
            holder.tvGroupTitle.setText(childrenBean.getName());
            ImageLoaderManager.LoadImage(mContext, childrenBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            if (childrenBean.getTag() == 1) {
                holder.ivRanking.setVisibility(View.VISIBLE);
                holder.ivRanking.setBackgroundResource(R.mipmap.found_hot_group_one);
            } else if (childrenBean.getTag() == 2) {
                holder.ivRanking.setVisibility(View.VISIBLE);
                holder.ivRanking.setBackgroundResource(R.mipmap.found_hot_group_two);
            } else if (childrenBean.getTag() == 3) {
                holder.ivRanking.setVisibility(View.VISIBLE);
                holder.ivRanking.setBackgroundResource(R.mipmap.found_hot_group_three);
            } else {
                holder.ivRanking.setVisibility(View.GONE);
            }

            /**
             * 同时不为null才可以点击
             */
            holder.rlItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", childrenBean.getId());
                    intent.putExtra("h5Url", childrenBean.getH5Url());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (childrenBeanList == null) ? 0 : childrenBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.rl_add_imageview)
        RelativeLayout rlAddImageview;
        @BindView(R.id.iv_ranking)
        ImageView ivRanking;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
