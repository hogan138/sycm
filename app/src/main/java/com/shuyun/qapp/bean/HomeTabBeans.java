package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeTabBeans
 * @Description: 首页tab
 * @Author: ganquan
 * @CreateDate: 2019/5/7 15:03
 */
@Data
public class HomeTabBeans extends TitleBean {

    /**
     * tabId : 6530989258759606272
     * label : 专题
     * type : 2
     */

    private Long tabId;
    private String label;
    private Long type;
    private String h5Url;
    private Boolean selected = false;
}
