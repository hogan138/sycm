package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * 推荐题组 大家都在答题组
 */
@Data
public class GroupBean {
    /**
     * id : 611
     * name : 南孔圣地 衢州有礼
     * parentId : 101
     * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/610/quzhou.png
     * opportunity : 0
     * guideId : 2
     * merchantName : 衢州广电传媒集团
     * sorting : 0
     * h5Url : http://192.168.3.137:8080/h5/index.html
     * tags : [{"groupId":611,"remark":"10%","tagName":"获得积分"},{"groupId":611,"remark":"60%","tagName":"获得现金"},{"groupId":611,"remark":"80%","tagName":"准确率"}]
     * opportunityLabel : 不消耗答题次数
     */

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//题组id
    private String name;//题组名称
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;//上级题组
    private String picture;//题组主图
    private Long opportunity;//消耗答题机会次数
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long guideId;//指南id
    private String merchantName;//题组参与的活动商户名称
    private Long sorting;
    private String h5Url; //答题url
    private String opportunityLabel; //不消耗答题次数
    private String tag; //答题攻略
    private List<TagsBean> tags;  //积分、现金、准确率

    //任意位置logo配置
    private List<AdConfigs> adConfigs;

    //是否推荐
    private boolean recommend;
    private String remark;


    @Data
    public static class TagsBean {
        /**
         * groupId : 611
         * remark : 10%
         * tagName : 获得积分
         */

        private Long groupId;
        private String remark;
        private String tagName;


    }
    @Data
    public static class AdConfigs {

        /**
         * type : 1
         * location : 8
         * width : 150
         * height : 39
         * padding : 8,8,8,8
         * shadow : 1
         * shadowColor : #000000
         * shadowAlpha : 0.2
         * shadowRadius : 8,8,0,0
         * imageUrl : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/pingan/pahys_logo2.png
         */

        private Long type;
        private Long location;
        private Long width;
        private Long height;
        private String padding;
        private String margin;
        private Long shadow;
        private String shadowColor;
        private String shadowAlpha;
        private String shadowRadius;
        private String imageUrl;

    }

}
