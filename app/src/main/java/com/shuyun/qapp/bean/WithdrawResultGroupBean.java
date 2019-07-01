package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: WithdrawResultGroupBean
 * @Description: 提现结果页面题组bean
 * @Author: ganquan
 * @CreateDate: 2019/7/1 14:52
 */
@Data
public class WithdrawResultGroupBean {


    private List<SmallBean> small;
    private List<BigBean> big;


    @Data
    public static class SmallBean {
        /**
         * h5Url : http://192.168.3.157/web/h5/index.html
         * name : 冬季养生
         * action : action.group
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/djys.jpg
         * content : 622
         * actionLabel : 立即答题
         */

        private String h5Url;
        private String name;
        private String action;
        private String picture;
        private String content;
        private String actionLabel;

    }

    @Data
    public static class BigBean {
        /**
         * h5Url : http://192.168.3.157/web/h5_hzdaily/index.html
         * name : 我的祖国我的城 我的地铁我的站
         * action : action.group
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/hzdaily.jpg
         * content : 6511853562161664000
         * actionLabel :
         */

        private String h5Url;
        private String name;
        private String action;
        private String picture;
        private String content;
        private String actionLabel;

    }
}
