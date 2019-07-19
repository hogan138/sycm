package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/21.
 * 首页题组
 */
@Data
public class HomeGroupsBean {
    /**
     * 推荐题组列表
     */
    private List<GroupBean> recommend;
    /**
     * 大家都在答题组列表
     */
    private List<GroupBean> thermal;

    /**
     * 常答题组
     */
    private List<GroupBean> often;

    /**
     * 题组树（分类）
     */
    private List<GroupClassifyBean> tree;



    @Override
    public String toString() {
        return "HomeGroupsBean{" +
                "recommend=" + recommend +
                ", thermal=" + thermal +
                ", tree=" + tree +
                '}';
    }
}
