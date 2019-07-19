package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 首页活动配置bean
 */
@Data
public class MainConfigBean {

    /**
     * datas : [{"template":"1","colspan":"0","h5Url":"http://192.168.3.137:8080/h5_invitation/index.html","rowspan":"0","icon":"http://192.168.3.137:8080/icon/box.png","count":"0","column":"1","action":"action.invite","remark":"共计送出23552个邀请宝箱","row":"1","title":"邀请好友得宝箱","height":"57"},{"template":"2","colspan":"0","rowspan":"1","icon":"http://192.168.3.137:8080/icon/rocketcopy.png","count":"0","column":"2","action":"action.answer.against","remark":"共32423234位用户在线","row":"1","title":"答题对战","height":"114"},{"template":"3","colspan":"0","rowspan":"0","icon":"http://192.168.3.137:8080/icon/box.png","count":"15","column":"1","action":"action.day.task","remark":"做任务赢好礼，积分奖品送不停","row":"2","title":"每日任务","height":"57"}]
     * column : 2
     * row : 2
     * height : 114
     */

    private String column; //列
    private String row; //行
    private String height; //高度
    private List<DatasBean> datas; //配置数据


    @Data
    public static class DatasBean {
        /**
         * template : 1
         * colspan : 0
         * h5Url : http://192.168.3.137:8080/h5_invitation/index.html
         * rowspan : 0
         * icon : http://192.168.3.137:8080/icon/box.png
         * count : 0
         * column : 1
         * action : action.invite
         * remark : 共计送出23552个邀请宝箱
         * row : 1
         * title : 邀请好友得宝箱
         * height : 57
         */

        private String template;
        private String colspan;
        private String h5Url;
        private String rowspan;
        private String icon;
        private String count;
        private String column;
        private String action;
        private String remark;
        private String row;
        private String title;
        private String height;
        private String content;
        private Long isLogin;


    }
}
