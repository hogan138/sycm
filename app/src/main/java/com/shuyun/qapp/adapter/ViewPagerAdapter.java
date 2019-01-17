package com.shuyun.qapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.shuyun.qapp.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public List<BaseFragment> mListViews = new ArrayList<>();
    public Context context;
    private BaseFragment currentFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<BaseFragment> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((BaseFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mListViews.get(position).getName();
    }
}
