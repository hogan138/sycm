package com.shuyun.qapp.bean;

import java.util.List;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeBottomInfoBean
 * @Description: 首页底部信息
 * @Author: ganquan
 * @CreateDate: 2019/3/5 10:09
 */
public class HomeBottomInfoBean {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * call : 400-650-9258
         * label : 客服热线:
         * value : 400-650-9258
         */

        private String call;
        private String label;
        private String value;

        public String getCall() {
            return call;
        }

        public void setCall(String call) {
            this.call = call;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
