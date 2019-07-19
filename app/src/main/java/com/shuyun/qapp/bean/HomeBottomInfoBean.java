package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeBottomInfoBean
 * @Description: 首页底部信息
 * @Author: ganquan
 * @CreateDate: 2019/3/5 10:09
 */
@Data
public class HomeBottomInfoBean {


    private List<ResultBean> result;


    @Data
    public static class ResultBean {
        /**
         * call : 400-650-9258
         * label : 客服热线:
         * value : 400-650-9258
         */

        private String call;
        private String label;
        private String value;


    }
}
