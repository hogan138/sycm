package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GameListBeans;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 游戏中心列表
 */

public class GameCenterAdapter extends RecyclerView.Adapter<GameCenterAdapter.MyViewHolder> {

    private Context mContext;
    private List<GameListBeans> gameListBeans;
    private LayoutInflater inflater;
    private int recommendWidth = 0, recommendHeight = 0;

    public GameCenterAdapter(Context mContext, List<GameListBeans> gameListBeans) {
        this.gameListBeans = gameListBeans;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.game_center_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GameListBeans gameListBeans1 = gameListBeans.get(position);
        //获取屏幕宽度
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        //计算高度宽高比
        recommendWidth = (int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(20);
        recommendHeight = (int) Math.ceil(recommendWidth * (354f / 710f));
        ViewGroup.LayoutParams bannerParams = holder.ivBg.getLayoutParams();
        bannerParams.height = recommendHeight;
        holder.ivBg.setLayoutParams(bannerParams);
        ImageLoaderManager.LoadImage(mContext, gameListBeans1.getPicture(), holder.ivBg, R.mipmap.zw01);
        holder.tvTitle.setText(gameListBeans1.getName());
        holder.llItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.llItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (gameListBeans == null) ? 0 : gameListBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_bg)
        OvalImageView ivBg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public MyViewHolder(View itemView) {
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
