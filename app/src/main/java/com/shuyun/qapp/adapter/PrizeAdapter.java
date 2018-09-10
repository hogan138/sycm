package com.shuyun.qapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.TextviewUtil;
import com.shuyun.qapp.utils.TimeTool;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/4/25.
 * 奖品适配器
 */

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MinePrize> minePrizes;

    public PrizeAdapter(Context context, List<MinePrize> minePrizes) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.minePrizes = minePrizes;
        notifyDataSetChanged();
    }

    @Override
    public PrizeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.mine_prize_item1, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MinePrize minePrize = minePrizes.get(position);
        ImageLoaderManager.LoadImage(context, minePrize.getPicture(), holder.ivPrizeIcon, R.mipmap.zw02);
        if (minePrize.getActionType().equals("action.withdraw") || minePrize.getActionType().equals("action.use.record")) {
            holder.tvTitle.setText(minePrize.getName() + " " + minePrize.getAmount() + "元");
        } else {
            holder.tvTitle.setText(minePrize.getName());
        }

        if (minePrize.getStatus() == 1 && minePrize.getUpcomming() == 1) {
            holder.tvAlmostExpired.setVisibility(View.VISIBLE);
            holder.tvAlmostExpired.setText("快过期");
        } else {
            holder.tvAlmostExpired.setText("");
            holder.tvAlmostExpired.setVisibility(View.INVISIBLE);
        }

        //使用中
        if (!EncodeAndStringTool.isStringEmpty(minePrize.getSubstatusName()) && minePrize.getStatus() == 4) {
            holder.tvExpirationTime.setText(minePrize.getSubstatusName());
            holder.tvExpirationTime.setTextColor(Color.parseColor("#0194EC"));
        } else if (!EncodeAndStringTool.isStringEmpty(minePrize.getExpireTime()) && !minePrize.getExpireTime().equals("0") && minePrize.getStatus() != 4) {
            String time = TimeTool.getTime(minePrize.getExpireTime());
            holder.tvExpirationTime.setText("到期时间：" + time);//格式化时间
            holder.tvExpirationTime.setTextColor(Color.parseColor("#ED5A3F"));
        } else {
            holder.tvExpirationTime.setText("");
            holder.tvExpirationTime.setVisibility(View.VISIBLE);
        }

        holder.tvOpen.setText(minePrize.getActionTypeLabel());
        //按钮名称
        if (minePrize.getStatus() == 1 || minePrize.getStatus() == 4) {
            holder.tvOpen.setEnabled(true);
        } else {
            holder.tvOpen.setEnabled(false);
        }

        holder.tvContent.setText(TextviewUtil.ToDBC(minePrize.getDescription()));

        /**
         * 奖品状态为正常(未用,且未到期时才可以点击)
         */
        holder.tvOpen.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.tvOpen, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return minePrizes == null ? 0 : minePrizes.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_prize_icon)
        RoundImageView ivPrizeIcon;
        @BindView(R.id.tv_common_title)
        TextView tvTitle;
        @BindView(R.id.tv_expiration_time)
        TextView tvExpirationTime;
        @BindView(R.id.tv_almost_expired)
        TextView tvAlmostExpired;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_open)
        TextView tvOpen;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    OnItemChildClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemChildClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemChildClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }
}
