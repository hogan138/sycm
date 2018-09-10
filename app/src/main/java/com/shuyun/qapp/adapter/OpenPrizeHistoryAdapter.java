package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.PrizeHistoryBean;
import com.shuyun.qapp.ui.integral.PrizeDetailsActivity;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开奖历史适配器
 */

public class

OpenPrizeHistoryAdapter extends RecyclerView.Adapter<OpenPrizeHistoryAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<PrizeHistoryBean> prizeHistoryBeanList;

    public OpenPrizeHistoryAdapter(Context context, List<PrizeHistoryBean> prizeHistoryBeanList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.prizeHistoryBeanList = prizeHistoryBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_open_prize_history, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PrizeHistoryBean prizeHistoryBean = prizeHistoryBeanList.get(position);
        GlideUtils.LoadImage(context, prizeHistoryBean.getMainPic(), holder.ivPicture);
        holder.tvName.setText(prizeHistoryBean.getPrizeName());
        holder.tvDate.setText("开奖时间：" + TimeUtils.millis2String(prizeHistoryBean.getEndTime()).replace("-", "/"));
        holder.tvNumber.setText("期号：" + prizeHistoryBean.getSchedule());
//        holder.tvWinnerNo.setText("中奖奖券：" + prizeHistoryBean.getTicketNum());


        holder.llItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //保存h5url
                SaveUserInfo.getInstance(context).setUserInfo("h5_url_prize", prizeHistoryBean.getH5Url());
                context.startActivity(new Intent(context, PrizeDetailsActivity.class).putExtra("scheduleId", prizeHistoryBean.getScheduleId() + ""));
            }
        });

    }


    @Override
    public int getItemCount() {
        return prizeHistoryBeanList == null ? 0 : prizeHistoryBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        RoundImageView ivPicture;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_winner_no)
        TextView tvWinnerNo;
        @BindView(R.id.tv_start_getbaby)
        TextView tvStartGetbaby;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
