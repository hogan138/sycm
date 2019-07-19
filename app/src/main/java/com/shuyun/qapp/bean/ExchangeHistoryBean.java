package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/3 11:23
 * 兑换记录
 */
@Data
public class ExchangeHistoryBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * prizePurpose : 满50可提现至支付宝
     * treasureUserChangeDataList : [{"changeTime":1533017199000,"userAccount":"158****1563","ticketNum":"ser35151w6e51","isMine":1,"isWin":1},{"changeTime":1533174736567,"userAccount":"158****1563","ticketNum":"e4dd41b0a55f4bf69b69397e9dd713e9","isMine":1,"isWin":0},{"changeTime":1533174783574,"userAccount":"158****1563","ticketNum":"94db38edf08942ed9f46c2c96f01aef9","isMine":1,"isWin":1},{"changeTime":1533174895768,"userAccount":"158****1563","ticketNum":"ebcd52a8637a4e69b8aac8774705edb7","isMine":1,"isWin":0},{"changeTime":1533194477599,"userAccount":"158****1563","ticketNum":"fde534da0966405aa0a6fe9f86076ab9","isMine":1,"isWin":0}]
     */

    private Long scheduleId;
    private String prizeName;
    private String prizePurpose;
    private Long scheduleStatus;
    private String mainPic;
    private List<TreasureUserChangeDataListBean> treasureUserChangeDataList;

    @Data
    public static class TreasureUserChangeDataListBean {
        /**
         * changeTime : 1533017199000
         * userAccount : 158****1563
         * ticketNum : ser35151w6e51
         * isMine : 1
         * isWin : 1
         */

        private Long changeTime;
        private String userAccount;
        private String ticketNum;
        private Long isMine;
        private Long isWin;


    }
}
