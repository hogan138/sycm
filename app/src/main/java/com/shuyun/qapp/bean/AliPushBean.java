package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AliPushBean
 * @Description: 阿里推送接收bean
 * @Author: ganquan
 * @CreateDate: 2018/12/26 15:10
 */
@Data
public class AliPushBean {

    /**
     * _ALIYUN_NOTIFICATION_PRIORITY_ : 1
     * _ALIYUN_NOTIFICATION_ID_ : 442864
     * pushData :
     * pushAction : push.integral.snatch.notify
     */

    private String _ALIYUN_NOTIFICATION_PRIORITY_;
    private String _ALIYUN_NOTIFICATION_ID_;
    private String pushData;
    private String pushAction;

}
