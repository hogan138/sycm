package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: SignInBean
 * @Description: 用户签到信息
 * @Author: ganquan
 * @CreateDate: 2019/3/22 13:30
 */
@Data
public class SignInBean {

    /**
     * bp : 3222
     * days : 0
     * datas : [{"taskId":"6514710400368513024","day":"03.22","remark":"1积分","selected":false},{"taskId":"6514710402226589696","day":"03.23","remark":"2积分","selected":false},{"taskId":"6514710402381778944","day":"03.24","remark":"3积分","selected":false},{"taskId":"6514710402587299840","day":"03.25","remark":"增次卡*2","selected":false},{"taskId":"6514710402801209344","day":"03.26","remark":"5积分","selected":false},{"taskId":"6514710402948009984","day":"03.27","remark":"6积分","selected":false},{"taskId":"6514710403082227712","day":"03.28","remark":"7积分","selected":false}]
     * nextTaskId : 6514710400368513024
     * signDay : false
     */

    private Long bp;
    private Long days;
    private String nextTaskId;
    private boolean signDay;
    private List<DatasBean> datas;


    @Data
    public static class DatasBean {
        /**
         * taskId : 6514710400368513024
         * day : 03.22
         * remark : 1积分
         * selected : false
         */

        private String taskId;
        private String day;
        private String remark;
        private boolean selected;


    }
}
