package com.shuyun.qapp.bean;

/**
 * 项目名称：android
 * 创建人：${ganquan}
 * 创建日期：2018/9/13 14:33
 * 首页弹框配置
 */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Long isLogin) {
        this.isLogin = isLogin;
    }

    public String getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(String baseImage) {
        this.baseImage = baseImage;
    }

    public boolean isShowBtn() {
        return showBtn;
    }

    public void setShowBtn(boolean showBtn) {
        this.showBtn = showBtn;
    }

    public String getBtnLabel() {
        return btnLabel;
    }

    public void setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel;
    }

    public String getBtnAction() {
        return btnAction;
    }

    public void setBtnAction(String btnAction) {
        this.btnAction = btnAction;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
