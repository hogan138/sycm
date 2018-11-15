package com.shuyun.qapp.bean;

import org.litepal.crud.DataSupport;

/**
 * 系统消息bean
 */

public class Msg extends DataSupport {

    private int id;//消息id
    /**
     * 消息类型
     * 0——不跳转
     * 1——题组详情
     * 2——外部h5
     * 3——内部h5
     * 4——实名认证
     */
    private int type;
    private String title;//消息的标题
    /**
     * 消息的内容
     */
    private String content;
    /**
     * 跳转的地址
     * 根据type而定：
     * Type=1表示题组id
     * Type=4没有值
     */
    private String url;
    /**
     * 消息阅读状态
     * 1——新消息（未读）
     * 2——已读
     */
    private int status;
    private long time;//消息时间
    private String h5Url; //答题url

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", time=" + time +
                ", id=" + id +
                '}';
    }
}
