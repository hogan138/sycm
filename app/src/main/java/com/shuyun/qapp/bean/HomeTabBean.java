package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeTabBean
 * @Description: homeTab
 * @Author: ganquan
 * @CreateDate: 2019/3/13 15:53
 */
@Data
public class HomeTabBean {


    /**
     * title : 建国70周年
     * picture : http://192.168.3.157:/icon/seventy_year.png
     * width : 68
     * height : 32
     * action : action.h5
     * color : #FFB500
     * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
     * mode : 1
     * status : 1
     */

    private String title;
    private String picture;
    private int width;
    private int height;
    private String action;
    private String color;
    private String h5Url;
    private Long mode;
    private Long status;
    private String content;



}
