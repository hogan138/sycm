package com.shuyun.qapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: AgainstGruopListBeans
 * @Description: 答题对战列表
 * @Author: ganquan
 * @CreateDate: 2019/5/16 14:50
 */
@Data
public class AgainstGruopListBeans implements Parcelable {


    /**
     * label : 文化
     * datas : [{"bp":0,"name":"端午节专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/27/b990a806f0b74162bda33761bc609cfe.jpg","groupId":27},{"bp":0,"name":"非物质文化遗产","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/28/c6ebfe8d25d944cab8f2e73f734ccccb.png","groupId":28},{"bp":0,"name":"二十四节气","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/29/e9e36f0a68be4cbc944fac1095e494dc.png","groupId":29},{"bp":0,"name":"中国神话传说","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/30/11ef3f07749e41b4abf418e4f0b40d31.jpg","groupId":30},{"bp":0,"name":"京剧知识","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/jingju.jpg","groupId":31},{"bp":0,"name":"成语知识","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/chengyu.jpg","groupId":32},{"bp":0,"name":"常用歇后语","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/12/33/74b2b24927df442fa52df6d96d7a7ef1.png","groupId":33},{"bp":0,"name":"寓言故事","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/yuyan.jpg","groupId":34},{"bp":0,"name":"经典猜谜语","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/newgroup/cai.jpg","groupId":35},{"bp":0,"name":"认识生僻字","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/shengpizi717.jpg","groupId":36},{"bp":0,"name":"中秋节专题","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/mid/mid01.jpg","groupId":615},{"bp":0,"name":"中国古建筑","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/gujianzhu.jpg","groupId":"6511054949043015680"},{"bp":0,"name":"民间工艺","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/minjiangongyi.jpg","groupId":"6516124494170558464"},{"bp":0,"name":"图片题组","picture":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/group/minjiangongyi.jpg","groupId":"6516534576297414656"}]
     */

    private String label;
    private List<DatasBean> datas;

    protected AgainstGruopListBeans(Parcel in) {
        label = in.readString();
    }

    public static final Creator<AgainstGruopListBeans> CREATOR = new Creator<AgainstGruopListBeans>() {
        @Override
        public AgainstGruopListBeans createFromParcel(Parcel in) {
            return new AgainstGruopListBeans(in);
        }

        @Override
        public AgainstGruopListBeans[] newArray(int size) {
            return new AgainstGruopListBeans[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
    }

    @Data
    public static class DatasBean implements Parcelable{
        /**
         * bp : 0
         * name : 端午节专题
         * picture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/group/1/10/27/b990a806f0b74162bda33761bc609cfe.jpg
         * groupId : 27
         */

        private Long bp;
        private String name;
        private String picture;
        private Long groupId;
        private String description;

        protected DatasBean(Parcel in) {
            if (in.readByte() == 0) {
                bp = null;
            } else {
                bp = in.readLong();
            }
            name = in.readString();
            picture = in.readString();
            if (in.readByte() == 0) {
                groupId = null;
            } else {
                groupId = in.readLong();
            }
            description = in.readString();
        }

        public static final Creator<DatasBean> CREATOR = new Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel in) {
                return new DatasBean(in);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (bp == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(bp);
            }
            dest.writeString(name);
            dest.writeString(picture);
            if (groupId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(groupId);
            }
            dest.writeString(description);
        }
    }
}
