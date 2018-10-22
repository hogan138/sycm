package com.shuyun.qapp.bean;

/**
 * h5返回分享bean
 */
public class SharePublicBean {


    /**
     * id :
     * action : share.challenge
     */

    private boolean show;
    private String id;
    private String action;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
