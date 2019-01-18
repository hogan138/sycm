package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;

/**
 * 登录返回
 */

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

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getWxHeadUrl() {
        return wxHeadUrl;
    }

    public void setWxHeadUrl(String wxHeadUrl) {
        this.wxHeadUrl = wxHeadUrl;
    }

    public Boolean isSetPwd() {
        return setPwd;
    }

    public void setSetPwd(Boolean setPwd) {
        this.setPwd = setPwd;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public void setBind(Integer bind) {
        this.bind = bind;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Long getExpire() {
        return expire;
    }

    public String getKey() {
        return key;
    }

    public String getRandom() {
        return random;
    }

    public Integer getBind() {
        return bind;
    }

    public String getInvite() {
        return invite;
    }

    public User getUser() {
        return user;
    }

    public static class User implements Serializable {
        private String nickname;//用户的昵称
        private Integer type;//用户类型   1——正式用户 0——内部用户
        private String headerId;//用户的头像
        private Integer ob;//是否参与公测 1——参与公测
        private Integer share;//是否参与邀请分享   1——参与邀请
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public void setHeaderId(String headerId) {
            this.headerId = headerId;
        }

        public void setOb(Integer ob) {
            this.ob = ob;
        }

        public void setShare(Integer share) {
            this.share = share;
        }

        public String getNickname() {
            return nickname;
        }

        public Integer getType() {
            return type;
        }

        public String getHeaderId() {
            return headerId;
        }

        public Integer getOb() {
            return ob;
        }

        public Integer getShare() {
            return share;
        }

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
