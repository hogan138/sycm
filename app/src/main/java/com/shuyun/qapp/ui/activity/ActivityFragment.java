package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 活动Fragment
 */
public class ActivityFragment extends Fragment {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    Unbinder unbinder;
    @BindView(R.id.rv_activity)
    RecyclerView rvActivity;
    private Activity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvCommonTitle.setText("活动");

        loadGroupTree();

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.changeUi(0);
                    homePageActivity.changeFragment(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取活动列表
     */
    private void loadGroupTree() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getGroupTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<GroupClassifyBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<GroupClassifyBean>> dataResponse) {
                        if (dataResponse.isSuccees()) {

                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ActivityFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ActivityFragment");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

