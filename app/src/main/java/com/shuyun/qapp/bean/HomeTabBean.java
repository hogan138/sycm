package com.shuyun.qapp.bean;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: HomeTabBean
 * @Description: homeTab
 * @Author: ganquan
 * @CreateDate: 2019/3/13 15:53
 */
public class HomeTabBean {


    /**
     * label : 建国70周年
     * color : #FFB500
     * h5Url : http://192.168.3.157/web/h5_hsChallenge/index.html
     * icon : http://192.168.3.157:/icon/seventy_year.png
     * status : 1
     */

    private String label;
    private String color;
    private String h5Url;
    private String icon;
    private Long status;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
