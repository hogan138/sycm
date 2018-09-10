package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sunxiao on 2018/6/28.
 */


public class BoxBean implements Parcelable {
    private int count = 0; // 宝箱数量
    private int source = 0; // 宝箱来源，仅当count=1时有效
    private List<PrizeInfo> prizes;
    private String id; // 宝箱id，当source=3有效
    private String bulletin; // 奖品的规则公告，仅针对宝箱有效
    private double beatRate; // 击败率
    private double accuracy; // 正确率
    private long prizeId; // 对应的奖品id
    private Long ruleId; // 得奖时的规则id
    private long expireTime; // 有效期-结束日期
    private long validTime; // 有效期-开始日期
    private short status; // 状态：0——待打开（容器:需要打开；单品：需要打开才能拿到）1——正常；2——已用；3——过期；4——使用中
    private short substatus = 0; // 子状态，根据不同的奖品有不同的定义
    private String substatusName; // 子状态显示名称
    private short upcomming = 0; // 是否快过期，1表示快过期了
    private double amount; // 奖品价值
    private String h5Url;

    protected BoxBean(Parcel in) {
        count = in.readInt();
        source = in.readInt();
        prizes = in.createTypedArrayList(PrizeInfo.CREATOR);
        id = in.readString();
        bulletin = in.readString();
        beatRate = in.readDouble();
        accuracy = in.readDouble();
        prizeId = in.readLong();
        if (in.readByte() == 0) {
            ruleId = null;
        } else {
            ruleId = in.readLong();
        }
        expireTime = in.readLong();
        validTime = in.readLong();
        status = (short) in.readInt();
        substatus = (short) in.readInt();
        substatusName = in.readString();
        upcomming = (short) in.readInt();
        amount = in.readDouble();
        beat = in.readLong();
        h5Url = in.readString();
    }

    public static final Creator<BoxBean> CREATOR = new Creator<BoxBean>() {
        @Override
        public BoxBean createFromParcel(Parcel in) {
            return new BoxBean(in);
        }

        @Override
        public BoxBean[] newArray(int size) {
            return new BoxBean[size];
        }
    };

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public List<PrizeInfo> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrizeInfo> prizes) {
        this.prizes = prizes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public double getBeatRate() {
        return beatRate;
    }

    public void setBeatRate(double beatRate) {
        this.beatRate = beatRate;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(long prizeId) {
        this.prizeId = prizeId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getSubstatus() {
        return substatus;
    }

    public void setSubstatus(short substatus) {
        this.substatus = substatus;
    }

    public String getSubstatusName() {
        return substatusName;
    }

    public void setSubstatusName(String substatusName) {
        this.substatusName = substatusName;
    }

    public short getUpcomming() {
        return upcomming;
    }

    public void setUpcomming(short upcomming) {
        this.upcomming = upcomming;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getBeat() {
        return beat;
    }

    public void setBeat(long beat) {
        this.beat = beat;
    }

    private long beat = 0L; // 得到宝箱时击败的人次

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(source);
        dest.writeTypedList(prizes);
        dest.writeString(id);
        dest.writeString(bulletin);
        dest.writeDouble(beatRate);
        dest.writeDouble(accuracy);
        dest.writeLong(prizeId);
        if (ruleId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ruleId);
        }
        dest.writeLong(expireTime);
        dest.writeLong(validTime);
        dest.writeInt((int) status);
        dest.writeInt((int) substatus);
        dest.writeString(substatusName);
        dest.writeInt((int) upcomming);
        dest.writeDouble(amount);
        dest.writeLong(beat);
        dest.writeString(h5Url);
    }


    public static class PrizeInfo implements Parcelable {
        private String name; // 奖品名称
        private short type; // 奖品类型
        private String description; // 奖品描述
        private short mode; // 使用方式
        private String purpose; // 奖品的使用方式
        private String content; // 奖品内容，对于h5的，为h5的地址
        private String picture; // 奖品图片
        private String openPicture; // 开奖的图片

        // 新增属性
        private String mainImage; // 奖品主图
        private String longImage; // 奖品长图

        private Long id; // 奖品id
        private String goodsCode; // 奖品商品编码
        private BigDecimal worthLower = new BigDecimal(0); // 价值范围
        private BigDecimal worthUpper = new BigDecimal(0); // 仅针对现金、红包和积分有效

        protected PrizeInfo(Parcel in) {
            name = in.readString();
            type = (short) in.readInt();
            description = in.readString();
            mode = (short) in.readInt();
            purpose = in.readString();
            content = in.readString();
            picture = in.readString();
            openPicture = in.readString();
            mainImage = in.readString();
            longImage = in.readString();
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readLong();
            }
            goodsCode = in.readString();
            showName = in.readString();
        }

        public static final Creator<PrizeInfo> CREATOR = new Creator<PrizeInfo>() {
            @Override
            public PrizeInfo createFromParcel(Parcel in) {
                return new PrizeInfo(in);
            }

            @Override
            public PrizeInfo[] newArray(int size) {
                return new PrizeInfo[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public short getType() {
            return type;
        }

        public void setType(short type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public short getMode() {
            return mode;
        }

        public void setMode(short mode) {
            this.mode = mode;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getOpenPicture() {
            return openPicture;
        }

        public void setOpenPicture(String openPicture) {
            this.openPicture = openPicture;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getLongImage() {
            return longImage;
        }

        public void setLongImage(String longImage) {
            this.longImage = longImage;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }

        public BigDecimal getWorthLower() {
            return worthLower;
        }

        public void setWorthLower(BigDecimal worthLower) {
            this.worthLower = worthLower;
        }

        public BigDecimal getWorthUpper() {
            return worthUpper;
        }

        public void setWorthUpper(BigDecimal worthUpper) {
            this.worthUpper = worthUpper;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        private String showName; // 显示名称

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeInt((int) type);
            dest.writeString(description);
            dest.writeInt((int) mode);
            dest.writeString(purpose);
            dest.writeString(content);
            dest.writeString(picture);
            dest.writeString(openPicture);
            dest.writeString(mainImage);
            dest.writeString(longImage);
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(id);
            }
            dest.writeString(goodsCode);
            dest.writeString(showName);
        }
    }

}
