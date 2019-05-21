package com.shuyun.qapp.ui.against;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AgainstListGroupAdapter;
import com.shuyun.qapp.bean.AgainstGruopListBeans;
import com.shuyun.qapp.view.GridSpacingItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Package: com.shuyun.qapp.ui.against
 * @ClassName: AgainstSortFragment
 * @Description: 答题对战分类Fragment
 * @Author: ganquan
 * @CreateDate: 2019/5/16 15:29
 */
public class AgainstSortFragment extends Fragment {

    @BindView(R.id.rv_sort_group)
    RecyclerView rvSortGroup;

    Unbinder unbinder;
    private Context mContext;
    List<AgainstGruopListBeans.DatasBean> datasBeanList = new ArrayList<>();
    private AgainstListGroupAdapter againstListGroupAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_against_sort_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        //获取参数
        final Bundle bundle = getArguments();
        datasBeanList = (List<AgainstGruopListBeans.DatasBean>) bundle.getSerializable("dataList");
        if (datasBeanList == null)
            return;

        //初始化适配器
        againstListGroupAdapter = new AgainstListGroupAdapter(datasBeanList, mContext);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        againstListGroupAdapter.setOnItemClickLitsener(new AgainstListGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AgainstGruopListBeans.DatasBean datasBean = datasBeanList.get(position);
                Intent intent = new Intent(mContext, FreeDetailActivity.class);
                intent.putExtra("groupId", datasBean.getGroupId());
                intent.putExtra("image", datasBean.getPicture());
                intent.putExtra("name", datasBean.getName());
                intent.putExtra("description", datasBean.getDescription());
                intent.putExtra("type", bundle.getInt("type"));
                intent.putExtra("score", bundle.getString("score"));
                startActivity(intent);
            }
        });
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvSortGroup.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvSortGroup.setHasFixedSize(true);
        rvSortGroup.setNestedScrollingEnabled(false);
        rvSortGroup.setAdapter(againstListGroupAdapter);
        rvSortGroup.addItemDecoration(new GridSpacingItemDecoration(2, ConvertUtils.dp2px(15), false));
        rvSortGroup.smoothScrollBy(0, 0);
        againstListGroupAdapter.notifyDataSetChanged();
    }

    public static AgainstSortFragment newInstance(List<AgainstGruopListBeans.DatasBean> List, int type, String score) {
        AgainstSortFragment fragment = new AgainstSortFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("score", score);
        bundle.putSerializable("dataList", (Serializable) List);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
