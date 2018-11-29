package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.BoxRecordBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 宝箱记录Adapter
 */

public class BoxRecordAdapter extends RecyclerView.Adapter<BoxRecordAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<BoxRecordBean> boxRecordBeanList;

    public BoxRecordAdapter(Context context, List<BoxRecordBean> boxRecordBeanList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.boxRecordBeanList = boxRecordBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_box_record, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BoxRecordBean boxRecordBean = boxRecordBeanList.get(position);
        holder.tvDate.setText(boxRecordBean.getBoxTime());
        holder.tvTitle.setText(boxRecordBean.getTitle());
        holder.tvRemark.setText(Html.fromHtml(boxRecordBean.getRemark()));
        if (position == 0) {
            holder.ivLine.setBackgroundResource(R.mipmap.box_record_top);
        } else if (position == boxRecordBeanList.size() - 1) {
            holder.ivLine.setBackgroundResource(R.mipmap.box_record_bottom);
        } else {
            holder.ivLine.setBackgroundResource(R.mipmap.box_record_middle);
        }

        holder.itemView.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.itemView, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return boxRecordBeanList == null ? 0 : boxRecordBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_line)
        ImageView ivLine;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_remark)
        TextView tvRemark;

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
