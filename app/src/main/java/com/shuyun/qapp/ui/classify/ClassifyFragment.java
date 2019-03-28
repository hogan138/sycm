package com.shuyun.qapp.ui.classify;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ChildrenGroupAdapter;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 分类
 */
public class ClassifyFragment extends BaseFragment implements OnRemotingCallBackListener<List<GroupClassifyBean>> {

    @BindView(R.id.rv_group_sort)
    RecyclerView rvGroupSort;//左侧题组分类RecyclerView
    @BindView(R.id.rv_group)
    RecyclerView rvGroup;//右侧题组RecyclerView
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private Unbinder unbinder;
    private Activity mContext;
    private List<GroupClassifyBean> classifyBeans = new ArrayList<>();
    private List<GroupClassifyBean.ChildrenBean> childrenBeans = new ArrayList<>();

    private boolean isLoading = false;

    private GroupTreeAdapter groupTreeAdapter;
    private CenterLayoutManager centerLayoutManager;

    private Handler mHandler = new Handler();
    /**
     * 右侧题组分类列表
     */
    private ChildrenGroupAdapter childrenGroupAdapter;
    private String dataString = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("题组分类");

        try {
            String showback = SaveUserInfo.getInstance(mContext).getUserInfo("show_back");
            if (showback.equals("show_back")) {
                //显示返回按钮
                rlBack.setVisibility(View.VISIBLE);
                SaveUserInfo.getInstance(mContext).setUserInfo("show_back", "");
            } else {
                //隐藏返回按钮
                rlBack.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 10);
        }
    }

    private void init() {
        groupTreeAdapter = new GroupTreeAdapter(classifyBeans, mContext);//分类左侧适配器
        centerLayoutManager = new CenterLayoutManager(mContext);

        /**
         * 左侧分类列表点击事件
         */
        groupTreeAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!EncodeAndStringTool.isListEmpty(classifyBeans)) {
                    for (int i = 0; i < classifyBeans.size(); i++) {
                        if (i == position) {
                            classifyBeans.get(i).setFlag(true);
                        } else {
                            classifyBeans.get(i).setFlag(false);
                        }
                        groupTreeAdapter.notifyDataSetChanged();
                    }
                    /**
                     * 将答题树列表滑动到指定位置TODO  和点击选中有问题; groupTreeAdapter
                     */
                    centerLayoutManager.smoothScrollToPosition(rvGroup, new RecyclerView.State(), position);

                    refreshRightGroup(position, classifyBeans);
                }
            }
        });
        rvGroupSort.setLayoutManager(centerLayoutManager);
        rvGroupSort.setAdapter(groupTreeAdapter);

        childrenGroupAdapter = new ChildrenGroupAdapter(mContext, childrenBeans);
        childrenGroupAdapter.setOnItemClickLitsener(new ChildrenGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                GroupClassifyBean.ChildrenBean childrenBean = childrenBeans.get(position);
                Intent intent = new Intent(mContext, WebAnswerActivity.class);
                intent.putExtra("groupId", childrenBean.getId());
                intent.putExtra("h5Url", childrenBean.getH5Url());
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
        rvGroup.setLayoutManager(layoutManager2);
        rvGroup.setAdapter(childrenGroupAdapter);

    }

    @OnClick({R.id.rl_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_back: //返回键
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.radioGroupChange(0);
                } else if (mContext instanceof ClassifyActivity) {
                    mContext.finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取到题组树
     */
    private void loadGroupTree() {
        RemotingEx.doRequest(ApiServiceBean.getGroupTree(), this);
    }

    /**
     * 选中左侧题组之后,刷新右侧题组的数据
     *
     * @param position
     * @param classifyBeans
     */
    private void refreshRightGroup(int position, List<GroupClassifyBean> classifyBeans) {
        final List<GroupClassifyBean.ChildrenBean> beans = classifyBeans.get(position).getChildren();
        childrenBeans.clear();
        childrenBeans.addAll(beans);
        childrenGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh() {
        if (!isLoading) {
            init();
        }
        isLoading = true;

        //刷新分类数据
        loadGroupTree();
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<List<GroupClassifyBean>> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        List<GroupClassifyBean> beans = response.getDat();
        String string = JSON.toJSONString(beans);
        if (dataString != null && dataString.equals(string)) {
            return;
        }
        dataString = string;

        if (!EncodeAndStringTool.isListEmpty(beans)) {
            classifyBeans.clear();
            classifyBeans.addAll(beans);
            groupTreeAdapter.setSelectedPosition(0);
            if (mContext instanceof ClassifyActivity) {
                Long id = mContext.getIntent().getLongExtra("id", 0);
                for (int i = 0; i < beans.size(); i++) {
                    if (beans.get(i).getId().longValue() == id.longValue()) {
                        beans.get(i).setFlag(true);
                        refreshRightGroup(i, beans);
                        groupTreeAdapter.setSelectedPosition(i);
                    } else if (id == 0) {
                        beans.get(0).setFlag(true);
                        refreshRightGroup(0, beans);
                    }
                }
            } else if (mContext instanceof HomePageActivity) {
                beans.get(0).setFlag(true);
                refreshRightGroup(0, beans);
            }
            groupTreeAdapter.notifyDataSetChanged();
        }
    }
}

