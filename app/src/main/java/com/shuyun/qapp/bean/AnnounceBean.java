package com.shuyun.qapp.bean;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/19.
 */

public class AnnounceBean {

    /**
     * bp : 20
     * bulletin : 1、每次抽奖将消耗全民积分20个，全民积分可通过答题活动获得。</br>
     * 2、打开宝箱就有机会获得随机赠送的现金及实物奖励、全民积分、虚拟道具等。</br>
     * 3、本平台的抽奖活动，发起方为“全民共进”平台，与其他第三方无关。</br>
     * 4、最终解释权归浙江舒云互联网传媒有限公司所有，未尽说明事项保留法律权利。
     * prizes : [{"showName":"积分","type":2,"mode":2,"purpose":"全民共进平台积分，可以进行抽奖等操作","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","id":1002,"worthLower":2,"worthUpper":2},{"showName":"现金","type":1,"mode":1,"purpose":"满50可提现至支付宝","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","longImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","shortImage":"http://img-syksc.25876.com/syksc/app/prize/ico/qian.png","id":1001,"worthLower":3,"worthUpper":3},{"showName":"红包","type":7,"mode":3,"purpose":"没有金额限制，任何金额都可随时提现至支付宝，请在规定时间内申请提现","mainImage":"http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png","longImage":"1","id":1004,"worthLower":4,"worthUpper":4}]
     */

    private int bp;
    private String bulletin;
    private List<PrizesBean> prizes;

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

    public static class PrizesBean {
        /**
         * showName : 积分
         * type : 2
         * mode : 2
         * purpose : 全民共进平台积分，可以进行抽奖等操作
         * mainImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
         * longImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
         * shortImage : http://img-syksc.25876.com/syksc/app/prize/ico/jf.png
         * id : 1002
         * worthLower : 2
         * worthUpper : 2
         */

        private String showName;
        private int type;
        private int mode;
        private String purpose;
        private String mainImage;
        private String longImage;
        private String shortImage;
        private int id;
        private double worthLower;
        private double worthUpper;

        public String getName() {
            return showName;
        }

        public void setName(String showName) {
            this.showName = showName;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
    }
}
