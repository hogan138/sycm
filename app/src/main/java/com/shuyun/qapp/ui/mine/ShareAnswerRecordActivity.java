package com.shuyun.qapp.ui.mine;

import android.app.Activity;
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

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.HistoryDataBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.utils.DisplayUtil;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.ViewToBitmap;
import com.shuyun.qapp.view.OvalImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
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
    OvalImageView imageView;
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

    public Context mContext;
    private HistoryDataBean.ResultBean recordBean;
    private BigDecimal a = new BigDecimal("85");
    private BigDecimal b = new BigDecimal("60");
    private Handler mHandler = new Handler();

    private static int SHARE_CHANNEL;//分享渠道2:微信好友;1:微信朋友圈

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
                SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                saveToBitmap(cardView);
                break;
            case R.id.sharePyq:
                SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
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
                        File file = new File(path);
                        //微信分享
                        wechatShare(file, mContext);
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

    /**
     * 微信、朋友圈分享
     */
    private static void wechatShare(final File file, Context context) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//微信
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {//朋友圈
            media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        UMImage image = new UMImage(context, file);//网络图片
        UMImage thumb = new UMImage(context, file);//缩略图
        image.setThumb(thumb);
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        new ShareAction((Activity) context)
                .setPlatform(media)
                .withMedia(image)
                .share();
    }

}
