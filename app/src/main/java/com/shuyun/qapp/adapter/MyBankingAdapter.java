package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.MyPropsBean;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.view.CircleImageView;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的排名适配器
 */

public class MyBankingAdapter extends RecyclerView.Adapter<MyBankingAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<NewHomeSelectBean.GuessBean> guessBeanList;

    public MyBankingAdapter(Context context, List<NewHomeSelectBean.GuessBean> guessBeanList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.guessBeanList = guessBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_my_banking, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        NewHomeSelectBean.GuessBean guessBean = guessBeanList.get(position);
        if (position == 0) {
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setImageResource(R.mipmap.item_banking_one_logo);
        } else if (position == 1) {
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setImageResource(R.mipmap.item_banking_two_logo);
        } else if (position == 2) {
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.ivLogo.setImageResource(R.mipmap.item_banking_three_logo);
        } else {
            holder.ivLogo.setVisibility(View.GONE);
            if (position < 9) {
                holder.tvIndex.setText("0" + (position + 1));
            } else {
                holder.tvIndex.setText((position + 1) + "");
            }
        }
//        ImageLoaderManager.LoadImage(context, myPropsBean.getPrizePicture(), holder.ivIcon, R.mipmap.zw02);
//        holder.tvTitle.setText(myPropsBean.getPrizeName());
//        holder.tvCount.setText("剩余" + myPropsBean.getCount());
//        holder.tvContent.setText(myPropsBean.getExplain());
//        holder.tvRemark.setText(Html.fromHtml(myPropsBean.getRemark()));
//
//        if (!EncodeAndStringTool.isStringEmpty(myPropsBean.getAction())) {
//            holder.tvOpen.setVisibility(View.VISIBLE);
//            //按钮名称
//            holder.tvOpen.setText(myPropsBean.getActionLabel());
//        } else {
//            holder.tvOpen.setVisibility(View.GONE);
//        }
//
//        holder.tvOpen.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                int position = holder.getLayoutPosition();
//                mOnItemChildClickLitsener.onItemChildClick(holder.tvOpen, position);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return guessBeanList == null ? 0 : guessBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_index)
        TextView tvIndex;
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.iv_header_pic)
        RoundImageView ivHeaderPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_star)
        TextView tvStar;

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
