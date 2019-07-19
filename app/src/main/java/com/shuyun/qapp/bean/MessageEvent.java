package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: MessageEvent
 * @Description: 作用描述
 * @Author: ganquan
 * @CreateDate: 2019/4/3 13:30
 */
@Data
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

}
