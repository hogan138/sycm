package com.shuyun.qapp.bean;

/**
 * Created by sunxiao on 2018/4/25.
 */

public class BannerBean {

    private int id;
    private String name;//名称
    private String description;//描述
    private String remark;//描述
    private String picture;//主图
    private String url;//跳转的地址
    private int type;//类型 1、外部链接;2、内部链接;3、内部功能
    private String h5Url; //答题url

    public String getH5Url() {
        return h5Url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRemark() {
        return remark;
    }

    public String getPicture() {
        return picture;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

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

}
