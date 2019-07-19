package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import lombok.Data;

/**
 * Created by sunxiao on 2018/6/27.
 * 答题首页需要传给H5的参数
 */
@Data
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
    private String pages; //友盟页面统计记录

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
                ", pages='" + pages + '\'' +
                '}';
    }

}
