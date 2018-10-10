package com.shuyun.qapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunxiao on 2018/4/25.
 * 右侧题组分类适配器
 */

public class ChildrenGroupAdapter extends RecyclerView.Adapter<ChildrenGroupAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<GroupClassifyBean.ChildrenBean> childrenBeans;

    public ChildrenGroupAdapter(Context context, List<GroupClassifyBean.ChildrenBean> childrenBeans) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.childrenBeans = childrenBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.children_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GroupClassifyBean.ChildrenBean childrenBean = childrenBeans.get(position);
        ImageLoaderManager.LoadImage(context, childrenBean.getPicture(), holder.ivGroup, R.mipmap.zw01);//题组图片
        if (0 == childrenBean.getOpportunity()) {
            holder.tvConsumeAnswerNum.setVisibility(View.VISIBLE);
            holder.tvConsumeAnswerNum.setText("不消耗答题次数");
        } else {
            holder.tvConsumeAnswerNum.setVisibility(View.GONE);
        }

        if (childrenBean.getGuideId() != 0) {
            holder.tvSolvingStrategy.setVisibility(View.VISIBLE);
            holder.tvSolvingStrategy.setText("有答题攻略");
        } else {
            holder.tvSolvingStrategy.setVisibility(View.GONE);
        }

        holder.tvTheme.setText(childrenBean.getName());//题组名称

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
        return childrenBeans == null ? 0 : childrenBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_group)
        RoundImageView ivGroup;//题组图片
        @BindView(R.id.tv_consume_answer_num)
        TextView tvConsumeAnswerNum;//不消耗答题次数
        @BindView(R.id.tv_solving_strategy)
        TextView tvSolvingStrategy;//有答题攻略 TODO
        @BindView(R.id.tv_theme)
        TextView tvTheme;//题组名字
        @BindView(R.id.tv_score)
        TextView tvScore; //获得积分
        @BindView(R.id.tv_cash)
        TextView tvCash; //获得现金
        @BindView(R.id.tv_right_number)
        TextView tvRightNumber; //正确率

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
