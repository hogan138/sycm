package com.shuyun.qapp.bean;

import java.util.List;

/**
 * 活动专区bean
 */
public class ActivityTabBean {


    /**
     * result : [{"h5Url":"http://192.168.3.137:8080/h5_invitation/index.html","stop":false,"baseImage":"https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/banner/yaoqingyoujiang.jpg","id":1,"btnAction":"action.invite"}]
     * total : 1
     */

    private Long total;
    private List<ResultBean> result;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

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
        private Long id;
        private String btnAction;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public String getBaseImage() {
            return baseImage;
        }

        public void setBaseImage(String baseImage) {
            this.baseImage = baseImage;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getBtnAction() {
            return btnAction;
        }

        public void setBtnAction(String btnAction) {
            this.btnAction = btnAction;
        }
    }
}
