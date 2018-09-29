package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AccountBean;

import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.TimeTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shuyun.qapp.utils.TimeTool.FORMAT_DATE_TIME_SECOND;

/**
 * 现金账户和积分账户流水 adapter
 */

public class AccountRecordAdapter extends RecyclerView.Adapter<AccountRecordAdapter.ViewHolder> {

    private int type;
    private Context context;
    private LayoutInflater layoutInflater;

    private List<AccountBean> accountRecordBeans;

    public AccountRecordAdapter(Context context, List<AccountBean> accountRecordBeans, int type) {
        this.context = context;
        this.type = type;
        layoutInflater = LayoutInflater.from(context);
        this.accountRecordBeans = accountRecordBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.account_record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountBean accountBean = accountRecordBeans.get(position);
        if (type == AppConst.ACCOUNT_INTEGRAL_TYPE) { //积分
            holder.ivStatus.setVisibility(View.GONE);
            if (!EncodeAndStringTool.isStringEmpty(accountBean.getName())) {
                holder.tvName.setText(accountBean.getName());
            } else {
                if (accountBean.getSource() == 5) {
                    holder.tvName.setText("答题对战");
                } else if (1 == accountBean.getWay()) {
                    holder.tvName.setText("拆宝箱");
                } else if (accountBean.getSource() == 6) {
                    holder.tvName.setText("积分夺宝");
                } else {
                    holder.tvName.setText("积分抽奖");
                }
            }
            //积分
            if (1 == accountBean.getWay()) {
                holder.tvNumber.setText("+" + accountBean.getAmount());
            } else if (2 == accountBean.getWay()) {
                holder.tvNumber.setText("-" + accountBean.getAmount());
            }
        } else if (type == AppConst.ACCOUNT_CASH_TYPE) {  //现金提现
            if (1 == accountBean.getWay()) {
                if (!EncodeAndStringTool.isStringEmpty(accountBean.getName())) {
                    holder.tvName.setText(accountBean.getName());
                } else {
                    holder.tvName.setText("拆宝箱");
                }
                if (3 == accountBean.getSource()) {
                    if (!EncodeAndStringTool.isStringEmpty(accountBean.getName())) {
                        holder.tvName.setText(accountBean.getName());
                    } else {
                        holder.tvName.setText("提现");
                    }
                    holder.ivStatus.setVisibility(View.VISIBLE);
                    holder.ivStatus.setImageResource(R.mipmap.fail);//
                    holder.tvName.setTextColor(context.getResources().getColor(R.color.color_15));
                    holder.tvNumber.setTextColor(context.getResources().getColor(R.color.color_15));
                    holder.tvDate.setTextColor(context.getResources().getColor(R.color.color_15));
                }
                holder.ivStatus.setVisibility(View.GONE);
            } else if (2 == accountBean.getWay()) {
                if (!EncodeAndStringTool.isStringEmpty(accountBean.getName())) {
                    holder.tvName.setText(accountBean.getName());
                } else {
                    holder.tvName.setText("提现");
                }
                if (1 == accountBean.getStatus()) {
                    holder.ivStatus.setVisibility(View.VISIBLE);
                    holder.ivStatus.setImageResource(R.mipmap.audit);//审核中
                } else if (2 == accountBean.getStatus()) {
                    holder.ivStatus.setVisibility(View.VISIBLE);
                    holder.ivStatus.setImageResource(R.mipmap.account);//已到账
                } else if (3 == accountBean.getStatus()) {//审核失败
                    holder.ivStatus.setVisibility(View.VISIBLE);
                    holder.ivStatus.setImageResource(R.mipmap.fail);//
                    holder.tvName.setTextColor(context.getResources().getColor(R.color.color_15));
                    holder.tvNumber.setTextColor(context.getResources().getColor(R.color.color_15));
                    holder.tvDate.setTextColor(context.getResources().getColor(R.color.color_15));
                } else {
                    holder.ivStatus.setVisibility(View.GONE);//默认状态
                }
            }

            if (1 == accountBean.getWay()) {
                holder.tvNumber.setText("+" + accountBean.getAmount() + "元");
            } else if (2 == accountBean.getWay()) {
                holder.tvNumber.setText("-" + accountBean.getAmount() + "元");
            }
        }

        //时间
        if (!accountBean.getTime().equals("0")) {
            String time = TimeTool.getCommTime(accountBean.getTime(), FORMAT_DATE_TIME_SECOND);
            holder.tvDate.setText(time);
        }

    }


    @Override
    public int getItemCount() {
        return accountRecordBeans == null ? 0 : accountRecordBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;//提现或者拆宝箱
        @BindView(R.id.tv_number)
        TextView tvNumber;//金额
        @BindView(R.id.tv_date)
        TextView tvDate;//时间格式
        @BindView(R.id.iv_status)
        ImageView ivStatus;//状态

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
