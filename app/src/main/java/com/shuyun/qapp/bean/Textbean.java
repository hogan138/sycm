package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：gq
 * 创建日期：2018/6/13 15:31
 */
public class Textbean {

    /**
     * id : 14
     * name : 宝箱
     * type : 101
     * expireTime : 1525497794400
     * validTime : 0
     * picture : http://1
     * mode : 0
     * status : 1
     * upcomming : 0
     */

    private String id;//奖品id
    /**
     * 奖品类型1:现金;2:知识币;3:实物;4:电子卷;5:道具;6:票务;7:红包;101:宝箱
     */
    private int type;
    /**
     * 奖品状态1:正常;2:已用;3:过期;
     */
    private int status;
    private String validTime;//奖品有效开始时间
    private String expireTime;//奖品有效结束时间
    private String name;//奖品名称
    private String description;//奖品描述
    private int mode;//使用方式
    private String picture;//奖品图片地址
    private String openPicture;//开奖使用的图片
    private int upcomming;//是否快过期
    /**
     * 如果是红包,则有金额;
     */
    private String amount;
    /**
     * 把获取的规则的公告内容冗余到本字段
     */
    private String bulletin;
    /**
     * 奖品的特定内容，对于h5、富文本的方式，为h5的地址
     */
    private String content;

    public boolean selected = false;

    public Textbean(){

    }

    protected Textbean(Parcel in) {
        id = in.readString();
        type = in.readInt();
        status = in.readInt();
        validTime = in.readString();
        expireTime = in.readString();
        name = in.readString();
        description = in.readString();
        mode = in.readInt();
        picture = in.readString();
        openPicture = in.readString();
        upcomming = in.readInt();
        amount = in.readString();
        bulletin = in.readString();
        content = in.readString();
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getValidTime() {
        return validTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMode() {
        return mode;
    }

    public String getPicture() {
        return picture;
    }

    public String getOpenPicture() {
        return openPicture;
    }

    public int getUpcomming() {
        return upcomming;
    }

    public String getAmount() {
        return amount;
    }

    public String getBulletin() {
        return bulletin;
    }

    public String getContent() {
        return content;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public String toString() {
        return "MinePrize{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", validTime='" + validTime + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mode=" + mode +
                ", picture='" + picture + '\'' +
                ", openPicture='" + openPicture + '\'' +
                ", upcomming=" + upcomming +
                ", amount='" + amount + '\'' +
                ", bulletin='" + bulletin + '\'' +
                ", content='" + content + '\'' +
                ", selected=" + selected +
                '}';
    }

}
