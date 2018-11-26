
package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnswerRecordBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shuyun.qapp.utils.TimeTool.getTime;

/**
 * Created by sunxiao on 2018/4/28.
 * 答题记录Adapter
 */

public class AnswerRecordAdapter extends RecyclerView.Adapter<AnswerRecordAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<AnswerRecordBean> answerRecordBeans;

    public AnswerRecordAdapter(Context context, List<AnswerRecordBean> answerRecordBeans) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.answerRecordBeans = answerRecordBeans;
        notifyDataSetChanged();
    }

    @Override
    public AnswerRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.answer_record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AnswerRecordBean answerRecordBean = answerRecordBeans.get(position);
        ImageLoaderManager.LoadImage(context, answerRecordBean.getPicture(), holder.ivBgImage, R.mipmap.zw01);
        if (!EncodeAndStringTool.isStringEmpty(answerRecordBean.getTitle())) {
            holder.tvTitle.setText(answerRecordBean.getTitle());
        }
        holder.tvAccuracy.setText("正确率:" + answerRecordBean.getAccuracy() + "%");
        if (!answerRecordBean.getTime().equals("0")) {
            holder.tvTime.setText("时间:" + getTime(answerRecordBean.getTime()));
        }
        if (!EncodeAndStringTool.isObjectEmpty(mOnItemChildClickLitsener)) {
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
        return answerRecordBeans == null ? 0 : answerRecordBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_bg_image)
        RoundImageView ivBgImage;
        @BindView(R.id.tv_common_title)
        TextView tvTitle;
        @BindView(R.id.tv_accuracy)
        TextView tvAccuracy;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    AnswerRecordAdapter.OnItemClickListener mOnItemChildClickLitsener;

    /**
     * 设置RecyclerView点击事件
     */
    public interface OnItemClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemClickLitsener(AnswerRecordAdapter.OnItemClickListener mOnItemChildClickLitsener) {
        this.mOnItemChildClickLitsener = mOnItemChildClickLitsener;
    }

}
