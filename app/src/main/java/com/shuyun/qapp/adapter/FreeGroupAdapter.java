package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupAgainstBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自由对战题组列表
 */

public class FreeGroupAdapter extends RecyclerView.Adapter<FreeGroupAdapter.MyViewHolder> {
    private Context mContext;
    //题组分类集合
    private List<GroupAgainstBean> groupBeans;
    private LayoutInflater inflater;

    public FreeGroupAdapter(List<GroupAgainstBean> groupBeans, Context mContext) {
        this.groupBeans = groupBeans;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public FreeGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.free_group_item, parent, false);
        FreeGroupAdapter.MyViewHolder holder = new FreeGroupAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FreeGroupAdapter.MyViewHolder holder, final int position) {
        GroupAgainstBean groupBean = groupBeans.get(position);

        ImageLoaderManager.LoadImage(mContext, groupBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);
        holder.tvGroupTitle.setText(groupBean.getName() + "");
        if (!EncodeAndStringTool.isStringEmpty(groupBean.getDescription())) {
            holder.tvGroupContent.setText(groupBean.getDescription() + "");
        }

        holder.ivGroupBg.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.ivGroupBg, position);
            }
        });
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
        @BindView(R.id.tv_group_content)
        TextView tvGroupContent;//题组内容

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    FreeGroupAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitsener(FreeGroupAdapter.OnItemClickListener mOnItemClickLitsener) {
        this.mOnItemClickListener = mOnItemClickLitsener;
    }
}
