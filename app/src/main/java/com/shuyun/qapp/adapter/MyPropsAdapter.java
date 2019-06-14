package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MyPropsBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的道具适配器
 */

public class MyPropsAdapter extends RecyclerView.Adapter<MyPropsAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MyPropsBean> myPropsBeans;

    public MyPropsAdapter(Context context, List<MyPropsBean> myPropsBeans) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.myPropsBeans = myPropsBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_my_props, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MyPropsBean myPropsBean = myPropsBeans.get(position);
        ImageLoaderManager.LoadImage(context, myPropsBean.getPrizePicture(), holder.ivIcon, R.mipmap.zw02);
        holder.tvTitle.setText(myPropsBean.getPrizeName());
        holder.tvCount.setText("剩余" + myPropsBean.getCount());
        holder.tvContent.setText(myPropsBean.getExplain());
        holder.tvRemark.setText(Html.fromHtml(myPropsBean.getRemark()));

        if (!EncodeAndStringTool.isStringEmpty(myPropsBean.getAction())) {
            holder.tvOpen.setVisibility(View.VISIBLE);
            //按钮名称
            holder.tvOpen.setText(myPropsBean.getActionLabel());
        } else {
            holder.tvOpen.setVisibility(View.GONE);
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
        return myPropsBeans == null ? 0 : myPropsBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        RoundImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
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
