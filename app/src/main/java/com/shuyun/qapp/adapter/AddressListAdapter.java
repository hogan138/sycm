package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AddressListBeans;
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

    private List<AddressListBeans> addressListBeansList;

    public AddressListAdapter(Context context, List<AddressListBeans> addressListBeansList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.addressListBeansList = addressListBeansList;
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

        AddressListBeans addressListBeans = addressListBeansList.get(position);
        holder.tvName.setText(addressListBeans.getUserName());
        holder.tvNumber.setText(addressListBeans.getUserPhone());
        StringBuilder sb = new StringBuilder();
        sb.append(addressListBeans.getProvinceName());
        if (!StringUtil.isBlank(addressListBeans.getCityName())) {
            sb.append(" ").append(addressListBeans.getCityName());
        }
        if (!StringUtil.isBlank(addressListBeans.getCountyName())) {
            sb.append(" ").append(addressListBeans.getCountyName());
        }
        sb.append(" ").append(addressListBeans.getDetail());
        String address = sb.toString();
        holder.tvAddress.setText(address);

        //是否显示默认地址
        Long isDefault = addressListBeans.getIsDefault();
        if (isDefault == 1) {
            holder.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefault.setVisibility(View.GONE);
        }


        //最后一条隐藏下划线
        if (position == addressListBeansList.size() - 1) {
            holder.viewLine.setVisibility(View.GONE);
        } else {
            holder.viewLine.setVisibility(View.VISIBLE);
        }

        //点击编辑
        holder.tvEditor.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnEditorClickListener.onEditorClick(holder.tvEditor, position);
            }
        });

        //点击地址信息
        holder.llItem.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.llItem, position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return addressListBeansList == null ? 0 : addressListBeansList.size();
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
        @BindView(R.id.tv_editor)
        TextView tvEditor;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    //编辑
    OnEditorClickListener mOnEditorClickListener;

    /**
     * 设置编辑点击事件
     */
    public interface OnEditorClickListener {
        void onEditorClick(View view, int position);
    }

    public void setEditorClickLitsener(OnEditorClickListener onEditorClickListener) {
        this.mOnEditorClickListener = onEditorClickListener;
    }


    /**
     * 设置item点击事件
     */
    OnItemClickListener mOnItemChildClickLitsener;

    /**
     * 设置编辑点击事件
     */
    public interface OnItemClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(OnItemClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }


}
