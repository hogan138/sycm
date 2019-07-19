package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 返回弹框bean
 */
@Data
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


    @Data
    public static class BtnsBean {
        /**
         * label : 确认退出
         * action : return.previous.page
         * h5Url :
         */

        private String label;
        private String action;
        private String h5Url;


    }
}
