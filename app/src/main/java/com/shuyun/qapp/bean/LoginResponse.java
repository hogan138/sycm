package com.shuyun.qapp.bean;

import java.io.Serializable;

/**
 * Created by sunxiao on 2018/5/3.
 */

public class LoginResponse implements Serializable{
    private String token;//返回的token
    private Long expire;//token的有效期
    private String key;//对称加密的秘钥。用请求方的私钥加密
    private String random;//接口随机生成的字符串
    private int bind;//是否绑定用户。0:未绑定;1:已绑定
    private String invite;//邀请分享的展现图片地址  当user.share=1时有效
    private User user;//对象

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

    public void setBind(int bind) {
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

    public int getBind() {
        return bind;
    }

    public String getInvite() {
        return invite;
    }

    public User getUser() {
        return user;
    }

    public static class User implements Serializable{
        private String nickname;//用户的昵称
        private int type;//用户类型   1——正式用户 0——内部用户
        private String headerId;//用户的头像
        private int ob;//是否参与公测 1——参与公测
        private int share;//是否参与邀请分享   1——参与邀请

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setHeaderId(String headerId) {
            this.headerId = headerId;
        }

        public void setOb(int ob) {
            this.ob = ob;
        }

        public void setShare(int share) {
            this.share = share;
        }

        public String getNickname() {
            return nickname;
        }

        public int getType() {
            return type;
        }

        public String getHeaderId() {
            return headerId;
        }

        public int getOb() {
            return ob;
        }

        public int getShare() {
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
