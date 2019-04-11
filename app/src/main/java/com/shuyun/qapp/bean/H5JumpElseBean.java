package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: H5JumpElseBean
 * @Description: h5跳转外部app
 * @Author: ganquan
 * @CreateDate: 2019/4/11 9:49
 */
public class H5JumpElseBean {

    private String urlScheme; //url
    private String appName;//app名称

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public void setUrlScheme(String urlScheme) {
        this.urlScheme = urlScheme;
    }

}
