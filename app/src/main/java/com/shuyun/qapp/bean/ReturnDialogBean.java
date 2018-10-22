package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 返回弹框bean
 */
public class ReturnDialogBean {

    /**
     * show : true
     * title : 确认要中途退出吗？
     * content : 现在退出无法进入榜单哦
     * btns : [{"label":"确认退出","action":"return.previous.page","h5Url":""},{"label":"继续答题","action":"continue.to.perform","h5Url":""}]
     */

    private boolean show;
    private String title;
    private String content;
    private List<BtnsBean> btns;

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

    public List<BtnsBean> getBtns() {
        return btns;
    }

    public void setBtns(List<BtnsBean> btns) {
        this.btns = btns;
    }

    public static class BtnsBean {
        /**
         * label : 确认退出
         * action : return.previous.page
         * h5Url :
         */

        private String label;
        private String action;
        private String h5Url;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }
    }
}
