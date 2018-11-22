package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：Administrator
 * 创建日期：2018/6/15 11:38
 * 答对三次获取扩展信息
 */
public class ThreeFinishBean {

    /**
     * bp : 0
     * bulletin : 答对三次有大奖规则 4444
     * prizes : [{"name":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":0.1,"worthUpper":0.5},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":2,"worthUpper":2},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":3,"worthUpper":3},{"name":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":0.5,"worthUpper":1}]
     */

    private int bp;
    private String bulletin;
    private List<PrizesBean> prizes;
    /**
     * 状态
     * 0——不满足扩展规则条件
     * 1——已经满足，但是未抽奖
     * 2——已经抽奖完毕
     */
    private int status;

    public int getBp() {
        return bp;
    }

    public void setBp(int bp) {
        this.bp = bp;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public List<PrizesBean> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrizesBean> prizes) {
        this.prizes = prizes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ThreeFinishBean{" +
                "bp=" + bp +
                ", bulletin='" + bulletin + '\'' +
                ", prizes=" + prizes +
                ", status=" + status +
                '}';
    }

    public static class PrizesBean {
        /**
         * showName : 现金
         * type : 1
         * mode : 1
         * purpose : 满50可提现至支付宝
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * longImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/qian.png
         * id : 1001
         * worthLower : 0.1
         * worthUpper : 0.5
         */

        private String showName;
        private int type;
        private int mode;
        private String purpose;
        private String mainImage;
        private String longImage;
        private String shortImage;
        private Long id;
        private double worthLower;
        private double worthUpper;

        public String getName() {
            return showName;
        }

        public void setName(String name) {
            this.showName = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getLongImage() {
            return longImage;
        }

        public void setLongImage(String longImage) {
            this.longImage = longImage;
        }

        public String getShortImage() {
            return shortImage;
        }

        public void setShortImage(String shortImage) {
            this.shortImage = shortImage;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public double getWorthLower() {
            return worthLower;
        }

        public void setWorthLower(double worthLower) {
            this.worthLower = worthLower;
        }

        public double getWorthUpper() {
            return worthUpper;
        }

        public void setWorthUpper(double worthUpper) {
            this.worthUpper = worthUpper;
        }

        @Override
        public String toString() {
            return "PrizesBean{" +
                    "showName='" + showName + '\'' +
                    ", type=" + type +
                    ", mode=" + mode +
                    ", purpose='" + purpose + '\'' +
                    ", mainImage='" + mainImage + '\'' +
                    ", longImage='" + longImage + '\'' +
                    ", shortImage='" + shortImage + '\'' +
                    ", id=" + id +
                    ", worthLower=" + worthLower +
                    ", worthUpper=" + worthUpper +
                    '}';
        }
    }
}
