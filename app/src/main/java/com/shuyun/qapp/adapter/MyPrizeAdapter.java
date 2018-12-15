package com.shuyun.qapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.ExchangeMyPrizeBean;
import com.shuyun.qapp.ui.integral.PrizeDetailsActivity;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RushBuyCountDownTimerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的奖券适配器
 */

public class MyPrizeAdapter extends RecyclerView.Adapter<MyPrizeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<ExchangeMyPrizeBean> exchangeMyPrizeBeanList;

    public MyPrizeAdapter(Context context, List<ExchangeMyPrizeBean> exchangeMyPrizeBeanList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.exchangeMyPrizeBeanList = exchangeMyPrizeBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_my_prize, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExchangeMyPrizeBean exchangeMyPrizeBean = exchangeMyPrizeBeanList.get(position);
        GlideUtils.LoadImage(context, exchangeMyPrizeBean.getMainPic(), holder.ivPicture);
        if (exchangeMyPrizeBean.getScheduleStatus() == 0) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//年-月-日 时-分
            try {
                long time1 = dateFormat.parse(TimeUtils.millis2String(exchangeMyPrizeBean.getEndTime())).getTime();
                long time2 = dateFormat.parse(TimeUtils.millis2String(System.currentTimeMillis())).getTime();
                if (time1 > time2) {
                    holder.tvDate.setVisibility(View.VISIBLE);
                    holder.tvOpenDate.setVisibility(View.GONE);
                    //待开奖
                    holder.tvDate.addTime((int) TimeUtils.getTimeSpan(exchangeMyPrizeBean.getEndTime(), System.currentTimeMillis(), 1000));
                    holder.tvDate.start();
                } else if (time1 < time2) {
                    holder.tvDate.setVisibility(View.GONE);
                    holder.tvOpenDate.setVisibility(View.VISIBLE);
                    holder.tvOpenDate.setText("已开奖：" + TimeUtils.millis2String(exchangeMyPrizeBean.getEndTime()).replace("-", "/"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tvOpenStatus.setText("待开奖");
            holder.tvOpenStatus.setBackgroundResource(R.drawable.common_prize_status_bg1);
        } else {
            //已开奖
            holder.tvDate.setVisibility(View.GONE);
            holder.tvOpenDate.setVisibility(View.VISIBLE);
            holder.tvOpenDate.setText("已开奖：" + TimeUtils.millis2String(exchangeMyPrizeBean.getEndTime()).replace("-", "/"));
            holder.tvOpenStatus.setText("已开奖");
            holder.tvOpenStatus.setBackgroundResource(R.drawable.common_prize_status_bg2);
        }
        holder.tvName.setText(exchangeMyPrizeBean.getPrizeName());
        holder.tvCount.setText("拥有" + exchangeMyPrizeBean.getUserTicketCount() + "张奖券");
        holder.tvNumber.setText("期号：" + exchangeMyPrizeBean.getSchedule());
        holder.tvPersonCount.setText("参与人数：" + exchangeMyPrizeBean.getParticipate());


        holder.rlItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //保存h5url
                SaveUserInfo.getInstance(context).setUserInfo("h5_url_prize", exchangeMyPrizeBean.getH5Url());
                context.startActivity(new Intent(context, PrizeDetailsActivity.class).putExtra("scheduleId", exchangeMyPrizeBean.getScheduleId() + ""));
            }
        });
    }


    @Override
    public int getItemCount() {
        return exchangeMyPrizeBeanList == null ? 0 : exchangeMyPrizeBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_date)
        RushBuyCountDownTimerView tvDate;
        @BindView(R.id.tv_open_date)
        TextView tvOpenDate;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_person_count)
        TextView tvPersonCount;
        @BindView(R.id.tv_look_detail)
        TextView tvLookDetail;
        @BindView(R.id.tv_open_status)
        TextView tvOpenStatus;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
