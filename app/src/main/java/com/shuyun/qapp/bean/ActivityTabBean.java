package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * 活动专区bean
 */
@Data
public class ActivityTabBean {


    /**
     * result : [{"h5Url":"http://192.168.3.137:8080/h5_invitation/index.html","stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/yaoqingyoujiang.jpg","id":1,"btnAction":"action.invite"}]
     * total : 1
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long total;
    private List<ResultBean> result;

    @Data
    public static class ResultBean {
        /**
         * h5Url : http://192.168.3.137:8080/h5_invitation/index.html
         * stop : false
         * baseImage : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/yaoqingyoujiang.jpg
         * id : 1
         * btnAction : action.invite
         */

        private String h5Url;
        private boolean stop;
        private String baseImage;
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;
        private String btnAction;
        private String content;
        private Long isLogin;

    }
}
