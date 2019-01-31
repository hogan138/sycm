package com.shuyun.qapp.ui.homepage;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.InfomationAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息界面
 */
public class InformationActivity extends BaseActivity implements CommonPopupWindow.ViewInterface, InfomationAdapter.OnItemClickListener {//

    @BindView(R.id.ll_information)
    LinearLayout llInformation;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;//左侧图标可点击区域
    @BindView(R.id.iv_left_icon)
    ImageView ivLeftIcon;//左侧图标
    @BindView(R.id.tv_common_title)
    TextView tvInformationTitle;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;//右侧图标
    @BindView(R.id.rv_information)
    RecyclerView rvInformation;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    private List<Msg> sqlMsgList = new ArrayList<>();

    private CommonPopupWindow popupWindow;
    private InfomationAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        MyActivityManager.getInstance().pushOneActivity(this);

        ivLeftIcon.setImageResource(R.mipmap.guanbi);
        tvInformationTitle.setText("消息");
        ivRightIcon.setImageResource(R.mipmap.messager);
        ivEmpty.setVisibility(View.VISIBLE);


        infoAdapter = new InfomationAdapter(sqlMsgList, InformationActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(InformationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvInformation.setLayoutManager(manager);
        rvInformation.setAdapter(infoAdapter);
        infoAdapter.setOnItemClickLitsener(this);

        loadMsg();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_information;
    }

    private long stamp1;

    private void loadMsg() {
        List<Msg> msgList = LitePal.findAll(Msg.class);
        if (!EncodeAndStringTool.isListEmpty(msgList)) {
            //取本地数据库中最新一条消息的时间戳(数据库中第一条数据的时间戳)
            stamp1 = msgList.get(0).getTime();
        } else {
            //第一条消息
            stamp1 = 0;
        }

        RemotingEx.doRequest(ApiServiceBean.getMsg(), new Object[]{0, stamp1, System.currentTimeMillis()}, new OnRemotingCallBackListener<List<Msg>>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<List<Msg>> dataResponse) {
                if (dataResponse.isSuccees()) {
                    final List<Msg> msgList = dataResponse.getDat();
                    if (msgList == null || msgList.isEmpty())
                        return;
                    for (int i = 0; i < msgList.size(); i++) {
                        Msg msg0 = msgList.get(i);
                        //查询数据库中的数据,如果不存在,才添加到数据库,存在则不添加
                        List<Msg> msgs = LitePal.where("msgId = ?", String.valueOf(msg0.getId())).find(Msg.class);
                        if (msgs != null && !msgs.isEmpty())
                            continue;
                        Msg msg = new Msg();
                        msg.setType(msg0.getType());
                        msg.setTitle(msg0.getTitle());
                        msg.setContent(msg0.getContent());
                        msg.setUrl(msg0.getUrl());
                        msg.setStatus(msg0.getStatus());
                        msg.setTime(msg0.getTime());
                        msg.setMsgId(String.valueOf(msg0.getId()));
                        boolean result = msg.save();
                        Log.d("DATA", String.valueOf(result));
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(InformationActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
                dataRefreshUi();
            }
        });

    }

    /**
     * 查询本地数据库,并更新消息列表
     */
    private void dataRefreshUi() {
        //查询本地数据库,展示在UI界面
        final List<Msg> msgs = LitePal.findAll(Msg.class);
        if (msgs != null && !msgs.isEmpty()) {
            ivEmpty.setVisibility(View.GONE);
            sqlMsgList.clear();
            sqlMsgList.addAll(msgs);
            infoAdapter.notifyDataSetChanged();
        } else {
            //设置空图片
            sqlMsgList.clear();
            infoAdapter.notifyDataSetChanged();
            ivEmpty.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_common_left_icon, R.id.iv_right_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left_icon:
                if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.iv_right_icon:
                showMsgPopupwindow();
                break;
            default:
                break;
        }
    }

    private void showMsgPopupwindow() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.msg_popupwindow, null);

        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.msg_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredWidth())//ViewGroup.LayoutParams.WRAP_CONTENT
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(vLine, 0, 0, Gravity.RIGHT);
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.msg_popupwindow:
                TextView tvAllReaded = view.findViewById(R.id.tv_all_readed);
                TextView tvDeleteReaded = view.findViewById(R.id.tv_delete_readed);
                tvAllReaded.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        //status全部置为2、已读状态
                        List<Msg> msgList = LitePal.findAll(Msg.class);
                        for (int i = 0; i < msgList.size(); i++) {
                            Msg msg = msgList.get(i);
                            msg.setStatus(2);
                            msg.save();
                        }
                        sqlMsgList.clear();
                        sqlMsgList.addAll(msgList);
                        infoAdapter.notifyDataSetChanged();
                    }
                });
                tvDeleteReaded.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //删除所有已读消息
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        LitePal.deleteAll(Msg.class, "status = ?", "2");
                        dataRefreshUi();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemChildClick(View view, int position) {
        Msg msg1 = sqlMsgList.get(position);
        //处理消息点击事件
        Msg msg = LitePal.find(Msg.class, msg1.getId());
        msg.setStatus(2);
        msg.save();

        msg1.setStatus(2);
        infoAdapter.notifyDataSetChanged();
        if (msg.getType() == 0) {//不跳转
            return;
        }
        if (msg.getType() == 1) {//题组详情
            try {
                Intent intent = new Intent(InformationActivity.this, WebAnswerActivity.class);
                intent.putExtra("groupId", Long.valueOf(msg.getUrl()));
                intent.putExtra("h5Url", msg.getH5Url());
                startActivity(intent);
                finish();
            } catch (Exception e) {
            }
            return;
        }
        if (msg.getType() == 2) {//外部H5
            Uri uri = Uri.parse(msg.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return;
        }
        if (msg.getType() == 3) {//内部链接
            Intent intent = new Intent(InformationActivity.this, WebH5Activity.class);
            intent.putExtra("url", msg.getUrl());
            intent.putExtra("name", msg.getTitle());
            startActivity(intent);
            return;
        }
        if (msg.getType() == 4) {//实名认证
            Intent intent = new Intent(InformationActivity.this, RealNameAuthActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }
}