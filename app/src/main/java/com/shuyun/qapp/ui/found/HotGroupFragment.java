package com.shuyun.qapp.ui.found;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundHotGroupAdapter;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.UmengPageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 热门题组Fragment
 */
public class HotGroupFragment extends Fragment {

    @BindView(R.id.rv_hot_group)
    RecyclerView rvHotGroup;

    private static Activity mContext;
    Unbinder unbinder;

    //热门题组
    public static List<FoundDataBean.TablesBean.GroupsBean> GroupsBeanList = new ArrayList<>();
    private static FoundHotGroupAdapter foundHotGroupAdapter;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private static String GroupsBeanListStr = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_hot_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static HotGroupFragment newInstance(List<FoundDataBean.TablesBean.GroupsBean> groupsBeanList) {
        String str = JSON.toJSONString(GroupsBeanListStr);
        if (GroupsBeanListStr != null && GroupsBeanListStr.equals(str)) {

        } else {
            GroupsBeanListStr = str;
            GroupsBeanList.clear();
            GroupsBeanList.addAll(groupsBeanList);
        }
        HotGroupFragment fragment = new HotGroupFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        //初始化适配器
        foundHotGroupAdapter = new FoundHotGroupAdapter(GroupsBeanList, mContext);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 1) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvHotGroup.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvHotGroup.setHasFixedSize(true);
        rvHotGroup.setNestedScrollingEnabled(false);
        rvHotGroup.setAdapter(foundHotGroupAdapter);
        foundHotGroupAdapter.notifyDataSetChanged();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            //友盟页面统计
            UmengPageUtil.startPage(AppConst.APP_FOUND_HOT_GROUP);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GroupsBeanListStr = null;
    }
}

