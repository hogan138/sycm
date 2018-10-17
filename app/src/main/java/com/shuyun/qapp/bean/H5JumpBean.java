package com.shuyun.qapp.bean;

/**
 * 与h5交互跳转bean
 */
public class H5JumpBean {


    /**
     * btnAction : action.real
     * content : 1
     * h5Url : 1
     */

    private String btnAction;
    private String content;
    private String h5Url;

    public String getBtnAction() {
        return btnAction;
    }

    public void setBtnAction(String btnAction) {
        this.btnAction = btnAction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }
}
