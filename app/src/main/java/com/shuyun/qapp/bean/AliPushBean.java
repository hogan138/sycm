package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AliPushBean
 * @Description: 阿里推送接收bean
 * @Author: ganquan
 * @CreateDate: 2018/12/26 15:10
 */
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

    public String get_ALIYUN_NOTIFICATION_PRIORITY_() {
        return _ALIYUN_NOTIFICATION_PRIORITY_;
    }

    public void set_ALIYUN_NOTIFICATION_PRIORITY_(String _ALIYUN_NOTIFICATION_PRIORITY_) {
        this._ALIYUN_NOTIFICATION_PRIORITY_ = _ALIYUN_NOTIFICATION_PRIORITY_;
    }

    public String get_ALIYUN_NOTIFICATION_ID_() {
        return _ALIYUN_NOTIFICATION_ID_;
    }

    public void set_ALIYUN_NOTIFICATION_ID_(String _ALIYUN_NOTIFICATION_ID_) {
        this._ALIYUN_NOTIFICATION_ID_ = _ALIYUN_NOTIFICATION_ID_;
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }

    public String getPushAction() {
        return pushAction;
    }

    public void setPushAction(String pushAction) {
        this.pushAction = pushAction;
    }
}
