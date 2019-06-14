package com.shuyun.qapp.ui.king;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.ui.against.MainAgainstActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 答题王者首页
 */
public class AnswerKingMainActivity extends BaseActivity {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        tvCommonTitle.setText("答题王者");

    }

    @OnClick({R.id.iv_back, R.id.iv_answer_against, R.id.ll_my_ranking, R.id.ll_my_record, R.id.btn_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_answer_against: //答题赢积分
                startActivity(new Intent(mContext, MainAgainstActivity.class));
                break;
            case R.id.ll_my_record: //我的战绩
                startActivity(new Intent(mContext, MyRecordActivity.class));
                break;
            case R.id.ll_my_ranking: //我的排名
                startActivity(new Intent(mContext, MyRankingActivity.class));
                break;
            case R.id.btn_start: //开始挑战
                startActivity(new Intent(mContext, StartChallengeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_answer_king_main;
    }
}
