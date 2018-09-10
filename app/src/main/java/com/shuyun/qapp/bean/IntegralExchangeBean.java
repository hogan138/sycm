package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 14:13
 * 积分兑换首页
 */
public class IntegralExchangeBean {

    /**
     * userBp : 8364
     * luckyPicList : ["http://img-syksc.25876.com/syksc/app/prize/ico/hauwei1.png","https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/prize/ico/xiaomi3%402x.png","http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png"]
     * treasurePicList : ["http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png","http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png","http://img-syksc.25876.com/syksc/app/prize/ico/huangs.png","http://img-syksc.25876.com/syksc/app/prize/ico/huangs.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png"]
     * luckyConsBp : 100
     */

    private int userBp;
    private int luckyConsBp;
    private List<String> luckyPicList;
    private List<String> treasurePicList;
    private String ruleUrl;

    public String getRuleUrl() {
        return ruleUrl;
    }

    public void setRuleUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public int getUserBp() {
        return userBp;
    }

    public void setUserBp(int userBp) {
        this.userBp = userBp;
    }

    public int getLuckyConsBp() {
        return luckyConsBp;
    }

    public void setLuckyConsBp(int luckyConsBp) {
        this.luckyConsBp = luckyConsBp;
    }

    public List<String> getLuckyPicList() {
        return luckyPicList;
    }

    public void setLuckyPicList(List<String> luckyPicList) {
        this.luckyPicList = luckyPicList;
    }

    public List<String> getTreasurePicList() {
        return treasurePicList;
    }

    public void setTreasurePicList(List<String> treasurePicList) {
        this.treasurePicList = treasurePicList;
    }
}
