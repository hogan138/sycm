package com.shuyun.qapp.alipay;

import android.app.Activity;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.alipay
 * @ClassName: AlipayTradeManager
 * @Description: 电商
 * @Author: ganquan
 * @CreateDate: 2019/1/2 16:23
 */
public class AlipayTradeManager {

    private static AlipayTradeManager manager = null;

    protected AlipayTradeManager() {

    }

    public static AlipayTradeManager instance() {
        if (manager == null)
            manager = new AlipayTradeManager();
        return manager;
    }

    /**
     * 显示商品详情
     *
     * @param mContext
     * @param itemId
     */
    public void showDetailPage(Activity mContext, String itemId) {
        //商品详情page
        AlibcBasePage tradePage = new AlibcDetailPage(itemId);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        Map<String, String> exParams = new HashMap<>();
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(mContext, tradePage, showParams, null, exParams, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                Log.d("D", "");
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                Log.e("D", msg);
            }
        });
    }

    /**
     * 打开链接地址
     *
     * @param mContext
     * @param url
     */
    public void showBasePage(Activity mContext, String url) {
        //实例化URL打开page
        AlibcBasePage tradePage = new AlibcPage(url);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        Map<String, String> exParams = new HashMap<>();
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(mContext, tradePage, showParams, null, exParams, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                Log.d("D", "");
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                Log.e("D", msg);
            }
        });
    }

    /**
     * 打开我的订单
     *
     * @param mContext
     * @param status
     */
    public void showMyOrdersPage(Activity mContext,int status) {
        //实例化我的订单打开page
        AlibcBasePage ordersPage = new AlibcMyOrdersPage(status, false);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        Map<String, String> exParams = new HashMap<>();
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(mContext, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                Log.d("D", "");
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                Log.e("D", msg);
            }
        });
    }
}
