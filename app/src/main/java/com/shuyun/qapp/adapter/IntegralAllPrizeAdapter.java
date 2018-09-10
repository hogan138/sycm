package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.IntegralAllPrizeBean;
import com.shuyun.qapp.ui.integral.PrizeDetailsActivity;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RoundImageView;
import com.shuyun.qapp.view.RushBuyCountDownTimerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分夺宝全部奖品
 */

public class IntegralAllPrizeAdapter extends RecyclerView.Adapter<IntegralAllPrizeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<IntegralAllPrizeBean> integralAllPrizeBeanList;

    public IntegralAllPrizeAdapter(Context context, List<IntegralAllPrizeBean> integralAllPrizeBeanList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.integralAllPrizeBeanList = integralAllPrizeBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_all_score_gift, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IntegralAllPrizeBean integralAllPrizeBean = integralAllPrizeBeanList.get(position);
        GlideUtils.LoadImage(context, integralAllPrizeBean.getMainPic(), holder.ivPicture);
        holder.tvName.setText(integralAllPrizeBean.getPrizeName());
        holder.tvReduceScore.setText("消耗" + integralAllPrizeBean.getBpcons() + "积分");
        holder.tvCount.setText(integralAllPrizeBean.getCount() + "份");
        holder.tvNumber.setText("期号：" + integralAllPrizeBean.getSchedule());
        holder.tvPersonCount.setText("参与人数：" + integralAllPrizeBean.getParticipate());
        holder.tvDate.addTime((int) TimeUtils.getTimeSpan(integralAllPrizeBean.getEndTime(), System.currentTimeMillis(), 1000));
        holder.tvDate.start();

        holder.rlItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //保存h5url
                SaveUserInfo.getInstance(context).setUserInfo("h5_url_prize", integralAllPrizeBean.getH5Url());
                context.startActivity(new Intent(context, PrizeDetailsActivity.class).putExtra("scheduleId", integralAllPrizeBean.getScheduleId() + ""));
            }
        });
    }


    @Override
    public int getItemCount() {
        return integralAllPrizeBeanList == null ? 0 : integralAllPrizeBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        RoundImageView ivPicture;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date)
        RushBuyCountDownTimerView tvDate;
        @BindView(R.id.tv_reduce_score)
        TextView tvReduceScore;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_person_count)
        TextView tvPersonCount;
        @BindView(R.id.tv_start_getbaby)
        TextView tvStartGetbaby;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
