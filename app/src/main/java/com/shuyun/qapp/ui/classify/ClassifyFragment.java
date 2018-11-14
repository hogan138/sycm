package com.shuyun.qapp.ui.classify;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ChildrenGroupAdapter;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 分类
 */
public class ClassifyFragment extends Fragment {

    @BindView(R.id.rv_group_sort)
    RecyclerView rvGroupSort;//左侧题组分类RecyclerView
    @BindView(R.id.rv_group)
    RecyclerView rvGroup;//右侧题组RecyclerView
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    Unbinder unbinder;
    private Activity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
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
        tvCommonTitle.setText("题组分类");

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();

        //请求分类数据
        loadGroupTree();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //刷新分类数据
            loadGroupTree();
        }
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回键
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.changeUi(0);
                } else if (mContext instanceof ClassifyActivity) {
                    startActivity(new Intent(mContext, HomePageActivity.class));
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
        ApiService apiService = BasePresenter.create(8000);
        apiService.getGroupTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<GroupClassifyBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<GroupClassifyBean>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            /**
                             * 左侧题组分类列表
                             */
                            final List<GroupClassifyBean> classifyBeans = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(classifyBeans)) {
                                final GroupTreeAdapter groupTreeAdapter = new GroupTreeAdapter(classifyBeans, mContext);//分类左侧适配器
                                final CenterLayoutManager centerLayoutManager = new CenterLayoutManager(getActivity());

                                if (mContext instanceof ClassifyActivity) {
                                    int id = mContext.getIntent().getIntExtra("id", 0);
                                    for (int i = 0; i < classifyBeans.size(); i++) {
                                        if (classifyBeans.get(i).getId() == id) {
                                            classifyBeans.get(i).setFlag(true);
                                            refreshRightGroup(i, classifyBeans);
                                        } else if (id == 0) {
                                            classifyBeans.get(0).setFlag(true);
                                            refreshRightGroup(0, classifyBeans);
                                        }
                                    }
                                } else if (mContext instanceof HomePageActivity) {
                                    classifyBeans.get(0).setFlag(true);
                                    refreshRightGroup(0, classifyBeans);
                                }
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
                                try {
                                    rvGroupSort.setLayoutManager(centerLayoutManager);
                                    rvGroupSort.setAdapter(groupTreeAdapter);
                                } catch (Exception e) {

                                }

                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 选中左侧题组之后,刷新右侧题组的数据
     *
     * @param position
     * @param classifyBeans
     */
    private void refreshRightGroup(int position, List<GroupClassifyBean> classifyBeans) {
        final List<GroupClassifyBean.ChildrenBean> childrenBeans = classifyBeans.get(position).getChildren();
        /**
         * 右侧题组分类列表
         */
        ChildrenGroupAdapter childrenGroupAdapter = new ChildrenGroupAdapter(mContext, childrenBeans);

        childrenGroupAdapter.setOnItemClickLitsener(new ChildrenGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                GroupClassifyBean.ChildrenBean childrenBean = childrenBeans.get(position);
                Intent intent = new Intent(getActivity(), WebAnswerActivity.class);
                intent.putExtra("groupId", childrenBean.getId());
                intent.putExtra("h5Url", childrenBean.getH5Url());
                startActivity(intent);
            }
        });
        try {
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
            rvGroup.setLayoutManager(layoutManager2);
            rvGroup.setAdapter(childrenGroupAdapter);
        } catch (Exception e) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ClassifyFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ClassifyFragment");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

