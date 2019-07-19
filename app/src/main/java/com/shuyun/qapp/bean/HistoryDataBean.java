package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户答题记录
 */
@Data
public class HistoryDataBean implements Serializable {

    private Long total;//记录总数
    private List<ResultBean> result;//记录


    @Data
    public static class ResultBean implements Serializable {
        private String id;//答题id
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long groupId;//题组id
        private String picture;//题组主图
        private String time;//答题时间
        private String accuracy;//正确率
        private String title;//题组名称
        private String merchantName;//商户名称
        private Long result;
        private String fullName; //所在分类


    }
}
