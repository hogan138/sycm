package com.shuyun.qapp.bean;


/**
 * Created by sunxiao on 2018/7/9.
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
        private int robotPic;//机器人头像
        private String robotAccount;//机器人账号
        private int matchTime;//匹配时间

        public UserBean getUser() {
                return user;
        }

        public void setUser(UserBean user) {
                this.user = user;
        }

        public int getRobotPic() {
                return robotPic;
        }

        public void setRobotPic(int robotPic) {
                this.robotPic = robotPic;
        }

        public String getRobotAccount() {
                return robotAccount;
        }

        public void setRobotAccount(String robotAccount) {
                this.robotAccount = robotAccount;
        }

        public int getMatchTime() {
                return matchTime;
        }

        public void setMatchTime(int matchTime) {
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
                private int obt;
                private String header;
                private int headerId;//头像id
                private int status;
                private int certification;
                private String certInfo;
                private String phone;
                private int gender;
                private double cash;
                private int bp;//积分
                private int opporitunity;
                private int withdraw;
                private int availablePrize;
                private int upcommings;
                private int messages;
                private int wxBind;
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

                public int getObt() {
                        return obt;
                }

                public void setObt(int obt) {
                        this.obt = obt;
                }

                public String getHeader() {
                        return header;
                }

                public void setHeader(String header) {
                        this.header = header;
                }

                public int getHeaderId() {
                        return headerId;
                }

                public void setHeaderId(int headerId) {
                        this.headerId = headerId;
                }

                public int getStatus() {
                        return status;
                }

                public void setStatus(int status) {
                        this.status = status;
                }

                public int getCertification() {
                        return certification;
                }

                public void setCertification(int certification) {
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

                public int getGender() {
                        return gender;
                }

                public void setGender(int gender) {
                        this.gender = gender;
                }

                public double getCash() {
                        return cash;
                }

                public void setCash(double cash) {
                        this.cash = cash;
                }

                public int getBp() {
                        return bp;
                }

                public void setBp(int bp) {
                        this.bp = bp;
                }

                public int getOpporitunity() {
                        return opporitunity;
                }

                public void setOpporitunity(int opporitunity) {
                        this.opporitunity = opporitunity;
                }

                public int getWithdraw() {
                        return withdraw;
                }

                public void setWithdraw(int withdraw) {
                        this.withdraw = withdraw;
                }

                public int getAvailablePrize() {
                        return availablePrize;
                }

                public void setAvailablePrize(int availablePrize) {
                        this.availablePrize = availablePrize;
                }

                public int getUpcommings() {
                        return upcommings;
                }

                public void setUpcommings(int upcommings) {
                        this.upcommings = upcommings;
                }

                public int getMessages() {
                        return messages;
                }

                public void setMessages(int messages) {
                        this.messages = messages;
                }

                public int getWxBind() {
                        return wxBind;
                }

                public void setWxBind(int wxBind) {
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
