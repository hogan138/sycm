package com.shuyun.qapp.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 导航页
 */
public class NavigationActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.start)
    Button start;

    //xml资源
    private int[] resources = {R.layout.navig_image1, R.layout.navig_image2, R.layout.navig_image3};
    private List<View> listViews = new ArrayList<View>();
    //加载布局的
    private LayoutInflater inflater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //初始化数据
        initView();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_navigation;
    }

    private void initView() {
        inflater = LayoutInflater.from(this);
        for (int i = 0; i < resources.length; i++) {
            View view = inflater.inflate(resources[i], null);
            listViews.add(view);
        }

        viewpager.setAdapter(new MyPageAdapter());
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == resources.length - 1) {
                    start.setEnabled(true);
                } else {
                    start.setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(listViews.get(position));
            return listViews.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(listViews.get(position));
        }

    }
}
