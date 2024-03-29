package com.shuyun.qapp.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.HomeTabContentBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.manager.ImageLoaderManager;
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
    private int wdith = 0, height = 0; //宽高

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
            //获取屏幕宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //计算图片高度宽高比
            wdith = (int) Math.ceil(dm.widthPixels);
            height = (int) (Math.ceil(wdith * (420f / 750f)));
            bannerParams.height = height;
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
                    GridLayoutManager gridLayoutManager;
                    //列表数量
                    int count = dataBeanList.size();
                    //类型少于等于5个且为奇数时，使用大图显示
                    if (count <= 5 && count % 2 == 1) {
                        TabClassifyGroupAdapter1 foundGroupAdapter = new TabClassifyGroupAdapter1(dataBeanList, mContext);
                        gridLayoutManager = new GridLayoutManager(mContext, 1) {
                            @Override
                            public boolean canScrollVertically() {//禁止layout垂直滑动
                                return false;
                            }
                        };
                        ((MyViewHolder) holder).rvClassifyGroup.setAdapter(foundGroupAdapter);
                    } else {
                        //默认使用小图显示
                        TabClassifyGroupAdapter foundGroupAdapter = new TabClassifyGroupAdapter(dataBeanList, mContext);
                        gridLayoutManager = new GridLayoutManager(mContext, 2) {
                            @Override
                            public boolean canScrollVertically() {//禁止layout垂直滑动
                                return false;
                            }
                        };
                        ((MyViewHolder) holder).rvClassifyGroup.setAdapter(foundGroupAdapter);
                        ((MyViewHolder) holder).rvClassifyGroup.addItemDecoration(new GridSpacingItemDecoration(2, ConvertUtils.dp2px(5), false));
                    }
                    gridLayoutManager.setSmoothScrollbarEnabled(true);
                    gridLayoutManager.setAutoMeasureEnabled(true);
                    ((MyViewHolder) holder).rvClassifyGroup.setLayoutManager(gridLayoutManager);
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
