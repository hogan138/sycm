package com.shuyun.qapp.ui.king;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.view.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 答题王者匹配
 */
public class KingMatchingActivity extends BaseActivity {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_big_circle)
    ImageView ivBigCircle;
    @BindView(R.id.iv_small_circle)
    ImageView ivSmallCircle;
    @BindView(R.id.iv_mine_head)
    RoundImageView ivMineHead;
    @BindView(R.id.iv_him_head)
    RoundImageView ivHimHead;
    @BindView(R.id.tv_matching_status)
    TextView tvMatchingStatus;
    @BindView(R.id.tv_cancel_matching)
    TextView tvCancelMatching;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        tvCommonTitle.setText("开始匹配");

        //初始化动画
        initAnimation();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel_matching})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel_matching:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_king_matching;
    }

    //初始化动画
    private void initAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation_no);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        ivBigCircle.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.img_animation_on);
        LinearInterpolator lin1 = new LinearInterpolator();
        animation1.setInterpolator(lin1);
        ivSmallCircle.startAnimation(animation1);
    }
}
