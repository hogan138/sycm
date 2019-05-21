package com.shuyun.qapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shuyun.qapp.bean.TitleBean;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：gq
 * 创建日期：2018/6/9 18:16
 * 公用Fragment适配器
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    //添加fragment的集合
    private List<Fragment> mFragmentList;
    //添加标题的集合
    private List<TitleBean> mTList;

    public TabViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<TitleBean> tList) {
        super(fm);
        mFragmentList = fragmentList;
        mTList = tList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTList.get(position).getTabTitle();
    }

    @Override
    public int getItemPosition(Object object) {//TODO 这是重点继续研究
        //return super.getItemPosition(object);//默认是不改变
        return POSITION_NONE;//可以即时刷新看源码
    }
}
