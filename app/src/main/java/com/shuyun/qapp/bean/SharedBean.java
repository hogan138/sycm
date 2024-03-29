package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * Created by sunxiao on 2018/5/10.
 * 答题分享
 */
@Data
public class SharedBean {

    /**
     * err : 00000
     * msg : null
     * ver : 1
     * dat : {"id":10,"title":"一起来答题吧","content":"我在全民共进答题，发现很有趣，一起来答题吧！","url":"http://wx.25876.com/share/h5/exam?p=%7B%22inviter%22%3A130%2C%22exam%22%3A%224d9cb6ccbf2d431294cee783c31c37fb%22%2C%22group%22%3A108%7D"}
     */

    /**
     * id : 10
     * title : 一起来答题吧
     * content : 我在全民共进答题，发现很有趣，一起来答题吧！
     * url : http://wx.25876.com/share/h5/exam?p=%7B%22inviter%22%3A130%2C%22exam%22%3A%224d9cb6ccbf2d431294cee783c31c37fb%22%2C%22group%22%3A108%7D
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//分享id
    private String title;//分享文案标题
    private String content;//分享文案内容
    private String url;//分享的网页地址

    private String time;//衢州活动,时间戳
    private String picture;//背景图
    private String buttonCaption;//按钮标题


    @Override
    public String toString() {
        return "SharedBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", picture='" + picture + '\'' +
                ", buttonCaption='" + buttonCaption + '\'' +
                '}';
    }
}
