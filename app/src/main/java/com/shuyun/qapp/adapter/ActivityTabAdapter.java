package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * 活动专区Adapter
 */

public class ActivityTabAdapter extends RecyclerView.Adapter<ActivityTabAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<ActivityTabBean.ResultBean> activityBeanList;
    private int height = 0;

    public ActivityTabAdapter(Context context, List<ActivityTabBean.ResultBean> activityBeanList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityBeanList = activityBeanList;

        //获取屏幕宽度
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w = (int) Math.ceil(dm.widthPixels) - dp2px(20);
        height = (int) Math.ceil(w * (300f / 690f));

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_tab_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ActivityTabBean.ResultBean resultBean = activityBeanList.get(position);
        ImageLoaderManager.LoadImage(context, resultBean.getBaseImage(), holder.ivBgImage, R.mipmap.zw01);

        ViewGroup.LayoutParams lp = holder.rlActivityItem.getLayoutParams();
        lp.height = height;
        holder.rlActivityItem.setLayoutParams(lp);

        if (resultBean.isStop()) {
            //已结束
            holder.llEnd.setVisibility(View.VISIBLE);
//            holder.rlActivityItem.setEnabled(false);
        } else {
            //正常
            holder.llEnd.setVisibility(View.GONE);
//            holder.rlActivityItem.setEnabled(true);
        }

        holder.rlActivityItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.rlActivityItem, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return activityBeanList == null ? 0 : activityBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_activity_item)
        RelativeLayout rlActivityItem;
        @BindView(R.id.iv_bg_image)
        RoundImageView ivBgImage;
        @BindView(R.id.ll_end)
        RelativeLayout llEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    OnItemClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }

}
