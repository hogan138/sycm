package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/8/2 14:13
 * 积分兑换首页
 */
@Data
public class IntegralExchangeBean {

    /**
     * userBp : 8364
     * luckyPicList : ["http://img-syksc.25876.com/syksc/app/prize/ico/hauwei1.png","https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/prize/ico/xiaomi3%402x.png","http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png"]
     * treasurePicList : ["http://img-syksc.25876.com/syksc/app/prize/ico/xianjin.png","http://img-syksc.25876.com/syksc/app/prize/ico/jf.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","http://img-syksc.25876.com/syksc/app/prize/ico/hongbao.png","http://img-syksc.25876.com/syksc/app/prize/ico/huangs.png","http://img-syksc.25876.com/syksc/app/prize/ico/huangs.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/taipingh.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/huashang.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png","http://img-syksc.25876.com/syksc/app/prize/ico/zengcik.png"]
     * luckyConsBp : 100
     */

    private Long userBp;
    private Long luckyConsBp;
    private Long flipConsBp;
    private List<String> luckyPicList;
    private List<String> treasurePicList;
    private List<String> flipPicList;
    private String ruleUrl;
    private String h5Url; //开宝箱地址
    private String flipUrl; //翻牌地址
    private BpExpireBean bpExpire; //积分过期

    @Data
    public static class BpExpireBean {
        /**
         * bp : 275
         * time : 2019年06月30日
         */

        private Long bp;
        private String time;
        private String h5Url;
    }


}
