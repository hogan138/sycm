package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AgainstGruopListBeans;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 答题对战分类列表适配器
 */

public class AgainstListGroupAdapter extends RecyclerView.Adapter<AgainstListGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<AgainstGruopListBeans.DatasBean> datasBeanList;
    private LayoutInflater layoutInflater;
    private int recommendWidth = 0, recommendHeight = 0;

    public AgainstListGroupAdapter(List<AgainstGruopListBeans.DatasBean> datasBeanList, Context mContext) {
        this.datasBeanList = datasBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.against_sort_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AgainstGruopListBeans.DatasBean datasBean = datasBeanList.get(position);
        holder.tvGroupTitle.setText(datasBean.getName());
        //获取屏幕宽度
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        //计算高度宽高比
        recommendWidth = ((int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(45)) / 2;
        recommendHeight = (int) Math.ceil(recommendWidth * (83f / 165f));
        ViewGroup.LayoutParams bannerParams = holder.ivGroupBg.getLayoutParams();
        bannerParams.height = recommendHeight;
        holder.ivGroupBg.setLayoutParams(bannerParams);

        ImageLoaderManager.LoadImage(mContext, datasBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

        holder.rlItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.rlItem, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (datasBeanList == null) ? 0 : datasBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;
        @BindView(R.id.tv_group_title)
        TextView tvGroupTitle;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

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
