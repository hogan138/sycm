package com.shuyun.qapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户答题记录
 */

public class HistoryDataBean implements Serializable {

    private Long total;//记录总数
    private List<ResultBean> result;//记录

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        private String id;//答题id
        private Long groupId;//题组id
        private String picture;//题组主图
        private String time;//答题时间
        private String accuracy;//正确率
        private String title;//题组名称
        private String merchantName;//商户名称
        private Long result;
        private String fullName; //所在分类

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public Long getResult() {
            return result;
        }

        public void setResult(Long result) {
            this.result = result;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }
}
