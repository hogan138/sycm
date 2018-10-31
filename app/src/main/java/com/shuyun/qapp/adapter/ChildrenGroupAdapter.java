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
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
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

        try {
            ImageLoaderManager.LoadImage(context, childrenBean.getPicture(), holder.ivGroup, R.mipmap.zw01);//题组图片
            if (!EncodeAndStringTool.isStringEmpty(childrenBean.getMerchantName())) {
                holder.tvCompany.setVisibility(View.VISIBLE);
                holder.tvCompany.setText("出题方：" + childrenBean.getMerchantName()); //出题方
            } else {
                holder.tvCompany.setVisibility(View.GONE);
            }

            if (childrenBean.isRecommend()) { //推荐标签
                holder.recommendLogo.setVisibility(View.VISIBLE);
            } else {
                holder.recommendLogo.setVisibility(View.GONE);
            }

            if (!EncodeAndStringTool.isStringEmpty(childrenBean.getRemark())) {
                if (childrenBean.isRecommend()) { //标签
                    holder.tvTag.setVisibility(View.VISIBLE);
                    holder.tvTag1.setVisibility(View.GONE);
                    holder.tvTag.setText(childrenBean.getRemark().replaceAll(" ", "\n"));
                } else {
                    holder.tvTag.setVisibility(View.GONE);
                    holder.tvTag1.setVisibility(View.VISIBLE);
                    holder.tvTag1.setText(childrenBean.getRemark().replaceAll(" ", "\n"));
                }
            } else {
                holder.tvTag.setVisibility(View.GONE);
                holder.tvTag1.setVisibility(View.GONE);
            }

            holder.tvTitle.setText(childrenBean.getName()); //题组名称
            if ((!EncodeAndStringTool.isObjectEmpty(mOnItemChildClickLitsener))) {
                holder.itemView.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnItemChildClickLitsener.onItemChildClick(holder.itemView, position);
                    }
                });
            }
        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return childrenBeans == null ? 0 : childrenBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_group)
        OvalImageView ivGroup;//题组图片
        @BindView(R.id.tv_company)
        TextView tvCompany; //出题方
        @BindView(R.id.recommend_logo)
        TextView recommendLogo; //推荐标签
        @BindView(R.id.tv_tag)
        TextView tvTag; //答题攻略
        @BindView(R.id.tv_title)
        TextView tvTitle; //标题
        @BindView(R.id.tv_tag1)
        TextView tvTag1; //标题1

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
