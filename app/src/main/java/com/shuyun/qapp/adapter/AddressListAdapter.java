package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.BoxRecordBean;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收货地址Adapter
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<BoxRecordBean> boxRecordBeanList;

    public AddressListAdapter(Context context, List<BoxRecordBean> boxRecordBeanList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.boxRecordBeanList = boxRecordBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_address_info, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        BoxRecordBean boxRecordBean = boxRecordBeanList.get(position);
//        holder.tvDate.setText(boxRecordBean.getBoxTime());


        //最后一条隐藏下划线
        if (position == boxRecordBeanList.size() - 1) {
            holder.viewLine.setVisibility(View.GONE);
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_default)
        TextView tvDefault;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.view_line)
        View viewLine;

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
