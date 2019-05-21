package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeTabContentBean
 * @Description: 首页tab内容
 * @Author: ganquan
 * @CreateDate: 2019/5/13 10:11
 */
@Data
public class HomeTabContentBean {


    private List<ContentsBean> contents;

    @Data
    public static class ContentsBean {
        /**
         * title : 专题
         * head : {"picture":"http://192.168.3.157/images/homeData/picture.jpg","action":"action.default","height":210}
         * data : {"rowNum":2,"datas":[{"picture":"http://192.168.3.157/images/chunjie.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"6494464706986774528","title":"春节专题","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/hzdaily.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5_hzdaily/index.html","content":"6511853562161664000","title":"我的祖国我的城 我的地铁我的站","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/yuanxiao.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"6503084490330607616","title":"元宵节专题","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/yuny/yidaiyilu.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"75","title":"一带一路","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/jungeliaoliang.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"76","title":"军歌嘹亮","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/yibao.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"621","title":"医保小知识","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/junqi.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"610","title":"军旗飘扬","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/chongyangjie.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"617","title":"重阳节专题","button":"开始答题"}],"scale":1.37}
         */

        private String title;
        private HeadBean head;
        private DataBean data;

        @Data
        public static class HeadBean {
            /**
             * picture : http://192.168.3.157/images/homeData/picture.jpg
             * action : action.default
             * height : 210
             */

            private String picture;
            private String action;
            private Long height;
            private String h5Url;
            private String content;

        }

        @Data
        public static class DataBean {
            /**
             * rowNum : 2
             * datas : [{"picture":"http://192.168.3.157/images/chunjie.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"6494464706986774528","title":"春节专题","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/hzdaily.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5_hzdaily/index.html","content":"6511853562161664000","title":"我的祖国我的城 我的地铁我的站","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/yuanxiao.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"6503084490330607616","title":"元宵节专题","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/yuny/yidaiyilu.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"75","title":"一带一路","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/jungeliaoliang.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"76","title":"军歌嘹亮","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/yibao.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"621","title":"医保小知识","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/junqi.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"610","title":"军旗飘扬","button":"开始答题"},{"picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/20181023/chongyangjie.jpg","action":"action.group","h5Url":"http://192.168.3.157/web/h5/index.html","content":"617","title":"重阳节专题","button":"开始答题"}]
             * scale : 1.37
             */

            private int rowNum;
            private Double scale;
            private List<DatasBean> datas;

            @Data
            public static class DatasBean {
                /**
                 * picture : http://192.168.3.157/images/chunjie.jpg
                 * action : action.group
                 * h5Url : http://192.168.3.157/web/h5/index.html
                 * content : 6494464706986774528
                 * title : 春节专题
                 * button : 开始答题
                 */

                private String picture;
                private String action;
                private String h5Url;
                private String content;
                private String title;
                private String button;

            }
        }
    }
}
