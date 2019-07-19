package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeNoticeBean
 * @Description: 首页公告bean
 * @Author: ganquan
 * @CreateDate: 2018/11/22 15:00
 */
@Data
public class HomeNoticeBean {

    /**
     * action : action.group
     * h5Url : http://192.168.3.157/web/h5/index.html
     * content : 全民共进挑战赛火热开启<br/><p style="color: red;">90秒内容题冲榜</p>，上榜得好礼 详情速戳 >>
     * groupId : 625
     */

    private String action;
    private String h5Url;
    private String content;
    private String groupId;
    private Long isLogin;


}
