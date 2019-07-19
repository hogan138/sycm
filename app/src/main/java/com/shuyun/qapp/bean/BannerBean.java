package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * Created by sunxiao on 2018/4/25.
 */
@Data
public class BannerBean {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    private String name;//名称
    private String description;//描述
    private String remark;//描述
    private String picture;//主图
    private String url;//跳转的地址
    private Long type;//类型 1、外部链接;2、内部链接;3、内部功能
    private String h5Url; //答题url
    private String action;
    private String content;
    private Long isLogin;

    //任意位置logo配置
    private List<AdConfigs> adConfigs;


    @Override
    public String toString() {
        return "BannerBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                '}';
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
