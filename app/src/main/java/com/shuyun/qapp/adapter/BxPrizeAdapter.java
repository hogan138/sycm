package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.TextviewUtil;
import com.shuyun.qapp.utils.TimeTool;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/5/17.
 */

public class BxPrizeAdapter extends RecyclerView.Adapter<BxPrizeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<MinePrize> prizeBeans;

    public BxPrizeAdapter(Context context, List<MinePrize> prizeBeans) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.prizeBeans = prizeBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bx_prize, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MinePrize prize = prizeBeans.get(position);

        //名字显示
        if (prize.getType() == 1 || prize.getType() == 7) {
            //红包
            holder.tvTitle2.setText(prize.getName() + "  " + prize.getAmount() + "元");//标题
        } else if (prize.getType() == 2) {
            //积分
            String amount = prize.getAmount();
            if (prize.getAmount().contains(".")) {
                amount = prize.getAmount().substring(0, prize.getAmount().indexOf('.'));
            }
            holder.tvTitle2.setText(prize.getName() + "  " + amount);
        } else {
            holder.tvTitle2.setText(prize.getName());
        }

        //按钮显示
        if (prize.getActionType().equals("action.withdraw")) {
            holder.tvOpen.setVisibility(View.VISIBLE);
            holder.tvOpen.setText(prize.getActionTypeLabel());
        } else if (prize.getActionType().equals("action.bp.use")) {
            holder.tvOpen.setVisibility(View.VISIBLE);
            holder.tvOpen.setText(prize.getActionTypeLabel());
        } else if (prize.getActionType().equals("action.h5.url")) {
            holder.tvOpen.setVisibility(View.VISIBLE);
            holder.tvOpen.setText(prize.getActionTypeLabel());
        } else {
            holder.tvOpen.setVisibility(View.GONE);
        }

        String time = TimeTool.getTime(prize.getExpireTime());
        if (prize.getExpireTime().equals("0")) {
            holder.tvDate.setVisibility(View.INVISIBLE);
        } else {
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText("到期时间：" + time);
        }

        if (!EncodeAndStringTool.isStringEmpty(prize.getDescription())) {
            holder.tvContent.setText(TextviewUtil.ToDBC(prize.getDescription()));//奖品描述
        }
        //奖品图片
        ImageLoaderManager.LoadImage(context, prize.getPicture(), holder.ivBxPrizePic, R.mipmap.zw02);

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
        return prizeBeans == null ? 0 : prizeBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_bx_prize_pic)
        RoundImageView ivBxPrizePic;//奖品图片
        @BindView(R.id.tv_title2)
        TextView tvTitle2;//奖品名称
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        TextView tvContent;//奖品描述
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
