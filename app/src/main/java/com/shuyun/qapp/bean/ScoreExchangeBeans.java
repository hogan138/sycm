package com.shuyun.qapp.bean;

import java.util.List;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: ScoreExchangeBeans
 * @Description: 发现页面积分兑换
 * @Author: ganquan
 * @CreateDate: 2019/3/26 13:55
 */
public class ScoreExchangeBeans {


    private List<PropsBean> props;
    private List<PresentsBean> presents;

    private String ruleUrl;

    public List<PropsBean> getProps() {
        return props;
    }

    public void setProps(List<PropsBean> props) {
        this.props = props;
    }

    public List<PresentsBean> getPresents() {
        return presents;
    }

    public void setPresents(List<PresentsBean> presents) {
        this.presents = presents;
    }

    public String getRuleUrl() {
        return ruleUrl;
    }

    public void setRuleUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public static class PropsBean {
        /**
         * id : 6516149939704303616
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/prize/jfbx/zengcika.png
         * name : 增次卡
         * purpose : 增加一次答题次数
         * action : action.bp.exchange
         * actionLabel : 6积分兑换
         */

        private String id;
        private String picture;
        private String name;
        private String purpose;
        private String action;
        private String actionLabel;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getActionLabel() {
            return actionLabel;
        }

        public void setActionLabel(String actionLabel) {
            this.actionLabel = actionLabel;
        }
    }

    public static class PresentsBean {
        /**
         * id : 6516149941826621441
         * name : 儿童图书
         * picture : 3000积分
         * purpose : 增加一次答题次数
         * action : action.replenishment
         * actionLabel : 补货中
         */

        private String id;
        private String name;
        private String picture;
        private String purpose;
        private String action;
        private String actionLabel;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getActionLabel() {
            return actionLabel;
        }

        public void setActionLabel(String actionLabel) {
            this.actionLabel = actionLabel;
        }
    }
}
