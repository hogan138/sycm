package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/3 11:23
 * 兑换记录
 */
public class ExchangeHistoryBean {


    /**
     * scheduleId : 1
     * prizeName : 现金
     * prizePurpose : 满50可提现至支付宝
     * treasureUserChangeDataList : [{"changeTime":1533017199000,"userAccount":"158****1563","ticketNum":"ser35151w6e51","isMine":1,"isWin":1},{"changeTime":1533174736567,"userAccount":"158****1563","ticketNum":"e4dd41b0a55f4bf69b69397e9dd713e9","isMine":1,"isWin":0},{"changeTime":1533174783574,"userAccount":"158****1563","ticketNum":"94db38edf08942ed9f46c2c96f01aef9","isMine":1,"isWin":1},{"changeTime":1533174895768,"userAccount":"158****1563","ticketNum":"ebcd52a8637a4e69b8aac8774705edb7","isMine":1,"isWin":0},{"changeTime":1533194477599,"userAccount":"158****1563","ticketNum":"fde534da0966405aa0a6fe9f86076ab9","isMine":1,"isWin":0}]
     */

    private int scheduleId;
    private String prizeName;
    private String prizePurpose;
    private int scheduleStatus;
    private String mainPic;
    private List<TreasureUserChangeDataListBean> treasureUserChangeDataList;

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizePurpose() {
        return prizePurpose;
    }

    public void setPrizePurpose(String prizePurpose) {
        this.prizePurpose = prizePurpose;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public List<TreasureUserChangeDataListBean> getTreasureUserChangeDataList() {
        return treasureUserChangeDataList;
    }

    public void setTreasureUserChangeDataList(List<TreasureUserChangeDataListBean> treasureUserChangeDataList) {
        this.treasureUserChangeDataList = treasureUserChangeDataList;
    }

    public static class TreasureUserChangeDataListBean {
        /**
         * changeTime : 1533017199000
         * userAccount : 158****1563
         * ticketNum : ser35151w6e51
         * isMine : 1
         * isWin : 1
         */

        private long changeTime;
        private String userAccount;
        private String ticketNum;
        private int isMine;
        private int isWin;

        public long getChangeTime() {
            return changeTime;
        }

        public void setChangeTime(long changeTime) {
            this.changeTime = changeTime;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getTicketNum() {
            return ticketNum;
        }

        public void setTicketNum(String ticketNum) {
            this.ticketNum = ticketNum;
        }

        public int getIsMine() {
            return isMine;
        }

        public void setIsMine(int isMine) {
            this.isMine = isMine;
        }

        public int getIsWin() {
            return isWin;
        }

        public void setIsWin(int isWin) {
            this.isWin = isWin;
        }
    }
}
