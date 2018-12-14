package com.shuyun.qapp.ui.homepage;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.InfomationAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息界面
 */
public class InformationActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {//

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
    long stamp0 = System.currentTimeMillis();//当前时间戳
    private CommonPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivLeftIcon.setImageResource(R.mipmap.guanbi);
        tvInformationTitle.setText("消息");
        ivRightIcon.setImageResource(R.mipmap.messager);

        MyActivityManager.getInstance().pushOneActivity(this);

        loadMsg();

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_information;
    }

    private long stamp1;

    private void loadMsg() {
        List<Msg> msgList = DataSupport.findAll(Msg.class);
        if (!EncodeAndStringTool.isListEmpty(msgList)) {
            //取本地数据库中最新一条消息的时间戳(数据库中第一条数据的时间戳)
            stamp1 = msgList.get(0).getTime();

        } else {
            //第一条消息
            stamp1 = 0;
        }
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMsg(0, stamp1, stamp0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<Msg>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<Msg>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            List<Msg> msgList = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(msgList)) {
                                for (int i = 0; i < msgList.size(); i++) {
                                    Msg msg0 = msgList.get(i);
                                    //查询数据库中的数据,如果不存在,才添加到数据库,不存在则不添加
                                    Msg msg1 = DataSupport.find(Msg.class, msg0.getId());
                                    if (EncodeAndStringTool.isObjectEmpty(msg1)) {
                                        Msg msg = new Msg();
                                        msg.setType(msg0.getType());
                                        msg.setTitle(msg0.getTitle());
                                        msg.setContent(msg0.getContent());
                                        msg.setUrl(msg0.getUrl());
                                        msg.setStatus(msg0.getStatus());
                                        msg.setTime(msg0.getTime());
                                        msg.setId(msg0.getId());
                                        msg.save();
                                    }
                                }
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(InformationActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                        getDataRefreshUi();
                    }


                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 查询本地数据库,并更新消息列表
     */
    private void getDataRefreshUi() {
        //查询本地数据库,展示在UI界面
        final List<Msg> sqlMsgList = DataSupport.findAll(Msg.class);
        if (!EncodeAndStringTool.isListEmpty(sqlMsgList)) {
            ivEmpty.setVisibility(View.GONE);
            setData(sqlMsgList);
        } else {
            //设置空图片
            InfomationAdapter infoAdapter = new InfomationAdapter(null, InformationActivity.this);
            LinearLayoutManager manager = new LinearLayoutManager(InformationActivity.this, LinearLayoutManager.VERTICAL, false);
            rvInformation.setAdapter(infoAdapter);
            rvInformation.setLayoutManager(manager);
            ivEmpty.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 将数据填充到listView
     *
     * @param sqlMsgList
     */
    private void setData(final List<Msg> sqlMsgList) {
        final InfomationAdapter infoAdapter = new InfomationAdapter(sqlMsgList, InformationActivity.this);
        final LinearLayoutManager manager = new LinearLayoutManager(InformationActivity.this, LinearLayoutManager.VERTICAL, false);
        infoAdapter.setOnItemClickLitsener(new InfomationAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                //处理消息点击事件
                Msg msg = DataSupport.find(Msg.class, sqlMsgList.get(position).getId());
                msg.setStatus(2);
                msg.save();

                Msg msg1 = sqlMsgList.get(position);
                msg1.setStatus(2);
                infoAdapter.notifyDataSetChanged();
                if (msg.getType() == 0) {//不跳转

                } else if (msg.getType() == 1) {//题组详情
                    try {
                        Intent intent = new Intent(InformationActivity.this, WebAnswerActivity.class);
                        intent.putExtra("groupId", Integer.parseInt(msg.getUrl()));
                        intent.putExtra("h5Url", msg.getH5Url());
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                    }

                } else if (msg.getType() == 2) {//外部H5
                    Uri uri = Uri.parse(msg.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (msg.getType() == 3) {//内部链接
                    Intent intent = new Intent(InformationActivity.this, WebH5Activity.class);
                    intent.putExtra("url", msg.getUrl());
                    intent.putExtra("name", msg.getTitle());
                    startActivity(intent);

                } else if (msg.getType() == 4) {//实名认证
                    Intent intent = new Intent(InformationActivity.this, RealNameAuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        rvInformation.setLayoutManager(manager);
        rvInformation.setAdapter(infoAdapter);
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
                        List<Msg> msgList = DataSupport.findAll(Msg.class);
                        for (int i = 0; i < msgList.size(); i++) {
                            Msg msg = msgList.get(i);
                            msg.setStatus(2);
                            msg.save();
                        }
                        setData(msgList);
                        //todo status全部置为2、已读状态
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                tvDeleteReaded.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //TODO 删除所有已读消息
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        DataSupport.deleteAll(Msg.class, "status=2");
                        getDataRefreshUi();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


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
}