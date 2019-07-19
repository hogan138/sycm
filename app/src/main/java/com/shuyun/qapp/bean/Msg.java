package com.shuyun.qapp.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import lombok.Data;

/**
 * 系统消息bean
 */
@Data
public class Msg extends LitePalSupport {

    @Column(unique = true)
    private Long id;//消息id
    private String msgId; //消息id
    /**
     * 消息类型
     * 0——不跳转
     * 1——题组详情
     * 2——外部h5
     * 3——内部h5
     * 4——实名认证
     */
    private Long type;
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
    private Long time;//消息时间
    private String h5Url; //答题url


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
