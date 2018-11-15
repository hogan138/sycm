package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.TimeTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息适配器
 */

public class InfomationAdapter extends RecyclerView.Adapter<InfomationAdapter.MyViewHolder> {
    //题组分类集合
    private List<Msg> msgList;
    private LayoutInflater inflater;

    public InfomationAdapter(List<Msg> msgList, Context mContext) {
        this.msgList = msgList;
        inflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    @Override
    public InfomationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.infomation_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final InfomationAdapter.MyViewHolder holder, int position) {
        Msg msg = msgList.get(position);

        holder.tvTitle.setText(msg.getTitle());//标题
        if (0 != msg.getTime()) {
            holder.tvDate.setText(TimeTool.getTime(msg.getTime() + ""));//消息时间
        }
        holder.tvInfoContent.setText(msg.getContent());//消息内容
        if (1 == msg.getStatus()) {//消息转态:1、未读转态;2、已读
            holder.ivInfoStatus.setImageResource(R.mipmap.messagw);
        } else if (2 == msg.getStatus()) {
            holder.ivInfoStatus.setVisibility(View.GONE);
        }
        //设置消息点击事件
        if ((!EncodeAndStringTool.isObjectEmpty(mOnItemChildClickLitsener))) {
            holder.itemView.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemChildClickLitsener.onItemChildClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (msgList == null) ? 0 : msgList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_common_title)
        TextView tvTitle;//消息的标题
        @BindView(R.id.tv_date)
        TextView tvDate;//消息时间
        @BindView(R.id.tv_info_content)
        TextView tvInfoContent;//消息的内容
        @BindView(R.id.iv_info_status)
        ImageView ivInfoStatus;//未读消息

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    InfomationAdapter.OnItemClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(InfomationAdapter.OnItemClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }
}
