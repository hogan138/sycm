package com.shuyun.qapp.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuyun.qapp.R;

import java.util.List;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * 创建日期：2018/9/14 16:48
 */
public class MyGalleryView extends RelativeLayout {
    private static final int WHAT_AUTO_PLAY = 1000;

    private Context context;
    private LinearLayout mPointRealContainerLl;
    private LayoutParams mPointRealContainerLp;
    private TextView textView;
    private ColorDrawable mPointContainerBackgroundDrawable;
    //是否为网络图片
    private boolean isNetUrl = false;
    //是否可以自动播放
    private boolean mAutoPlayAble = true;
    //是否正在播放
    private boolean mIsAutoPlaying = false;
    // 是否只有一个数据
    private boolean mIsOneImg = false;
    private GalleryView mGallery;
    private List<String> mUrls; //网络图片集合
    private List<Integer> mImages; //本地图片集合
    //当前页面位置
    private int mCurrentPositon;
    //指示点资源
//    private int mPointDrawableResId = R.drawable.selector_bgabanner_point;
    //自动播放时间
    private int mAutoPalyTime = 3000;

    private Handler mAutoPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!mAutoPlayAble) {
                mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPalyTime);
                return;
            }
            mCurrentPositon++;
            mGallery.setSelection(mCurrentPositon);
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPalyTime);
        }
    };
    private int mLength;

    public MyGalleryView(Context context) {
        this(context, null);
    }

    public MyGalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        mPointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#00aaaaaa"));

        mGallery = new GalleryView(context);
        // 设置每张图片的间距  通过手机屏幕的基准比例来设置
        mGallery.setSpacing((int) (-90 * displayMetrics.density));
        mGallery.setOnTouchListener(onTouchListener);
        addView(mGallery, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //设置指示器背景容器
        RelativeLayout pointContainerRl = new RelativeLayout(context);
//        pointContainerRl.setId(R.id.banner_title);
        if (Build.VERSION.SDK_INT >= 16) {
            pointContainerRl.setBackground(mPointContainerBackgroundDrawable);
        } else {
            pointContainerRl.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        //设置内边距
        pointContainerRl.setPadding(0, 10, 0, 10);
        //设定指示器容器布局及位置
        LayoutParams pointContainerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(pointContainerRl, pointContainerLp);
        //设置指示器容器
        mPointRealContainerLl = new LinearLayout(context);
        mPointRealContainerLl.setOrientation(LinearLayout.HORIZONTAL);
        mPointRealContainerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pointContainerRl.addView(mPointRealContainerLl, mPointRealContainerLp);

        textView = new TextView(getContext());
        textView.setText("");
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER);
        layoutParams.bottomMargin = 5;
//        layoutParams.addRule(RelativeLayout.ABOVE, R.id.banner_title);
        addView(textView, layoutParams);
        //设置指示器容器是否可见
        if (mPointRealContainerLl != null) {
            mPointRealContainerLl.setVisibility(View.VISIBLE);
        }
        //设置指示器布局位置
        mPointRealContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mAutoPlayAble = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    mAutoPlayAble = true;
                    break;
            }
            return false;
        }
    };

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mGallery.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 设置网络图片
     *
     * @param urls
     */
    public void setUrls(List<String> urls) {
        if (urls == null) {
            return;
        }
        mLength = urls.size();
        isNetUrl = true;
        this.mUrls = urls;
        if (urls.size() <= 1)
            mIsOneImg = true;
        initGallery();
    }

    /**
     * 设置本地图片
     *
     * @param images
     */
    public void setImages(List<Integer> images) {
        if (images == null || images.size() == 0) {
            return;
        }
        isNetUrl = false;
        mLength = images.size();
        this.mImages = images;
        if (images.size() <= 1)
            mIsOneImg = true;
        initGallery();
    }

    private void initGallery() {
        if (!mIsOneImg) {
            addPoints();
        }
        ImageAdapter adapter = new ImageAdapter();
        mGallery.setAdapter(adapter);
        mGallery.setOnItemSelectedListener(mOnItemSelectedListener);
        mGallery.setSelection(1);
        if (!mIsOneImg) {
            startAutoPlay();
        }
    }

    /**
     * 开始播放
     */
    public void startAutoPlay() {
        if (mAutoPlayAble && !mIsAutoPlaying) {
            mIsAutoPlaying = true;
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPalyTime);
        }
    }

    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e("Tag", "position =" + position + "");
            if (position == 0) {
                mGallery.setSelection(mLength - 2);
                mCurrentPositon = mLength - 2;
                return;
            } else if (position == mLength - 1) {
                mGallery.setSelection(1);
                mCurrentPositon = 1;
                return;
            }
            mCurrentPositon = position;
            // 指示器显示
            switchToPoint(position - 1);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 0.8f);
            imgScaleUpYAnim.setDuration(100);
            //imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 0.8f);
            imgScaleUpXAnim.setDuration(100);
            animatorSet.playTogether(imgScaleUpYAnim, imgScaleUpXAnim);
            animatorSet.start();

            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) != view) {
                    View s = parent.getChildAt(i);
                    ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(s, "scaleY", 0.8f, 0.6f);
                    imgScaleDownYAnim.setDuration(100);
                    //imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
                    ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(s, "scaleX", 0.8f, 0.6f);
                    imgScaleDownXAnim.setDuration(100);
                    animatorSet.playTogether(imgScaleDownXAnim, imgScaleDownYAnim);
                    animatorSet.start();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * 添加指示点
     */
    private void addPoints() {
        mPointRealContainerLl.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        ImageView imageView;
        int length = isNetUrl ? mUrls.size() : mImages.size();
        for (int i = 0; i < length - 2; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
//            imageView.setImageResource(mPointDrawableResId);
            mPointRealContainerLl.addView(imageView);
        }
    }

    /**
     * 切换指示器
     *
     * @param currentPoint
     */
    private void switchToPoint(final int currentPoint) {
        for (int i = 0; i < mPointRealContainerLl.getChildCount(); i++) {
            mPointRealContainerLl.getChildAt(i).setEnabled(false);
        }
        mPointRealContainerLl.getChildAt(currentPoint).setEnabled(true);

    }


    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return isNetUrl ? mUrls == null ? 0 : mUrls.size() : mImages == null ? 0 : mImages.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new RoundImageView(context);
                ((RoundImageView) convertView).setScaleType(ImageView.ScaleType.FIT_XY);
                convertView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));
                convertView.setScaleX(0.6f);
                convertView.setScaleY(0.6f);
            }
            if (isNetUrl)
                Glide.with(context).load(mUrls.get(position)).into((ImageView) convertView);
            else
                ((RoundImageView) convertView).setImageResource(mImages.get(position));
            return convertView;
        }
    }
}
