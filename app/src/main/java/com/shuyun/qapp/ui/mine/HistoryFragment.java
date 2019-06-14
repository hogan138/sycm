package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.HistoryDataBean;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.utils.DisplayUtil;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.manager.ImageLoaderManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.ivLevel)
    ImageView ivLevel;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.viewDetail)
    Button viewDetail;
    @BindView(R.id.tvShare)
    LinearLayout tvShare;

    private Context mContext;
    private HistoryDataBean.ResultBean recordBean;
    private BigDecimal a = new BigDecimal("85");
    private BigDecimal b = new BigDecimal("50");

    public void setRecordBean(HistoryDataBean.ResultBean recordBean) {
        this.recordBean = recordBean;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        tvTitle.setText(recordBean.getTitle());
        Date currentTime = new Date(Long.valueOf(recordBean.getTime()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String time = formatter.format(currentTime).replace(" ", "\n");
        tvTime.setText(time);
        String fullName = recordBean.getFullName();
        if (fullName != null) {
            String[] names = fullName.split("/");
            tvClass.setText(names[0]);
        } else {
            tvClass.setText("");
        }

        StringBuffer sb = new StringBuffer();
        sb.append(recordBean.getAccuracy()).append("%");
        tvRate.setText(sb.toString());

        //正确率：85%以上A ，正确率50%~85% 的B，其他C吧
        BigDecimal rate = new BigDecimal(recordBean.getAccuracy());
        if (rate.compareTo(a) > 0) {
            ivLevel.setImageResource(R.mipmap.a);
        } else if (rate.compareTo(b) >= 0 && rate.compareTo(a) <= 0) {
            ivLevel.setImageResource(R.mipmap.b);
        } else {
            ivLevel.setImageResource(R.mipmap.c);
        }

        //获取屏幕宽度
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int w = (int) Math.ceil(dm.widthPixels - DisplayUtil.dp2px(mContext, 8));
        int height = (int) Math.ceil(w * 264f / 528f);

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = height;
        imageView.setLayoutParams(lp);

        ImageLoaderManager.LoadImage(mContext, recordBean.getPicture(), imageView, R.mipmap.zw01);//题组图片

        viewDetail.setOnClickListener(this);
        tvShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viewDetail) {
            String id = recordBean.getId();
            String title = recordBean.getTitle();
            if (!EncodeAndStringTool.isStringEmpty(id)) {
                Intent intent = new Intent(mContext, AnswerHistoryActivity.class);
                intent.putExtra("answer_id", id);
                intent.putExtra("title", title);
                intent.putExtra("from", "answer");
                startActivity(intent);
            }
        } else if (v.getId() == R.id.tvShare) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("share", recordBean);
            Intent intent = new Intent(mContext, ShareAnswerRecordActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void refresh() {

    }
}
