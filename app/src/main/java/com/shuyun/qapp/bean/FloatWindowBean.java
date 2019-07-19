package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: FloatWindowBean
 * @Description: 浮窗数据
 * @Author: ganquan
 * @CreateDate: 2019/3/18 13:22
 */
@Data
public class FloatWindowBean {


    /**
     * picture : http://192.168.3.157:/icon/float_window.png
     * width : 46
     * height : 51
     * padding : 0,0,0,0
     * margin : 3,3,3,3
     * shadow : 0
     * action : action.h5
     * color :
     * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
     * status : 1
     */

    private String picture;
    private Long width;
    private Long height;
    private String padding;
    private String margin;
    private Long shadow;
    private String action;
    private String color;
    private String h5Url;
    private Long status;
    private String content;
    private String shadowColor;
    private String shadowAlpha;
    private String shadowRadius;


}
