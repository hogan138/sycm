package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeNoticeBean
 * @Description: 首页公告bean
 * @Author: ganquan
 * @CreateDate: 2018/11/22 15:00
 */
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
