
package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主页面tab适配器
 */
public class MyHomeadapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private Context context;

    //构造方法
    public MyHomeadapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
    }

    //得到item条目
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    //得到数量
    @Override
    public int getCount() {
        return fragments.size();
    }
} 
 