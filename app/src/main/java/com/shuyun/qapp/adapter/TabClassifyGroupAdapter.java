package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.HomeTabContentBean;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * tab 类题组
 */

public class TabClassifyGroupAdapter extends RecyclerView.Adapter<TabClassifyGroupAdapter.ViewHolder> {
    private Activity mContext;
    //题组分类集合
    private List<HomeTabContentBean.ContentsBean.DataBean.DatasBean> dataBeanList;
    private LayoutInflater layoutInflater;
    private int count = 0; //行个数
    private int wdith = 0, height = 0; //宽高
    private Double scale; //宽高比

    public TabClassifyGroupAdapter(List<HomeTabContentBean.ContentsBean.DataBean.DatasBean> dataBeanList, Activity mContext, int count, Double scale) {
        this.dataBeanList = dataBeanList;
        this.mContext = mContext;
        this.count = count;
        this.scale = scale;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_tab_classify_group, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HomeTabContentBean.ContentsBean.DataBean.DatasBean dataBean = dataBeanList.get(position);
        try {
            holder.tvGroupName.setText(dataBean.getTitle());
            ViewGroup.LayoutParams Params = holder.ivGroupBg.getLayoutParams();
            //获取屏幕宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //计算图片高度宽高比
            wdith = (((int) Math.ceil(dm.widthPixels) - ((count - 1) * ConvertUtils.dp2px(5)))) / count;
            height = (int) (Math.ceil(wdith / scale));
            Params.height = height;
            holder.ivGroupBg.setLayoutParams(Params);

            ImageLoaderManager.LoadImage(mContext, dataBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);
            holder.tvButtonName.setText(dataBean.getButton());

            final String action = dataBean.getAction();
            holder.rlGroup.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    ActionJumpUtil.dialogSkip(action, mContext, dataBean.getContent(), dataBean.getH5Url(), 0L);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (dataBeanList == null) ? 0 : dataBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_group_bg)
        ImageView ivGroupBg;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_button_name)
        TextView tvButtonName;
        @BindView(R.id.rl_group)
        RelativeLayout rlGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
