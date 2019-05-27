package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.BoxRecordAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.BoxRecordBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 宝箱记录
 */
public class BoxRecordActivity extends BaseActivity implements OnRemotingCallBackListener<List<BoxRecordBean>> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_box_record)
    RecyclerView rvBoxRecord;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_prize_empty)
    ImageView ivPrizeEmpty;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    private BoxRecordAdapter boxRecordAdapter;
    private List<BoxRecordBean> boxRecordBeanList = new ArrayList<>();
    private CommonPopupWindow popupWindow;
    private Context mContext;

    @Override
    public int intiLayout() {
        return R.layout.activity_box_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("宝箱记录");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadBoxRecord(currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadBoxRecord(currentPage);
            }
        });

        boxRecordAdapter = new BoxRecordAdapter(BoxRecordActivity.this, boxRecordBeanList);
        boxRecordAdapter.setOnItemClickLitsener(new BoxRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                BoxRecordBean boxRecordBean = boxRecordBeanList.get(position);
                String title = boxRecordBean.getTitle();
                String remark = boxRecordBean.getRemark();
                showPop(title, remark);
            }
        });
        GridLayoutManager glManager = new GridLayoutManager(BoxRecordActivity.this, 1, LinearLayoutManager.VERTICAL, false);
        rvBoxRecord.setLayoutManager(glManager);
        rvBoxRecord.setAdapter(boxRecordAdapter);

        loadBoxRecord(currentPage);
    }

    private void loadBoxRecord(int currentPage) {
        RemotingEx.doRequest(RemotingEx.Builder().boxRecord(currentPage), this);
    }

    /**
     * 详情popupWindow
     */
    public void showPop(final String title1, final String remark) {
        View upView = LayoutInflater.from(BoxRecordActivity.this).inflate(R.layout.box_record_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(BoxRecordActivity.this)
                .setView(R.layout.box_record_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.box_record_popupwindow:
                                ImageView ivClose = view.findViewById(R.id.iv_close_icon);
                                TextView title = view.findViewById(R.id.tv_title);
                                title.setText(title1);
                                TextView tv_hint = view.findViewById(R.id.tv_hint);
                                tv_hint.setText(Html.fromHtml(remark));
                                tv_hint.setMovementMethod(ScrollingMovementMethod.getInstance());
                                ivClose.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();

        popupWindow.showAtLocation(llMain, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        if (currentPage == 0)
            refreshLayout.finishRefresh();
        else
            refreshLayout.finishLoadmore();
    }

    @Override
    public void onSucceed(String action, DataResponse<List<BoxRecordBean>> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        List<BoxRecordBean> boxRecordBeanList1 = response.getDat();
        if (!EncodeAndStringTool.isListEmpty(boxRecordBeanList1) && boxRecordBeanList1.size() > 0) {
            ivPrizeEmpty.setVisibility(View.GONE);
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加载||下拉刷新
                boxRecordBeanList.clear();
                boxRecordBeanList.addAll(boxRecordBeanList1);
                refreshLayout.finishRefresh();
                refreshLayout.setLoadmoreFinished(false);

            } else if (loadState == AppConst.STATE_MORE) {
                if (boxRecordBeanList1.size() == 0) {//没有数据了
                    refreshLayout.finishLoadmore(); //
                    refreshLayout.setLoadmoreFinished(true);
                } else {
                    boxRecordBeanList.addAll(boxRecordBeanList1);
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(false);
                }
            }
            boxRecordAdapter.notifyDataSetChanged();
        } else {
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                ivPrizeEmpty.setVisibility(View.VISIBLE);
            }
            refreshLayout.finishLoadmore();
            refreshLayout.setLoadmoreFinished(true);
        }
    }
}
