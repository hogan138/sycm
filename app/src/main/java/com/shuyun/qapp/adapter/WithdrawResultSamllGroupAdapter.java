package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.WithdrawResultGroupBean;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.OvalImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 提现结果小图适配器
 */

public class WithdrawResultSamllGroupAdapter extends RecyclerView.Adapter<WithdrawResultSamllGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<WithdrawResultGroupBean.SmallBean> smallBeanList;
    private LayoutInflater layoutInflater;
    private int wdith = 0, height = 0; //宽高

    public WithdrawResultSamllGroupAdapter(List<WithdrawResultGroupBean.SmallBean> smallBeanList, Context mContext) {
        this.smallBeanList = smallBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.withdraw_result_small_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WithdrawResultGroupBean.SmallBean smallBean = smallBeanList.get(position);
        try {
            holder.tvGroupName.setText(smallBean.getName());

            ViewGroup.LayoutParams Params = holder.ivGroupBg.getLayoutParams();
            //获取屏幕宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //计算图片高度宽高比
            wdith = (((int) Math.ceil(dm.widthPixels) - ConvertUtils.dp2px(10))) / 2;
            height = (int) (Math.ceil(wdith * (100f / 173f)));
            Params.height = height;
            holder.ivGroupBg.setLayoutParams(Params);

            ImageLoaderManager.LoadImage(mContext, smallBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            holder.tvButtonName.setText(smallBean.getActionLabel());


            holder.rlItem.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    MyActivityManager1.getInstance().finishAllActivity();
                    ActionJumpUtil.dialogSkip(smallBean.getAction(), (Activity) mContext, smallBean.getContent(), smallBean.getH5Url(), 0L);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (smallBeanList == null) ? 0 : smallBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        OvalImageView ivGroupBg;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_button_name)
        TextView tvButtonName;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
