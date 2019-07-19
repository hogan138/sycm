package com.shuyun.qapp.bean;

import java.util.List;

import lombok.Data;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.bean
 * @ClassName: TaskBeans
 * @Description: 任务积分
 * @Author: ganquan
 * @CreateDate: 2019/3/25 10:31
 */
@Data
public class TaskBeans {


    private List<DatasBean> datas;


    @Data
    public static class DatasBean {
        /**
         * tabTitle : 新手任务
         * tasks : [{"taskId":"6514710403220639744","name":"完成实名认证","picture":"http://192.168.3.157/icon/task/3.png","purpose":"在\u201c我的\u201d-\u201c用户信息\u201d-\u201c实名认证\u201d","prize":"10积分","action":"action.receive","actionLabel":"领取奖励"},{"taskId":"6514710403363246080","name":"绑定微信账号","picture":"http://192.168.3.157/icon/task/3.png","purpose":"在\u201c我的\u201d-\u201c用户信息\u201d-\u201c绑定微信\u201d进行微信绑定","prize":"10积分","action":"action.bind.wx","actionLabel":"绑定微信"},{"taskId":"6514710403463909376","name":"完成第一次答题","picture":"http://192.168.3.157/icon/task/3.png","purpose":"完成任意题组的答题","prize":"10积分","action":"action.receive","actionLabel":"领取奖励"},{"taskId":"6514710403572961280","name":"领取一次答题机会","picture":"http://192.168.3.157/icon/task/3.png","purpose":"我的\u201d页面点击增加次数，领取答题机会","prize":"10积分","action":"action.receive","actionLabel":"领取奖励"},{"taskId":"6514710403711373312","name":"邀请好友","picture":"http://192.168.3.157/icon/task/3.png","purpose":"邀请好友注册登录全民共进","prize":"50积分","action":"action.invite.friends","actionLabel":"立即前往","h5Url":"http://192.168.3.157/web/h5_invitation/index.html"}]
         */

        private String tabTitle;
        private List<TasksBean> tasks;


        @Data
        public static class TasksBean {
            /**
             * taskId : 6514710403220639744
             * name : 完成实名认证
             * picture : http://192.168.3.157/icon/task/3.png
             * purpose : 在“我的”-“用户信息”-“实名认证”
             * prize : 10积分
             * action : action.receive
             * actionLabel : 领取奖励
             * h5Url : http://192.168.3.157/web/h5_invitation/index.html
             */

            private String taskId;
            private String name;
            private String picture;
            private String purpose;
            private String prize;
            private String action;
            private String actionLabel;
            private String h5Url;
            private String content;


        }
    }
}
