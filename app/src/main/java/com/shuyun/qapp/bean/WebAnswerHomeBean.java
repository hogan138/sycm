package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
 * Created by sunxiao on 2018/6/27.
 * 答题首页需要传给H5的参数
 */

public class WebAnswerHomeBean {

    private String token;//登录返回参数
    private String random;//登录返回参数
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId;//题组id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long act;//活动id;获取题组答题详情接口返回
    private Long v;
    private String salt;
    /**
     * 后台配的appKey
     */
    private String appSecret;
    //是否参与邀请分享 1——参与邀请
    private Integer share;
    private String deviceId;//设备id  数美sdk得到的设备id

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setAct(Long act) {
        this.act = act;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getToken() {
        return token;
    }

    public String getRandom() {
        return random;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getAct() {
        return act;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getV() {
        return v;
    }

    public String getSalt() {
        return salt;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public Integer getShare() {
        return share;
    }

    @Override
    public String toString() {
        return "WebAnswerHomeBean{" +
                "token='" + token + '\'' +
                ", random='" + random + '\'' +
                ", groupId=" + groupId +
                ", act=" + act +
                ", v=" + v +
                ", salt='" + salt + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", share=" + share +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
