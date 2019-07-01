package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.WithdrawResultGroupBean;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 提现结果大图适配器
 */

public class WithdrawResultBigGroupAdapter extends RecyclerView.Adapter<WithdrawResultBigGroupAdapter.ViewHolder> {

    private Context mContext;
    //题组分类集合
    private List<WithdrawResultGroupBean.BigBean> bigBeanList;
    private LayoutInflater layoutInflater;
    private int wdith = 0, height = 0; //宽高

    public WithdrawResultBigGroupAdapter(List<WithdrawResultGroupBean.BigBean> bigBeanList, Context mContext) {
        this.bigBeanList = bigBeanList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.withdraw_result_big_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WithdrawResultGroupBean.BigBean bigBean = bigBeanList.get(position);
        try {

            ViewGroup.LayoutParams Params = holder.ivGroupBg.getLayoutParams();
            //获取屏幕宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //计算图片高度宽高比
            wdith = ((int) Math.ceil(dm.widthPixels) - ConvertUtils.dp2px(20));
            height = (int) (Math.ceil(wdith * (139f / 355f)));
            Params.height = height;
            holder.ivGroupBg.setLayoutParams(Params);
            ImageLoaderManager.LoadImage(mContext, bigBean.getPicture(), holder.ivGroupBg, R.mipmap.zw01);

            holder.ivGroupBg.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    MyActivityManager1.getInstance().finishAllActivity();
                    ActionJumpUtil.dialogSkip(bigBean.getAction(), (Activity) mContext, bigBean.getContent(), bigBean.getH5Url(), 0L);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (bigBeanList == null) ? 0 : bigBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_group_bg)
        RoundImageView ivGroupBg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
