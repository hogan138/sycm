package com.shuyun.qapp.ui.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.HomeTabClassifyAdapter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.HomeTabContentBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ObjectUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Package: com.shuyun.qapp.ui.homepage
 * @ClassName: TabClassifyFragment
 * @Description: tab分类
 * @Author: ganquan
 * @CreateDate: 2019/5/13 9:42
 */
public class TabClassifyFragment extends Fragment implements OnRemotingCallBackListener<HomeTabContentBean> {

    Unbinder unbinder;
    @BindView(R.id.rv_classify_group)
    RecyclerView rvClassifyGroup;

    private Activity mContext;

    HomeTabClassifyAdapter homeTabCassifyAdapter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_classify, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //获取参数
            final Bundle bundle = getArguments();

            if (bundle != null) {
                loadTabInfo(bundle.getLong("tabId"));
            }
        }
    }

    /**
     * tab数据
     */
    public void loadTabInfo(Long tabId) {
        RemotingEx.doRequest("getTabInfo", RemotingEx.Builder().HometabContent(tabId), this);
    }

    public static TabClassifyFragment newInstance(Long tabId) {
        TabClassifyFragment fragment = new TabClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("tabId", tabId);
        fragment.setArguments(bundle);
        return fragment;
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
    public void onSucceed(String action, DataResponse<HomeTabContentBean> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if ("getTabInfo".equals(action)) {
            HomeTabContentBean homeTabContentBean = ObjectUtil.cast(response.getDat());
            List<HomeTabContentBean.ContentsBean> contentsBeans = homeTabContentBean.getContents();
            if (!EncodeAndStringTool.isListEmpty(contentsBeans)) {
                homeTabCassifyAdapter = new HomeTabClassifyAdapter(contentsBeans, mContext);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1) {
                    @Override
                    public boolean canScrollVertically() {//禁止layout垂直滑动
                        return false;
                    }
                };
                gridLayoutManager.setSmoothScrollbarEnabled(true);
                gridLayoutManager.setAutoMeasureEnabled(true);
                rvClassifyGroup.setLayoutManager(gridLayoutManager);
                //解决数据加载不完的问题
                rvClassifyGroup.setHasFixedSize(true);
                rvClassifyGroup.setNestedScrollingEnabled(false);
                rvClassifyGroup.setAdapter(homeTabCassifyAdapter);
            }

        }


    }
}
