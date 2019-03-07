package com.shuyun.qapp.net;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.adapter.ut.AlibcUserTracker;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.blankj.utilcode.util.Utils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.shuyun.qapp.R;
import com.shuyun.qapp.utils.CrashHandler;
import com.shuyun.qapp.utils.MTACrashModule;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.hybrid.StatHybridHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.ut.mini.internal.UTTeamWork;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.HashMap;
import java.util.Map;

public class SykscApplication extends Application {


    private static final String TAG = "SykscApplication";

    /**
     * 取得当前进程名
     *
     * @param context
     * @return
     */
    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//兼容android4.4及以下系统版本
    }

    /**
     * app全局context
     */
    private static Context appContext;
    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 全局Context
         * 使用Application的context,防止内存泄漏
         */
        appContext = getApplicationContext();

        //初始化工具类
        Utils.init(this);


        //阿里推送
        initCloudChannel(this);

        //Litepal数据库
        LitePal.initialize(appContext);
        /**
         * 创建数据库创建表
         */
        Connector.getDatabase();

        CrashHandler.instance().init();

        //数美
        //如果 AndroidManifest.xml 中没有指定主进程名字，主进程名默认与 packagename 相同
        if (this.getPackageName().equals(getCurProcessName(appContext))) {
            SmAntiFraud.SmOption option = new SmAntiFraud.SmOption();
            String DEBUG_ORG = "7pLqYjvuoJeakPdaNjEj";// organization 代码 不要传 AccessKey
            option.setOrganization(DEBUG_ORG);
            option.setChannel("sycm_syksc");//渠道代码
            //如果是首次启动 App，设置为 true，否则设置为 false 或不设置。
            if (!option.isFirst()) {
                option.setFirst(true);
            } else {
                option.setFirst(false);
            }
            //可选的方式，deviceId 拉取成功的事件监听，异步方式
            SmAntiFraud.registerServerIdCallback(
                    new SmAntiFraud.IServerSmidCallback() {
                        @Override
                        public void onSuccess(String s) {
                            Log.i(TAG, "deviceId is " + s);
                        }

                        @Override
                        public void onError(int i) {

                        }

                    });
            SmAntiFraud.create(appContext, option);
            // 注意！！获取 deviceId，这个接口在真正的注册或登录事件产生的地方调用。
            // create 后马上调用返回的是本地的 deviceId，
            // 本地 deviceId 和服务器同步需要一点时间。
//            String deviceId = SmAntiFraud.getDeviceId();
        }
        AppConst.loadToken(appContext);

        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret,如果不用集成推送业务，则传空
         */
        UMConfigure.init(appContext, UMConfigure.DEVICE_TYPE_PHONE, "");//友盟
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);//友盟
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);//友盟
        /**
         *  context	当前宿主进程的ApplicationContext上下文。
         *  etype	EScenarioType.E_UM_NORMAL 普通统计场景，如果您在埋点过程中没有使用到
         *  U-Game统计接口，请使用普通统计场景。
         *  EScenarioType.E_UM_GAME 游戏场景 ，如果您在埋点过程中需要使用到U-Game
         *   统计接口，则必须设置游戏场景，否则所有的U-Game统计接口不会生效。
         */
        MobclickAgent.setScenarioType(appContext, MobclickAgent.EScenarioType.E_UM_NORMAL);//友盟

        //向微信注册APP
        registToWX();//微信

        /**
         * 微信配置
         */
        PlatformConfig.setWeixin("wxb9167b3dcd6daa1e", "4233d16c2cf81ff153f337d4fefe09d6");//友盟

        // 请在初始化时调用，参数为Application或Activity或Service
        //StatisticsDataAPI.instance(this);
        StatService.setContext(appContext);
        // TLink功能，true：开启；false：关闭，默认值
        StatConfig.setTLinkStatus(true);
        // hybrid统计功能初始化
        StatHybridHandler.init(appContext);

        //  初始化MTA配置
        initMTAConfig(true);
        // 注册Activity生命周期监控，自动统计时长
        StatService.registerActivityLifecycleCallbacks(this);
        // 初始化MTA的Crash模块，可监控java、native的Crash，以及Crash后的回调
        MTACrashModule.initMtaCrashModule(appContext);

        //阿里百川初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                Log.d(TAG, "电商初始化成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                Log.d(TAG, "电商初始化失败:" + msg);
            }
        });

    }

    /**
     * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
     *
     * @param isDebugMode 根据调试或发布条件，配置对应的MTA配置
     */
    private void initMTAConfig(boolean isDebugMode) {

        if (isDebugMode) { // 调试时建议设置的开关状态
            // 查看MTA日志及上报数据内容
            StatConfig.setDebugEnable(true);
        } else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
            // 禁止MTA打印日志
            StatConfig.setDebugEnable(false);
            // 根据情况，决定是否开启MTA对app未处理异常的捕获
            StatConfig.setAutoExceptionCaught(true);
            // 选择默认的上报策略
            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
            // 10分钟上报一次的周期
            StatConfig.setSendPeriodMinutes(10);
        }

        // 初始化java crash捕获
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
        // 初始化native crash捕获，记得复制so文件
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
        // crash的回调，请根据需要添加
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).addCrashCallback(new StatCrashCallback() {

            @Override
            public void onJniNativeCrash(String tombstoneMsg) {
                Log.d("qmgj", "Native crash happened, tombstone message:" + tombstoneMsg);
            }

            @Override
            public void onJavaCrash(Thread thread, Throwable throwable) {
                Log.d("qmgj", "Java crash happened, thread: " + thread + ",Throwable:" + throwable.toString());
            }
        });
    }

    public static Context getAppContext() {
        /**
         * 获取全局的context上下文
         */
        return appContext;
    }

    private void registToWX() {
        //AppConst.WEIXIN_APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(appContext, AppConst.WEIXIN_APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(AppConst.WEIXIN_APP_ID);
    }

    //下拉刷新
    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_12, R.color.color_30);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "SykscApplication == onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "SykscApplication ==onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "SykscApplication == onTrimMemory");
        super.onTrimMemory(level);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
                Log.d(TAG, pushService.getDeviceId());
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(applicationContext, "2882303761517539952", "5421753971952");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
        //OPPO通道注册（公测）
        //OppoRegister.register(applicationContext, appKey, appSecret); // appKey/appSecret在OPPO通道开发者平台获取

        //Android 8.0以上设备通知接收不到
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

}
