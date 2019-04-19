package com.shuyun.qapp.bean;

import java.util.List;

public class GoodsCommitBean {

    private List<StandardsBean> standards; //规格
    private Long count; //数量
    private Long addressId; //地址id

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<StandardsBean> getStandards() {
        return standards;
    }

    public void setStandards(List<StandardsBean> standards) {
        this.standards = standards;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public static class StandardsBean {

        private String param;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }


        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

    }

}
