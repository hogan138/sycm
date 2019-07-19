package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;

import lombok.Data;

/**
 * 登录返回
 */
@Data
public class LoginResponse implements Serializable {
    private String token;//返回的token
    private Long expire;//token的有效期
    private String key;//对称加密的秘钥。用请求方的私钥加密
    private String random;//接口随机生成的字符串
    private Integer bind;//是否绑定用户。0:未绑定;1:已绑定
    private String invite;//邀请分享的展现图片地址  当user.share=1时有效
    private User user;//对象
    private Boolean setPwd;
    private String openId;
    private String wxName;
    private String wxHeadUrl;
    private String boxId; //宝箱ID
    private String certification; //是否实名


    @Data
    public static class User implements Serializable {
        private String nickname;//用户的昵称
        private Integer type;//用户类型   1——正式用户 0——内部用户
        private String headerId;//用户的头像
        private Integer ob;//是否参与公测 1——参与公测
        private Integer share;//是否参与邀请分享   1——参与邀请
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;



        @Override
        public String toString() {
            return "User{" +
                    "nickname='" + nickname + '\'' +
                    ", type=" + type +
                    ", headerId='" + headerId + '\'' +
                    ", ob=" + ob +
                    ", share=" + share +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", expire=" + expire +
                ", key='" + key + '\'' +
                ", random='" + random + '\'' +
                ", bind=" + bind +
                ", invite='" + invite + '\'' +
                ", user=" + user +
                '}';
    }
}
