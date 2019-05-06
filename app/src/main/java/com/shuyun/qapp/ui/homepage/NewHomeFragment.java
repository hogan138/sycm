package com.shuyun.qapp.ui.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Package: com.shuyun.qapp.ui.homepage
 * @ClassName: NewHomeFragment
 * @Description: 新版首页
 * @Author: ganquan
 * @CreateDate: 2019/5/6 10:03
 */
public class NewHomeFragment extends BaseFragment {

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void refresh() {

    }
}
