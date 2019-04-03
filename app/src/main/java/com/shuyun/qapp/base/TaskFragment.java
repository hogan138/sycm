package com.shuyun.qapp.base;

import android.support.v4.app.Fragment;

import com.shuyun.qapp.bean.TaskBeans;

import java.util.List;

public abstract class TaskFragment extends Fragment {

    public abstract void refreshTaskUI(List<TaskBeans.DatasBean.TasksBean> tasksBeans);
}
