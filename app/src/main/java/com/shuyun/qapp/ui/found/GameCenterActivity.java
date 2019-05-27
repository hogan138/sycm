package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.GameCenterAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GameListBeans;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 游戏中心
 */

public class GameCenterActivity extends BaseActivity implements OnRemotingCallBackListener {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_game_center)
    RecyclerView rvGameCenter;
    @BindView(R.id.ll_main)
    LinearLayout llMain;


    private Context mContext;
    private Handler mHandler = new Handler();

    private List<GameListBeans> gameListBeansList = new ArrayList<>();

    private GameCenterAdapter gameCenterAdapter;

    private String title = ""; //游戏标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("游戏中心");

        gameCenterAdapter = new GameCenterAdapter(mContext, gameListBeansList);
        gameCenterAdapter.setOnItemClickLitsener(new GameCenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GameListBeans gameListBeans = gameListBeansList.get(position);
                String url = gameListBeans.getUrl();
                title = gameListBeans.getName();
                CustomLoadingFactory factory = new CustomLoadingFactory();
                LoadingBar.make(llMain, factory).show();
                //游戏认证
                GameAuth(url);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvGameCenter.setLayoutManager(layoutManager);
        rvGameCenter.setHasFixedSize(true);
        rvGameCenter.setNestedScrollingEnabled(false);
        rvGameCenter.setAdapter(gameCenterAdapter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取游戏列表
                loadGame();
            }
        }, 10);


    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //游戏列表
    private void loadGame() {
        RemotingEx.doRequest("gameList", RemotingEx.Builder().gameList(), this);
    }

    //游戏认证
    private void GameAuth(String url) {
        RemotingEx.doRequest("gameAuth", RemotingEx.Builder().gameAuth(url), this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_game_center;
    }

    @Override
    public void onCompleted(String action) {
        LoadingBar.cancel(llMain);

    }

    @Override
    public void onFailed(String action, String message) {
        LoadingBar.cancel(llMain);
    }

    @Override
    public void onSucceed(String action, DataResponse response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if ("gameList".equals(action)) {
            //游戏列表
            List<GameListBeans> gameListBeansList1 = ObjectUtil.cast(response.getDat());
            if (!EncodeAndStringTool.isListEmpty(gameListBeansList1)) {
                rvGameCenter.setVisibility(View.VISIBLE);
                gameListBeansList.clear();
                gameListBeansList.addAll(gameListBeansList1);
                gameCenterAdapter.notifyDataSetChanged();
            } else {
                rvGameCenter.setVisibility(View.GONE);
            }
        } else if ("gameAuth".equals(action)) {
            //游戏认证
            GameListBeans gameListBeans = ObjectUtil.cast(response.getDat());
            String url = gameListBeans.getUrl();
            Intent intent = new Intent(mContext, WebH5Activity.class);
            intent.putExtra("url", url);
            intent.putExtra("name", title);//标题
            startActivity(intent);
        }

    }
}
