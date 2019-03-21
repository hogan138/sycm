package com.shuyun.qapp.manager;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class FragmentTouchManager {
    public interface FragmentTouchListener {
        boolean onTouchEvent(MotionEvent event);
    }

    private List<FragmentTouchListener> mFragmentTouchListeners = new ArrayList<>();

    private static FragmentTouchManager manager = null;

    protected FragmentTouchManager() {

    }

    public static FragmentTouchManager instance() {
        if (manager == null)
            manager = new FragmentTouchManager();
        return manager;
    }

    public void registerFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.add(listener);
    }

    public void unRegisterFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.remove(listener);
    }

    public void apply(MotionEvent event) {
        for (FragmentTouchListener ls : mFragmentTouchListeners) {
            ls.onTouchEvent(event);
        }
    }
}
