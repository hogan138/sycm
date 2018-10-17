package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 返回弹框bean
 */
public class ReturnDialogBean {

    /**
     * show : true
     * title : 您已经完成线上申请操作了么
     * content : d大大大无无无无
     * btn : ["已完成","未完成"]
     */

    private boolean show;
    private String title;
    private String content;
    private List<String> btn;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getBtn() {
        return btn;
    }

    public void setBtn(List<String> btn) {
        this.btn = btn;
    }
}
