package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * 立即兑换入参
 */

@Data
public class GoodsCommitBean {

    private List<StandardsBean> standards; //规格
    private Long count; //数量
    private Long addressId; //地址id


    @Data
    public static class StandardsBean {

        private String param;
        private String value;

    }

}
