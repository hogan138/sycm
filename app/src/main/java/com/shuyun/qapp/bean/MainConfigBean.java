package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 首页活动配置bean
 */
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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getColspan() {
            return colspan;
        }

        public void setColspan(String colspan) {
            this.colspan = colspan;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }

        public String getRowspan() {
            return rowspan;
        }

        public void setRowspan(String rowspan) {
            this.rowspan = rowspan;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }
}
