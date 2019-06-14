package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 首页最新上线适配器
 */

public class HomeNewGroupAdapter extends RecyclerView.Adapter<HomeNewGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<NewHomeSelectBean.NewTopicsBean> newTopicsBeanList;
    private LayoutInflater layoutInflater;

    public HomeNewGroupAdapter(List<NewHomeSelectBean.NewTopicsBean> newTopicsBeanList, Context mContext) {
        this.newTopicsBeanList = newTopicsBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.home_new_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewHomeSelectBean.NewTopicsBean newTopicsBean = newTopicsBeanList.get(position);
        try {
            holder.tvGroupTitle.setText(newTopicsBean.getName());
            ImageLoaderManager.LoadImage(mContext, newTopicsBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            /**
             * 同时不为null才可以点击
             */
            holder.llItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", newTopicsBean.getId());
                    intent.putExtra("h5Url", newTopicsBean.getH5Url());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (newTopicsBeanList == null) ? 0 : newTopicsBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        ImageView ivGroupBg;//题组背景图
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;//题组标题
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
