package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AnyPositionBean
 * @Description: 任意位置bean
 * @Author: ganquan
 * @CreateDate: 2019/1/7 11:04
 */
@Data
public class AnyPositionBean {

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
    private String margin; //广告四周间距
    private Long shadow;
    private String shadowColor;
    private String shadowAlpha;
    private String shadowRadius;
    private String imageUrl;
    private String action;

}
