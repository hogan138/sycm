package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * 创建日期：2018/9/13 14:33
 * 首页弹框配置
 */
@Data
public class ConfigDialogBean {


    /**
     * baseImage : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/home/dialogBase.jpg
     * showBtn : true
     * btnLabel : 立即跳转
     * btnAction : action.group
     * h5Url : http://192.168.3.137:8080/h5/index.html
     * content : 611
     * count : 2
     */
    private String id;
    private String baseImage;
    private boolean showBtn;
    private String btnLabel;
    private String btnAction;
    private String h5Url;
    private String content;
    private Long count;
    private Long isLogin;


}
