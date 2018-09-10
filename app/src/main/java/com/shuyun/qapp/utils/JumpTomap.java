package com.shuyun.qapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/11 13:43
 * 唤起本机地图
 */
public class JumpTomap {


    //跳转到高德地图
    public static void goToGaode(Context context, String addressName) {
        if (isAvilible(context, "com.autonavi.minimap")) {
            try {
                Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dname=" + addressName + "&dev=0&m=0&t=1");
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();
        }

    }

    //跳转到百度地图
    public static void goToBaidu(Context context, String addressName) {
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination=" + addressName + "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isAvilible(context, "com.baidu.BaiduMap")) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    //跳转到腾讯地图
    public static void goToTencent(Context context, String addressName, String latitude, String longitude) {
        if (isAvilible(context, "com.tencent.map")) {
            try {
                Intent intent = Intent.getIntent("qqmap://map/routeplan?type=drive&to=" + addressName + "&tocoord=" + latitude + "," + longitude + "&policy=2&referer=myapp");
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "没有安装腾讯地图客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
