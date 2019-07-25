package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.TextviewUtil;
import com.shuyun.qapp.utils.TimeTool;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        //快到期
        if (minePrize.getStatus() == 1 && minePrize.getUpcomming() == 1) {
            holder.tvAlmostExpired.setVisibility(View.VISIBLE);
        } else {
            holder.tvAlmostExpired.setVisibility(View.GONE);
        }

        //使用中状态
        if (!EncodeAndStringTool.isStringEmpty(minePrize.getSubstatusName()) && minePrize.getStatus() == 4) {
            holder.tvUseStatus.setVisibility(View.VISIBLE);
            holder.tvUseStatus.setText(minePrize.getSubstatusName());
        } else {
            holder.tvUseStatus.setVisibility(View.GONE);
        }

        //显示到期时间
        if (!EncodeAndStringTool.isStringEmpty(minePrize.getExpireTime()) && !minePrize.getExpireTime().equals("0") && minePrize.getStatus() != 4) {
            holder.tvExpirationTime.setVisibility(View.VISIBLE);
            String time = TimeTool.getTimeMill(minePrize.getExpireTime());
            holder.tvExpirationTime.setText("到期日期：" + time);//格式化时间
        } else {
            holder.tvExpirationTime.setVisibility(View.GONE);
        }

        //按钮名称
        holder.tvOpen.setText(minePrize.getActionTypeLabel());
        if (minePrize.getStatus() == 1 || minePrize.getStatus() == 4) {
            if ("action.default".equals(minePrize.getActionType())) {
                holder.tvOpen.setEnabled(false);
            } else {
                holder.tvOpen.setEnabled(true);
            }
            holder.tvUseLogo.setVisibility(View.GONE);
        } else {
            holder.tvUseLogo.setVisibility(View.VISIBLE);
            if ("action.alipay.coupon".equals(minePrize.getActionType()) && minePrize.getStatus() == 2) {
                holder.tvOpen.setEnabled(true);
            } else {
                holder.tvOpen.setEnabled(false);
            }
        }

        //内容
        holder.tvContent.setText(TextviewUtil.ToDBC(minePrize.getDescription()));

        //是否显示锁遮罩层
        Long lock = minePrize.getLock();
        if (lock == 0) {
            holder.rlShadow.setVisibility(View.GONE);
        } else if (lock == 1) {
            holder.rlShadow.setVisibility(View.VISIBLE);
        }

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
        @BindView(R.id.tv_use_status)
        TextView tvUseStatus;
        @BindView(R.id.tv_use_logo)
        TextView tvUseLogo;
        @BindView(R.id.rl_shadow)
        RelativeLayout rlShadow;//锁遮罩层

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
