package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: ScoreExchangeBeans
 * @Description: 发现页面积分兑换
 * @Author: ganquan
 * @CreateDate: 2019/3/26 13:55
 */
@Data
public class ScoreExchangeBeans {


    private List<PropsBean> props;
    private List<PresentsBean> presents;

    private String ruleUrl;


    @Data
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


    }

    @Data
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


    }
}
