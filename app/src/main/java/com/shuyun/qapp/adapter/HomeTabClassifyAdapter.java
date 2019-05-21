package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.HomeTabContentBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.view.GridSpacingItemDecoration;
import com.shuyun.qapp.view.ActionJumpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页tab内容
 */

public class HomeTabClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    //题组分类集合
    private List<HomeTabContentBean.ContentsBean> contentsBeanList;

    public HomeTabClassifyAdapter(List<HomeTabContentBean.ContentsBean> contentsBeanList, Activity mContext) {
        this.contentsBeanList = contentsBeanList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_home_tab_classify, parent, false);
        RecyclerView.ViewHolder holder = new MyViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final HomeTabContentBean.ContentsBean contentsBean = contentsBeanList.get(position);
        try {
            ViewGroup.LayoutParams bannerParams = ((MyViewHolder) holder).ivTopBg.getLayoutParams();
            bannerParams.height = ConvertUtils.dp2px(contentsBean.getHead().getHeight());
            ((MyViewHolder) holder).ivTopBg.setLayoutParams(bannerParams);
            ImageLoaderManager.LoadImage(mContext, contentsBean.getHead().getPicture(), ((MyViewHolder) holder).ivTopBg, R.mipmap.zw01);
            final String action = contentsBean.getHead().getAction();
            ((MyViewHolder) holder).ivTopBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //顶部图片点击事件
                    ActionJumpUtil.dialogSkip(action, mContext, contentsBean.getHead().getContent(), contentsBean.getHead().getH5Url(), 0L);
                }
            });

            List<HomeTabContentBean.ContentsBean.DataBean.DatasBean> dataBeanList = contentsBean.getData().getDatas();
            if (!EncodeAndStringTool.isListEmpty(dataBeanList)) {
                try {
                    //解决数据加载不完的问题
                    ((MyViewHolder) holder).rvClassifyGroup.setHasFixedSize(true);
                    ((MyViewHolder) holder).rvClassifyGroup.setNestedScrollingEnabled(false);
                    TabClassifyGroupAdapter foundGroupAdapter = new TabClassifyGroupAdapter(dataBeanList, mContext, contentsBean.getData().getRowNum(), contentsBean.getData().getScale());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, contentsBean.getData().getRowNum()) {
                        @Override
                        public boolean canScrollVertically() {//禁止layout垂直滑动
                            return false;
                        }
                    };
                    gridLayoutManager.setSmoothScrollbarEnabled(true);
                    gridLayoutManager.setAutoMeasureEnabled(true);
                    ((MyViewHolder) holder).rvClassifyGroup.setLayoutManager(gridLayoutManager);
                    ((MyViewHolder) holder).rvClassifyGroup.setAdapter(foundGroupAdapter);
                    ((MyViewHolder) holder).rvClassifyGroup.addItemDecoration(new GridSpacingItemDecoration(contentsBean.getData().getRowNum(), ConvertUtils.dp2px(5), false));
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (contentsBeanList == null) ? 0 : contentsBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_top_bg)
        ImageView ivTopBg;
        @BindView(R.id.rv_classify_group)
        RecyclerView rvClassifyGroup;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
