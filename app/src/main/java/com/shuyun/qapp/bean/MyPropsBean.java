package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: MyPropsBean
 * @Description: 我的道具bean
 * @Author: ganquan
 * @CreateDate: 2018/11/30 13:53
 */
@Data
public class MyPropsBean {


    /**
     * count : 22222222222
     * prizeName : 增次卡
     * prizePicture : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/prize/jfbx/zengcika.png
     * prizeMode : 622222222
     * action : action.prop.use
     * actionLabel : 使用
     * 道具
     * explain : 可用于增加答题次数
     * remark : <span style="color: #939393;">其中3件道具将于</span> <span style="color: #FF0000;">明天6:30</span> <span style="color: #939393;">到期</span>
     */

    private String count;
    private String prizeName;
    private String prizePicture;
    private String prizeMode;
    private String action;
    private String actionLabel;
    private String explain;
    private String remark;


}
