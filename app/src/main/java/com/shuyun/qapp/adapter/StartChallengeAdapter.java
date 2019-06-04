package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.ui.integral.IntegralCenterActivity;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开始挑战适配器
 */

public class StartChallengeAdapter extends RecyclerView.Adapter<StartChallengeAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<NewHomeSelectBean.GuessBean> guessBeanList;

    public StartChallengeAdapter(Context context, List<NewHomeSelectBean.GuessBean> guessBeanList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.guessBeanList = guessBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_start_challenge, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        NewHomeSelectBean.GuessBean guessBean = guessBeanList.get(position);

        if (position > 0) {
            holder.rlLock.setVisibility(View.VISIBLE);
            holder.rlStart.setEnabled(false);
            holder.tvCurrentLevel.setVisibility(View.GONE);
        } else {
            holder.rlLock.setVisibility(View.GONE);
            holder.rlStart.setEnabled(true);
            holder.tvCurrentLevel.setVisibility(View.VISIBLE);
        }

        holder.llAddStar.removeAllViews();

        if (position > 6) {
            for (int i = -1; i < 6; i++) {
                holder.llAddStar.setGravity(Gravity.CENTER_VERTICAL);
                ImageView imageView = new ImageView(context);
                int imageWidth = ConvertUtils.dp2px(27);
                int imageHeight = ConvertUtils.dp2px(27);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                imageView.setPadding(ConvertUtils.dp2px(7), 0, 0, 0);
                imageView.setImageResource(R.mipmap.start_logo_gray);
                holder.llAddStar.addView(imageView);
            }
        } else {
            for (int i = -1; i < position; i++) {
                holder.llAddStar.setGravity(Gravity.CENTER_VERTICAL);
                ImageView imageView = new ImageView(context);
                int imageWidth = ConvertUtils.dp2px(27);
                int imageHeight = ConvertUtils.dp2px(27);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                imageView.setPadding(ConvertUtils.dp2px(7), 0, 0, 0);
                if (i < 0 - 1) {
                    imageView.setImageResource(R.mipmap.start_logo_yellow);
                } else {
                    imageView.setImageResource(R.mipmap.start_logo_gray);
                }

                holder.llAddStar.addView(imageView);
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
        holder.rlStart.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemChildClickLitsener.onItemChildClick(holder.rlStart, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return guessBeanList == null ? 0 : guessBeanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_start)
        RelativeLayout rlStart;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.rl_lock)
        RelativeLayout rlLock;
        @BindView(R.id.tv_current_level)
        TextView tvCurrentLevel;
        @BindView(R.id.ll_add_star)
        LinearLayout llAddStar;

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
