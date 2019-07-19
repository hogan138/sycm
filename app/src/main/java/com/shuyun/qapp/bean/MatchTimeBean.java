package com.shuyun.qapp.bean;


import lombok.Data;

/**
 * 用户匹配时长
 */
@Data
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

    @Data
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
