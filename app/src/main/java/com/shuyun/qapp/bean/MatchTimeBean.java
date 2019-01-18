package com.shuyun.qapp.bean;


/**
 * 用户匹配时长
 */

public class MatchTimeBean {


    /**
     * user : {"account":"15868421563","nickname":"Gan","obt":0,"header":"http://3","headerId":3,"status":10,"certification":1,"certInfo":"甘* 4113**********4255","phone":"15868421563","gender":0,"cash":20.26,"bp":99044,"opporitunity":9986,"withdraw":0,"availablePrize":61,"upcommings":0,"messages":0,"wxBind":1,"wxHeader":"http://thirdwx.qlogo.cn/mmopen/vi_32/BiblaKjthHdwdPlYf8rFuJXEYddjCKn5JW0icwgAuh8TKVKheIjN09iaSvxQEWdoKjK8QdcuRW8nDR4qrhKhKA8UA/132"}
     * robotPic : 0
     * robotAccount : 155****2971
     * matchTime : 4
     */

    private UserBean user;
    private Integer robotPic;//机器人头像
    private String robotAccount;//机器人账号
    private Integer matchTime;//匹配时间

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Integer getRobotPic() {
        return robotPic;
    }

    public void setRobotPic(Integer robotPic) {
        this.robotPic = robotPic;
    }

    public String getRobotAccount() {
        return robotAccount;
    }

    public void setRobotAccount(String robotAccount) {
        this.robotAccount = robotAccount;
    }

    public Integer getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Integer matchTime) {
        this.matchTime = matchTime;
    }

    public static class UserBean {
        /**
         * account : 15868421563
         * nickname : Gan
         * obt : 0
         * header : http://3
         * headerId : 3
         * status : 10
         * certification : 1
         * certInfo : 甘* 4113**********4255
         * phone : 15868421563
         * gender : 0
         * cash : 20.26
         * bp : 99044
         * opporitunity : 9986
         * withdraw : 0
         * availablePrize : 61
         * upcommings : 0
         * messages : 0
         * wxBind : 1
         * wxHeader : http://thirdwx.qlogo.cn/mmopen/vi_32/BiblaKjthHdwdPlYf8rFuJXEYddjCKn5JW0icwgAuh8TKVKheIjN09iaSvxQEWdoKjK8QdcuRW8nDR4qrhKhKA8UA/132
         */

        private String account;//账号
        private String nickname;
        private Long obt;
        private String header;
        private Integer headerId;//头像id
        private Long status;
        private Integer certification;
        private String certInfo;
        private String phone;
        private Long gender;
        private Double cash;
        private Long bp;//积分
        private Long opporitunity;
        private Long withdraw;
        private Long availablePrize;
        private Long upcommings;
        private Long messages;
        private Long wxBind;
        private String wxHeader;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Long getObt() {
            return obt;
        }

        public void setObt(Long obt) {
            this.obt = obt;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public Integer getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Integer headerId) {
            this.headerId = headerId;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Integer getCertification() {
            return certification;
        }

        public void setCertification(Integer certification) {
            this.certification = certification;
        }

        public String getCertInfo() {
            return certInfo;
        }

        public void setCertInfo(String certInfo) {
            this.certInfo = certInfo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getGender() {
            return gender;
        }

        public void setGender(Long gender) {
            this.gender = gender;
        }

        public Double getCash() {
            return cash;
        }

        public void setCash(Double cash) {
            this.cash = cash;
        }

        public Long getBp() {
            return bp;
        }

        public void setBp(Long bp) {
            this.bp = bp;
        }

        public Long getOpporitunity() {
            return opporitunity;
        }

        public void setOpporitunity(Long opporitunity) {
            this.opporitunity = opporitunity;
        }

        public Long getWithdraw() {
            return withdraw;
        }

        public void setWithdraw(Long withdraw) {
            this.withdraw = withdraw;
        }

        public Long getAvailablePrize() {
            return availablePrize;
        }

        public void setAvailablePrize(Long availablePrize) {
            this.availablePrize = availablePrize;
        }

        public Long getUpcommings() {
            return upcommings;
        }

        public void setUpcommings(Long upcommings) {
            this.upcommings = upcommings;
        }

        public Long getMessages() {
            return messages;
        }

        public void setMessages(Long messages) {
            this.messages = messages;
        }

        public Long getWxBind() {
            return wxBind;
        }

        public void setWxBind(Long wxBind) {
            this.wxBind = wxBind;
        }

        public String getWxHeader() {
            return wxHeader;
        }

        public void setWxHeader(String wxHeader) {
            this.wxHeader = wxHeader;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "account='" + account + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", obt=" + obt +
                    ", header='" + header + '\'' +
                    ", headerId=" + headerId +
                    ", status=" + status +
                    ", certification=" + certification +
                    ", certInfo='" + certInfo + '\'' +
                    ", phone='" + phone + '\'' +
                    ", gender=" + gender +
                    ", cash=" + cash +
                    ", bp=" + bp +
                    ", opporitunity=" + opporitunity +
                    ", withdraw=" + withdraw +
                    ", availablePrize=" + availablePrize +
                    ", upcommings=" + upcommings +
                    ", messages=" + messages +
                    ", wxBind=" + wxBind +
                    ", wxHeader='" + wxHeader + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MatchTimeBean{" +
                "user=" + user +
                ", robotPic=" + robotPic +
                ", robotAccount='" + robotAccount + '\'' +
                ", matchTime=" + matchTime +
                '}';
    }
}
