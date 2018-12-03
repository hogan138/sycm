package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPropsAdapter;
import com.shuyun.qapp.adapter.PrizeAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.MyPropsBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RealNamePopupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的道具
 */
public class MyPropsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_props)
    RecyclerView rvProps;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    MyPropsAdapter myPropsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("我的道具");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadProps();

        myPropsAdapter = new MyPropsAdapter(MyPropsActivity.this, myPropsBeanList);
        myPropsAdapter.setOnItemClickLitsener(new MyPropsAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                MyPropsBean myPropsBean = myPropsBeanList.get(position);
                final String mode = myPropsBean.getPrizeMode();
                CustomLoadingFactory factory = new CustomLoadingFactory();
                LoadingBar.make(llMain, factory).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //使用道具
                        useProps(mode);
                    }
                }, 1000);

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyPropsActivity.this);
        rvProps.setLayoutManager(layoutManager);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_my_props;
    }

    /**
     * 获取到我的道具
     */
    List<MyPropsBean> myPropsBeanList = new ArrayList<>();

    private void loadProps() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.MyProps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<MyPropsBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<MyPropsBean>> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            final List<MyPropsBean> minePrizeList1 = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(minePrizeList1) && minePrizeList1.size() > 0) {
                                ivEmpty.setVisibility(View.GONE);
                                rvProps.setVisibility(View.VISIBLE);
                                myPropsBeanList.clear();
                                myPropsBeanList.addAll(minePrizeList1);
                                rvProps.setAdapter(myPropsAdapter);
                            } else {
                                ivEmpty.setVisibility(View.VISIBLE);
                                rvProps.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(MyPropsActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                        }
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
     * 使用道具
     */
    private void useProps(String mode) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.useProps(mode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<Object> DataResponse) {
                        LoadingBar.cancel(llMain);
                        if (DataResponse.isSuccees()) {
                            Toast.makeText(MyPropsActivity.this, "道具使用成功", Toast.LENGTH_SHORT).show();

                            //刷新道具列表
                            loadProps();
                        } else {
                            ErrorCodeTools.errorCodePrompt(MyPropsActivity.this, DataResponse.getErr(), DataResponse.getMsg());
                        }
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
}
