package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: GoodsDetailBeans
 * @Description: 商品详情
 * @Author: ganquan
 * @CreateDate: 2019/3/28 16:56
 */
@Data
public class GoodsDetailBeans {


    /**
     * shortName : 细雪青花瓷
     * price : 200000积分
     * name : 细雪青花瓷8/12件套参装
     * picture : http://192.168.3.157/images/xixueqinghuaci-main.png
     * pictures : ["http://192.168.3.157/images/xixueqinghuaci.png","http://192.168.3.157/images/xixueqinghuaci.png","http://192.168.3.157/images/xixueqinghuaci.png","http://192.168.3.157/images/xixueqinghuaci.png"]
     * videos : [{"value":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4","title":"办公室小野开番外了，居然在办公室开澡堂！老板还点赞？","picture":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg"},{"value":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4","title":"办公室小野开番外了，居然在办公室开澡堂！老板还点赞？","picture":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg"},{"value":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4","title":"办公室小野开番外了，居然在办公室开澡堂！老板还点赞？","picture":"http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg"}]
     * standards : [{"label":"规格","param":"size","stas":[{"value":"M","label":"M"},{"value":"L","label":"L"},{"value":"S","label":"S"}]},{"label":"颜色分类","param":"color","stas":[{"value":"1","label":"灰色","color":"#333333"},{"value":"2","label":"黑色","color":"#000000"},{"value":"3","label":"白色","color":"#FFFFFF"}]}]
     * purpose : 法国陶瓷与中国青花瓷，散落一桌清冽
     * detail : <img src="http://192.168.3.157/images/xixueqinghuaci-img3.png"/><br/><img src="http://192.168.3.157/images/xixueqinghuaci-img3.png"/><br/><img src="http://192.168.3.157/images/xixueqinghuaci-img3.png"/><br/><img src="http://192.168.3.157/images/xixueqinghuaci-img3.png"/>
     * remark : <span style="font-size: 30px;color: #333333;letter-spacing: 0;text-align: justify;">注意事项</span><br/><span style="font-size: 28px;color: #666666;letter-spacing: 0;text-align: justify;">1.兑换此券即可0元购买该商品。</span><br/><span style="font-size: 28px;color: #666666;letter-spacing: 0;text-align: justify;">2.兑换后不可退换积分。</span><br/><span style="font-size: 28px;color: #666666;letter-spacing: 0;text-align: justify;">3.使用前确认收货地址与电话的正确。</span>
     * bp : 200000
     * inventory : 10000
     * mode : 1
     */

    private String shortName;
    private String price;
    private String name;
    private String picture;
    private String purpose;
    private String detail;
    private String remark;
    private Long bp;
    private Long inventory;
    private Long mode;
    private List<String> pictures;
    private List<VideosBean> videos;
    private List<StandardsBean> standards;


    @Data
    public static class VideosBean {
        /**
         * value : http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4
         * title : 办公室小野开番外了，居然在办公室开澡堂！老板还点赞？
         * picture : http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg
         */

        private String value;
        private String title;
        private String picture;


    }
    @Data
    public static class StandardsBean {
        /**
         * label : 规格
         * param : size
         * stas : [{"value":"M","label":"M"},{"value":"L","label":"L"},{"value":"S","label":"S"}]
         */

        private String label;
        private String param;
        private List<StasBean> stas;


        @Data
        public static class StasBean {
            /**
             * value : M
             * label : M
             */
            private String value;
            private String label;
            private String color;
            private boolean selected = false;


        }
    }
}
