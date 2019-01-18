package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.HistoryDataBean;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.utils.DisplayUtil;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.ViewToBitmap;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * 成绩单
 */
public class ShareAnswerRecordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
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
    @BindView(R.id.sharePyq)
    LinearLayout sharePyq;
    @BindView(R.id.shareHy)
    LinearLayout shareHy;
    @BindView(R.id.shareQr)
    ImageView shareQr;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.ll_share)
    LinearLayout llShare;

    private Context mContext;
    private HistoryDataBean.ResultBean recordBean;
    private BigDecimal a = new BigDecimal("85");
    private BigDecimal b = new BigDecimal("60");
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;


        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {
            llShare.setVisibility(View.GONE);
        } else {
            llShare.setVisibility(View.VISIBLE);
        }

        Bundle bundle = getIntent().getExtras();
        recordBean = (HistoryDataBean.ResultBean) bundle.getSerializable("share");
        if (recordBean == null)
            return;

        tvCommonTitle.setText(recordBean.getTitle());

        tvTitle.setText(recordBean.getTitle());
        Date currentTime = new Date(Long.valueOf(recordBean.getTime()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String time = formatter.format(currentTime).replace(" ", "\n");
        tvTime.setText(time);
        String fullName = recordBean.getFullName();
        String[] names = fullName.split("/");
        tvClass.setText(names[0]);

        StringBuffer sb = new StringBuffer();
        sb.append(recordBean.getAccuracy()).append("%");
        tvRate.setText(sb.toString());

        //正确率：85%以上A ，正确率60%~85% 的B，其他C吧
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
        int w = (int) Math.ceil(dm.widthPixels - DisplayUtil.dp2px(mContext, 90));
        int height = (int) Math.ceil(w * 264f / 528f);

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = height;
        imageView.setLayoutParams(lp);

        ImageLoaderManager.LoadImage(mContext, recordBean.getPicture(), imageView, R.mipmap.zw01);//题组图片

        ivBack.setOnClickListener(this);
        shareHy.setOnClickListener(this);
        sharePyq.setOnClickListener(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shareQr();
            }
        }, 0);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_share_history;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shareHy:
            case R.id.sharePyq:
                saveToBitmap(cardView);
                break;
            default:
                break;
        }
    }

    private void saveToBitmap(View view) {
        ViewToBitmap toBitmap = new ViewToBitmap(this, view, "shareImg");
        toBitmap.setFileName(recordBean.getId());
        toBitmap.setOnBitmapSaveListener(new ViewToBitmap.OnBitmapSaveListener() {

            @Override
            public void onBitmapSaved(final boolean isSaved, final String path) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, isSaved + ":" + path, Toast.LENGTH_LONG).show();
                        File file = new File(path);
                    }
                }, 0);
            }
        });
        toBitmap.saveToBitmap();
    }

    private void shareQr() {
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo, null);
        Bitmap mBitmap = CodeUtils.createImage("https://t.cn/R3rvkuk", dp2px(114), dp2px(114), logo);
        shareQr.setImageBitmap(mBitmap);
    }
}
