package com.shuyun.qapp.ui.found;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.AppUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundHotGroupAdapter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 热门题组Fragment
 */
public class HotGroupFragment extends Fragment implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.rv_hot_group)
    RecyclerView rvHotGroup;

    private Activity mContext;
    Unbinder unbinder;

    //热门题组
    private List<GroupClassifyBean> groupClassifyBeans = new ArrayList<>();
    private FoundHotGroupAdapter foundHotGroupAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_hot_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        //获取题组列表
        loadHomeData();

        //初始化适配器
        foundHotGroupAdapter = new FoundHotGroupAdapter(groupClassifyBeans, mContext);
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

    }


    /**
     * 获取题组列表
     */
    private void loadHomeData() {
        RemotingEx.doRequest("getHomeGroups", ApiServiceBean.getHomeGroups(), new Object[]{AppUtils.getAppVersionName()}, this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if ("getHomeGroups".equals(action)) {
            HomeGroupsBean homeGroupsBean = (HomeGroupsBean) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getTree())) {
                final List<GroupClassifyBean> classifyBeans = homeGroupsBean.getTree();
                groupClassifyBeans.clear();
                groupClassifyBeans.addAll(classifyBeans);
                foundHotGroupAdapter.notifyDataSetChanged();
            }
        }
    }
}

