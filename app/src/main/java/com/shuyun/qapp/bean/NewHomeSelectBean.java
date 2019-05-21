package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: NewHomeSelectBean
 * @Description: 新版首页精选bean
 * @Author: ganquan
 * @CreateDate: 2019/5/7 14:26
 */
@Data
public class NewHomeSelectBean {


    /**
     * banners : [{"id":16,"name":"冬季穿衣指南","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/djcy-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"624","isLogin":0},{"id":18,"name":"二十四节气养生","picture":"http://192.168.3.157/jiankangleitizu/jieqiyangsheng-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"627","isLogin":0,"adConfigIds":""},{"id":24,"name":"黄山春","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshan-chun-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"6506048129614352384","isLogin":0},{"id":25,"name":"黄山五月份","picture":"http://192.168.3.157/images/hs-5-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"6506048129614352384","isLogin":0},{"id":19,"name":"食品安全","picture":"http://192.168.3.157/images/shipinanquan-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"638","isLogin":0,"adConfigIds":""},{"id":20,"name":"武侠小说","picture":"http://192.168.3.157/images/wuxiaxiaoshuo-banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5/index.html","action":"action.group","content":"640","isLogin":0,"adConfigIds":""},{"id":21,"name":"提现公告","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/withdraw_notice_banner2.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5_static/withdraw_notice.html","action":"action.h5","isLogin":0},{"id":26,"name":"五一提现延迟公告","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/withdraw/banner.jpg","description":"秋季适合吃什么？","h5Url":"http://192.168.3.157/web/h5_static/withdraw_notice.html","action":"action.h5","content":"","isLogin":0}]
     * recommends : [{"id":84,"name":"书法艺术","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/7/82/84/shufa.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":[],"adConfigIds":"5"},{"id":70,"name":"驾考科目1","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/kemu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":["不消耗答题次数"]},{"id":92,"name":"西湖风景区","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/8/89/92/xihu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":[],"adConfigIds":"7,8,9"},{"id":77,"name":"黄山旅游专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/71/hsgroupmain.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":["不消耗答题次数","有答题攻略","赢黄山门票"]},{"id":"6506048129614352384","name":"黄山旅游专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshang.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":["不消耗答题次数","有答题攻略","赢黄山门票"],"adConfigIds":"7,8,9"},{"id":91,"name":"故宫博物院","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/8/88/gugong.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":true,"tags":[],"adConfigIds":"7,8,9"}]
     * region : {"datas":[{"icon":"http://192.168.3.157/images/answer1.png","action":"action.answer.against","title":"答题对战","remark":"共计送出23552个邀请宝箱","template":"4","row":"2","column":"1","rowspan":"0","colspan":"0","height":75,"isLogin":1,"h5Url":"http://192.168.3.157/web/h5/index.html","content":"6506048129614352384"},{"icon":"http://192.168.3.157/images/hs-5-home.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","template":"4","row":"1","column":"1","rowspan":"0","colspan":"1","height":60,"content":"6506048129614352384","isLogin":1},{"icon":"http://192.168.3.157/images/game2/335*150.png","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsGameTwo/index.html?gameId=2","template":"4","row":"2","column":"2","rowspan":"0","colspan":"0","height":75,"content":"","isLogin":0}],"column":2,"row":2,"height":145}
     * oftens : [{"id":32,"name":"成语知识","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"accuracy":100},{"id":61,"name":"夏季养生","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/4/20/xiajigroup.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"accuracy":66.67}]
     * newTopics : [{"id":"6516534576297414656","name":"图片题组","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/minjiangongyi.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6516124494170558464","name":"民间工艺","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/minjiangongyi.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6513588101145825280","name":"《诗经》专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/shijing.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6511853562161664000","name":"我的祖国我的城 我的地铁我的站","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/hzdaily.jpg","h5Url":"http://192.168.3.157/web/h5_hzdaily/index.html","recommend":false},{"id":"6511054949043015680","name":"中国古建筑","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/gujianzhu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6516534576297414657","name":"黄山限时挑战赛2","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/chunjiyangsheng.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6506048129614352384","name":"黄山旅游专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshang.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6505656503649832960","name":"春季养生","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/chunjiyangsheng.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6503084490330607616","name":"元宵节专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/yuanxiao.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":"6494464706986774528","name":"春节专题","picture":"http://192.168.3.157/images/chunjie.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false}]
     * broadcasts : [{"message":"恭喜 158****5737用户获得2.90元红包"},{"message":"恭喜 188****8935用户获得8.77元红包"},{"message":"恭喜 158****1563用户获得3.90元红包"},{"message":"恭喜 188****8935用户获得6.89元红包"},{"message":"恭喜 158****1563用户获得4.12元红包"},{"message":"恭喜 158****5737用户获得7.84元红包"},{"message":"恭喜 136****2244用户获得5.05元现金"},{"message":"恭喜 158****1563用户获得3.90元红包"},{"message":"恭喜 188****8935用户获得11.93元红包"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 155****1816用户获得5.90元红包"},{"message":"恭喜 158****0878用户获得6.90元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 158****1563用户获得4.12元红包"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 136****2241用户获得0.55元现金"},{"message":"恭喜 136****2241用户获得1.90元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 136****2241用户获得1.90元红包"},{"message":"恭喜 158****0878用户获得13.69元红包"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 136****2241用户获得10.00元现金"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 158****1563用户获得50.00元现金"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 155****1816用户获得9.12元红包"},{"message":"恭喜 158****1563用户获得5.89元红包"},{"message":"恭喜 188****8935用户获得5.90元现金"},{"message":"恭喜 155****1816用户获得10.00元红包"},{"message":"恭喜 188****8935用户获得6.89元红包"},{"message":"恭喜 151****5810用户获得5.90元红包"},{"message":"恭喜 158****1563用户获得7.72元红包"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 188****8935用户获得5.64元红包"},{"message":"恭喜 158****1563用户获得4.98元红包"},{"message":"恭喜 188****8935用户获得8.11元红包"},{"message":"恭喜 136****2241用户获得7.13元红包"},{"message":"恭喜 158****1563用户获得3.89元红包"},{"message":"恭喜 136****2245用户获得21.85元现金"},{"message":"恭喜 136****2241用户获得10.00元现金"},{"message":"恭喜 158****1563用户获得4.90元红包"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 158****1563用户获得1.27元红包"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 158****1563用户获得5.89元红包"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 155****1816用户获得5.90元红包"},{"message":"恭喜 188****8935用户获得11.93元红包"},{"message":"恭喜 158****1563用户获得4.90元红包"},{"message":"恭喜 188****8935用户获得2.12元红包"},{"message":"恭喜 136****2241用户获得0.55元现金"},{"message":"恭喜 136****2241用户获得1.90元红包"},{"message":"恭喜 158****1563用户获得5.89元红包"},{"message":"恭喜 188****8935用户获得9.78元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 158****1563用户获得3.89元红包"},{"message":"恭喜 136****2244用户获得40.00元现金"},{"message":"恭喜 136****2241用户获得3.84元红包"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 158****1563用户获得13.89元红包"},{"message":"恭喜 136****2241用户获得1.58元红包"},{"message":"恭喜 158****0878用户获得13.69元红包"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 158****5737用户获得6.90元红包"},{"message":"恭喜 158****0878用户获得13.69元红包"},{"message":"恭喜 188****8935用户获得8.90元红包"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 136****2241用户获得0.32元红包"},{"message":"恭喜 158****0878用户获得6.90元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 188****8935用户获得9.78元红包"},{"message":"恭喜 136****2245用户获得21.85元现金"},{"message":"恭喜 136****2241用户获得0.38元红包"},{"message":"恭喜 188****8935用户获得2.11元红包"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 155****1816用户获得5.11元红包"},{"message":"恭喜 136****2241用户获得10.00元现金"},{"message":"恭喜 155****1816用户获得7.90元现金"},{"message":"恭喜 136****2241用户获得0.55元现金"},{"message":"恭喜 158****1563用户获得4.98元红包"},{"message":"恭喜 158****0878用户获得6.90元红包"},{"message":"恭喜 136****2245用户获得40.00元现金"},{"message":"恭喜 136****2245用户获得21.85元现金"},{"message":"恭喜 136****2241用户获得0.55元现金"},{"message":"恭喜 158****1563用户获得50.00元现金"},{"message":"恭喜 151****5810用户获得41.85元现金"},{"message":"恭喜 155****1816用户获得6.90元红包"},{"message":"恭喜 155****1816用户获得7.90元红包"},{"message":"恭喜 151****5810用户获得50.00元现金"},{"message":"恭喜 136****2241用户获得0.55元现金"},{"message":"恭喜 155****1816用户获得2.12元红包"},{"message":"恭喜 158****1563用户获得4.98元红包"},{"message":"恭喜 136****2241用户获得0.10元现金"},{"message":"恭喜 136****2245用户获得2.90元红包"},{"message":"恭喜 188****8935用户获得8.77元红包"}]
     * guess : [{"id":"6506048129614352384","name":"黄山旅游专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshang.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"adConfigIds":"7,8,9"},{"id":640,"name":"经典武侠小说","picture":"http://192.168.3.157/images/wuxiaxiaoshuo.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"adConfigIds":"3"},{"id":32,"name":"成语知识","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"adConfigIds":"2,5"},{"id":78,"name":"乒乓球王国","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/5/21/78/pinpangqiu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false},{"id":84,"name":"书法艺术","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/7/82/84/shufa.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false,"adConfigIds":"5"},{"id":75,"name":"一带一路","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/yuny/yidaiyilu.jpg","h5Url":"http://192.168.3.157/web/h5/index.html","recommend":false}]
     */

    public RegionBean region;
    private List<BannersBean> banners;
    private List<RecommendsBean> recommends;
    private List<OftensBean> oftens;
    private List<NewTopicsBean> newTopics;
    private List<BroadcastsBean> broadcasts;
    private List<GuessBean> guess;

    @Data
    public static class RegionBean {
        /**
         * datas : [{"icon":"http://192.168.3.157/images/answer1.png","action":"action.answer.against","title":"答题对战","remark":"共计送出23552个邀请宝箱","template":"4","row":"2","column":"1","rowspan":"0","colspan":"0","height":75,"isLogin":1},{"icon":"http://192.168.3.157/images/hs-5-home.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","template":"4","row":"1","column":"1","rowspan":"0","colspan":"1","height":60,"content":"6506048129614352384","isLogin":1},{"icon":"http://192.168.3.157/images/game2/335*150.png","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsGameTwo/index.html?gameId=2","template":"4","row":"2","column":"2","rowspan":"0","colspan":"0","height":75,"content":"","isLogin":0}]
         * column : 2
         * row : 2
         * height : 145
         */

        private String column;
        private String row;
        private String height;
        private List<DatasBean> datas;

        @Data
        public static class DatasBean {
            /**
             * icon : http://192.168.3.157/images/answer1.png
             * action : action.answer.against
             * title : 答题对战
             * remark : 共计送出23552个邀请宝箱
             * template : 4
             * row : 2
             * column : 1
             * rowspan : 0
             * colspan : 0
             * height : 75
             * isLogin : 1
             * h5Url : http://192.168.3.157/web/h5/index.html
             * content : 6506048129614352384
             */

            private String icon;
            private String action;
            private String title;
            private String remark;
            private String template;
            private String row;
            private String column;
            private String rowspan;
            private String colspan;
            private String height;
            private Long isLogin;
            private String h5Url;
            private String content;
        }
    }

    @Data
    public static class BannersBean {
        /**
         * id : 16
         * name : 冬季穿衣指南
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/djcy-banner.jpg
         * description : 秋季适合吃什么？
         * h5Url : http://192.168.3.157/web/h5/index.html
         * action : action.group
         * content : 624
         * isLogin : 0
         * adConfigIds :
         */

        private Long id;
        private String name;
        private String picture;
        private String description;
        private String h5Url;
        private String action;
        private String content;
        private Long isLogin;
        private String adConfigIds;

    }

    @Data
    public static class RecommendsBean {
        /**
         * id : 84
         * name : 书法艺术
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/7/82/84/shufa.jpg
         * h5Url : http://192.168.3.157/web/h5/index.html
         * recommend : true
         * tags : []
         * adConfigIds : 5
         */

        private Long id;
        private String name;
        private String picture;
        private String h5Url;
        private boolean recommend;
        private String adConfigIds;
        private List<String> tags;
        private String button;

    }


    @Data
    public static class OftensBean {
        /**
         * id : 32
         * name : 成语知识
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg
         * h5Url : http://192.168.3.157/web/h5/index.html
         * recommend : false
         * accuracy : 100
         */

        private Long id;
        private String name;
        private String picture;
        private String h5Url;
        private boolean recommend;
        private Double accuracy;


    }

    @Data
    public static class NewTopicsBean {
        /**
         * id : 6516534576297414656
         * name : 图片题组
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/minjiangongyi.jpg
         * h5Url : http://192.168.3.157/web/h5/index.html
         * recommend : false
         */

        private String id;
        private String name;
        private String picture;
        private String h5Url;
        private boolean recommend;

    }

    @Data
    public static class BroadcastsBean {
        /**
         * message : 恭喜 158****5737用户获得2.90元红包
         */

        private String message;

    }

    @Data
    public static class GuessBean {
        /**
         * id : 6506048129614352384
         * name : 黄山旅游专题
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshang.jpg
         * h5Url : http://192.168.3.157/web/h5/index.html
         * recommend : false
         * adConfigIds : 7,8,9
         */

        private String id;
        private String name;
        private String picture;
        private String h5Url;
        private boolean recommend;
    }
}
