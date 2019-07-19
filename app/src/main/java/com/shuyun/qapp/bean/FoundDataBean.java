package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: FoundDataBean
 * @Description: 发现页面数据
 * @Author: ganquan
 * @CreateDate: 2019/3/13 16:26
 */
@Data
public class FoundDataBean {


    /**
     * banner : [{"h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","isLogin":0,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-activity-zone.jpg","id":9,"btnAction":"action.h5","content":""},{"h5Url":"http://192.168.3.157/web/h5/index.html","isLogin":0,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/zone/huangshan.jpg","id":8,"btnAction":"action.group","content":"77"},{"h5Url":"http://192.168.3.157/web/h5/index.html","isLogin":1,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/huangshan/huangshan-chun.jpg","id":10,"btnAction":"action.group","content":"6506048129614352384"},{"h5Url":"http://192.168.3.157/web/h5_invitation/index.html","isLogin":0,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/zone/yjhy.jpg","id":6,"btnAction":"action.invite"},{"h5Url":"http://192.168.3.157/web/h5/index.html","isLogin":0,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/zone/quzhou.jpg","id":7,"btnAction":"action.group","content":"611"},{"h5Url":"http://192.168.3.157/web/h5/static.html#/prizerule","isLogin":1,"stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/zone/jfdb.jpg","id":5,"btnAction":"action.integral.treasure","content":""},{"h5Url":"http://192.168.3.157/web/h5_challenge/index.html","isLogin":1,"stop":true,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/zone/tzs.jpg","id":4,"btnAction":"action.h5","content":""}]
     * region : {"datas":[{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"2","rowspan":"0","colspan":"0","height":80,"isLogin":1},{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"1","rowspan":"0","colspan":"0","height":80,"isLogin":1},{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"3","rowspan":"0","colspan":"0","height":80,"isLogin":1}],"column":3,"row":1,"height":80}
     * tables : [{"groups":[{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"经典猜谜语","id":35,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/cai.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"认识生僻字","id":36,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/shengpizi717.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《红楼梦》专题","id":37,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/37/6e4e516532514471aafdbfd9a41f0e71.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《西游记》专题","id":38,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/38/0571724ac04d465c86ebde972801cd6e.jpg"}],"title":"高分榜"},{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"端午节专题","id":27,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/27/b990a806f0b74162bda33761bc609cfe.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"非物质文化遗产","id":28,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/28/c6ebfe8d25d944cab8f2e73f734ccccb.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"二十四节气","id":29,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/29/e9e36f0a68be4cbc944fac1095e494dc.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"中国神话传说","id":30,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/30/11ef3f07749e41b4abf418e4f0b40d31.jpg"}],"title":"热答榜"},{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"京剧知识","id":31,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/jingju.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"成语知识","id":32,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"常用歇后语","id":33,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/12/33/74b2b24927df442fa52df6d96d7a7ef1.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"寓言故事","id":34,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/yuyan.jpg"}],"title":"新题榜"}],"title":"热门题组","type":2},{"h5Url":"http://www.baidu.com","title":"排行榜","type":1},{"h5Url":"http://www.baidu.com","title":"黄山挑战赛","type":1}]
     */

    private RegionBean region;
    private List<BannerBean> banner;
    private List<TablesBean> tables;


    @Data
    public static class RegionBean {
        /**
         * datas : [{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"2","rowspan":"0","colspan":"0","height":80,"isLogin":1},{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"1","rowspan":"0","colspan":"0","height":80,"isLogin":1},{"icon":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg","action":"action.h5","h5Url":"http://192.168.3.157/web/h5_hsChallenge/index.html","template":"4","row":"1","column":"3","rowspan":"0","colspan":"0","height":80,"isLogin":1}]
         * column : 3
         * row : 1
         * height : 80
         */

        private int column;
        private int row;
        private int height;
        private List<DatasBean> datas;


        @Data
        public static class DatasBean {
            /**
             * icon : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-home.jpg
             * action : action.h5
             * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
             * template : 4
             * row : 1
             * column : 2
             * rowspan : 0
             * colspan : 0
             * height : 80
             * isLogin : 1
             */

            private String template;
            private String colspan;
            private String h5Url;
            private String rowspan;
            private String icon;
            private String count;
            private String column;
            private String action;
            private String remark;
            private String row;
            private String title;
            private String height;
            private String content;
            private Long isLogin;


        }
    }

    @Data
    public static class BannerBean {

        /**
         * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
         * isLogin : 0
         * stop : false
         * baseImage : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-activity-zone.jpg
         * action : action.h5
         * id : 9
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/activity/huangshan/hstzs-activity-zone.jpg
         * btnAction : action.h5
         * content :
         */

        private String h5Url;
        private Long isLogin;
        private boolean stop;
        private String baseImage;
        private String action;
        private Long id;
        private String picture;
        private String btnAction;
        private String content;


    }

    @Data
    public static class TablesBean {
        /**
         * groups : [{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"经典猜谜语","id":35,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/cai.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"认识生僻字","id":36,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/shengpizi717.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《红楼梦》专题","id":37,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/37/6e4e516532514471aafdbfd9a41f0e71.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《西游记》专题","id":38,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/38/0571724ac04d465c86ebde972801cd6e.jpg"}],"title":"高分榜"},{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"端午节专题","id":27,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/27/b990a806f0b74162bda33761bc609cfe.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"非物质文化遗产","id":28,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/28/c6ebfe8d25d944cab8f2e73f734ccccb.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"二十四节气","id":29,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/29/e9e36f0a68be4cbc944fac1095e494dc.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"中国神话传说","id":30,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/30/11ef3f07749e41b4abf418e4f0b40d31.jpg"}],"title":"热答榜"},{"children":[{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"京剧知识","id":31,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/jingju.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"成语知识","id":32,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"常用歇后语","id":33,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/12/33/74b2b24927df442fa52df6d96d7a7ef1.png"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"寓言故事","id":34,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/yuyan.jpg"}],"title":"新题榜"}]
         * title : 热门题组
         * type : 2
         * h5Url : http://www.baidu.com
         */

        private String title;
        private Long type;
        private String h5Url;
        private List<GroupsBean> groups;

        @Data
        public static class GroupsBean {
            /**
             * children : [{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"经典猜谜语","id":35,"tag":1,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/cai.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"认识生僻字","id":36,"tag":2,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/shengpizi717.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《红楼梦》专题","id":37,"tag":3,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/37/6e4e516532514471aafdbfd9a41f0e71.jpg"},{"h5Url":"http://192.168.3.157/web/h5/index.html","name":"《西游记》专题","id":38,"tag":4,"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/2/13/38/0571724ac04d465c86ebde972801cd6e.jpg"}]
             * title : 高分榜
             */

            private String title;
            private List<ChildrenBean> children;


            @Data
            public static class ChildrenBean {
                /**
                 * h5Url : http://192.168.3.157/web/h5/index.html
                 * name : 经典猜谜语
                 * id : 35
                 * tag : 1
                 * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/cai.jpg
                 */

                private String h5Url;
                private String name;
                private Long id;
                private Long tag;
                private String picture;

            }
        }
    }
}
