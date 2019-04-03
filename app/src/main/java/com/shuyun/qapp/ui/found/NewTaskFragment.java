package com.shuyun.qapp.ui.found;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundTaskAdapter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.TaskApplayBean;
import com.shuyun.qapp.bean.TaskBeans;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.integral.IntegralMainActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 新手任务Fragment
 */
public class NewTaskFragment extends Fragment implements FoundTaskAdapter.OnItemChildClickListener {


    @BindView(R.id.rv_new_task)
    RecyclerView rvNewTask;
    private Activity mContext;
    Unbinder unbinder;

    private List<TaskBeans.DatasBean.TasksBean> tasksBeans = new ArrayList<>();
    private FoundTaskAdapter foundTaskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_new_task, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void refreshUI(List<TaskBeans.DatasBean.TasksBean> tasksBeans) {
        for (int i = 0, j = tasksBeans.size(); i < j; i++) {
            TaskBeans.DatasBean.TasksBean bean = tasksBeans.get(i);
            TaskBeans.DatasBean.TasksBean bean0 = getTask(bean.getTaskId());
            if (bean0 == null) {
                this.tasksBeans.add(bean);
            } else {
                bean0.setTaskId(bean.getTaskId());
                bean0.setAction(bean.getAction());
                bean0.setActionLabel(bean.getActionLabel());
                bean0.setContent(bean.getContent());
                bean0.setH5Url(bean.getH5Url());
                bean0.setName(bean.getName());
                bean0.setPicture(bean.getPicture());
                bean0.setPrize(bean.getPrize());
                bean0.setPurpose(bean.getPurpose());
            }
        }

        foundTaskAdapter.notifyDataSetChanged();
    }

    /**
     * 根据ID获取对应的任务
     *
     * @param taskId
     * @return
     */
    private TaskBeans.DatasBean.TasksBean getTask(String taskId) {
        for (int i = 0, j = tasksBeans.size(); i < j; i++) {
            TaskBeans.DatasBean.TasksBean bean = tasksBeans.get(i);
            if (bean.getTaskId().equals(taskId))
                return bean;
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        //初始化适配器
        foundTaskAdapter = new FoundTaskAdapter(tasksBeans, mContext);
        foundTaskAdapter.setOnItemClickLitsener(this);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 1) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvNewTask.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvNewTask.setHasFixedSize(true);
        rvNewTask.setNestedScrollingEnabled(false);
        rvNewTask.setAdapter(foundTaskAdapter);

    }

    //领取奖励
    public void applyTask(final TaskBeans.DatasBean.TasksBean tasksBean) {
        RemotingEx.doRequest(ApiServiceBean.taskApply(), new Object[]{tasksBean.getTaskId()}, new OnRemotingCallBackListener<TaskApplayBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<TaskApplayBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    TaskApplayBean taskApplayBean = dataResponse.getDat();
                    Toast.makeText(mContext, "领取成功", Toast.LENGTH_SHORT).show();
                    tasksBean.setAction("action.finish");
                    tasksBean.setActionLabel("已完成");
                    foundTaskAdapter.notifyDataSetChanged();
                    //刷新积分信息
                    SignInActivity signInActivity = (SignInActivity) getActivity();
                    signInActivity.tvMyScore.setText(taskApplayBean.getBp().toString());
                } else {
                    ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChildClick(View view, int position) {
        TaskBeans.DatasBean.TasksBean tasksBean = tasksBeans.get(position);
        String action = tasksBean.getAction();
        if (action.equals(AppConst.ACTION_RECEIVE)) {
            //请求领取接口
            applyTask(tasksBean);
        } else if (action.equals(AppConst.REAL)) {
            //实名认证
            mContext.startActivity(new Intent(mContext, RealNameAuthActivity.class));
        } else if (action.equals(AppConst.ACTION_BIND_WX)) {
            //绑定微信
            SignInActivity.wxLogin();
        } else if (action.equals(AppConst.ACTION_ONE_EXAM) || action.equals(AppConst.ACTION_DAY_EXAM)) {
            //完成一次答题、每日答题 跳转到分类页
            SaveUserInfo.getInstance(mContext).setUserInfo("task_jump", "task_answer");
            mContext.finish();
        } else if (action.equals(AppConst.action_oppty)) {
            //领取答题弹框，跳转到我的页面
            SaveUserInfo.getInstance(mContext).setUserInfo("task_jump", "task_oppty");
            mContext.finish();
        } else if (action.equals(AppConst.ACTION_INVITE_FRIENDS)) {
            //邀请好友
            Intent intent = new Intent(mContext, WebH5Activity.class);
            intent.putExtra("url", tasksBean.getH5Url());
            intent.putExtra("name", "");//名称 标题
            mContext.startActivity(intent);
        } else if (action.equals(AppConst.ACTION_HS_GROUP_EXAM)) {
            //黄山题组
            Intent intent = new Intent(mContext, WebAnswerActivity.class);
            intent.putExtra("groupId", Long.parseLong(tasksBean.getContent()));
            intent.putExtra("h5Url", tasksBean.getH5Url());
            mContext.startActivity(intent);
        } else if (action.equals(AppConst.OPEN_BOX)) {
            //积分开宝箱
            Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
            intent.putExtra("main_box", "score_box");
            intent.putExtra("h5Url", tasksBean.getH5Url());
            mContext.startActivity(intent);
        } else if (action.equals(AppConst.TREASURE)) {
            //积分夺宝
            SaveUserInfo.getInstance(mContext).setUserInfo("h5_rule", tasksBean.getH5Url());
            mContext.startActivity(new Intent(mContext, IntegralMainActivity.class));
        }
    }
}

