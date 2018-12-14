package com.shuyun.qapp.event;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.event
 * @ClassName: MessageEvent
 * @Description: 定义消息事件类
 * @Author: ganquan
 * @CreateDate: 2018/12/6 10:11
 */
public class MessageEvent {
    private String message;
    private Object data;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
