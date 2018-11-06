package com.shuyun.qapp.bean;

/**
 * 实名认证bean
 */
public class RealNameBean {


    /**
     * certCount : 3
     * bizNo : ZM201811063000000414100366630516
     * body : https://openapi.alipay.com/gateway.do?alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2018041902582802&biz_content=%7B%22biz_no%22%3A%22ZM201811063000000414100366630516%22%7D&charset=UTF-8&format=json&method=zhima.customer.certification.certify&sign=ANXLHSJXIeX5l7%2BX18Fy1sftGNDcnvQVv3lQClW1elxuNzYZg6z4UMwPewxyG3G4XhXLgtHMmbhye0vWnWpC38rmwQWSSFIbN%2FDWoNsWCEPEzYdVYuMs3aUqeYbpZfCqTvmfknUOVuDMlS%2B7fqohterMMGcyjNuNcv6bD8qfqXDmPYGj%2FMQTqhy2Y%2B8cXh0%2FvjJWk%2FhUwKN%2FWUcFb6xc%2B%2FxkvyA0c7gJKWYVRbPqxmOstmDlP7h5QcUEdXPF852xfUaFHXzsxJJJp4BKyNcfCPzmmAFkK9KamcEQWbw1H2qNHnmKcjmmLIsTFUPZhPhutA9HEEZfTuw5xtsuM9u8LA%3D%3D&sign_type=RSA2&timestamp=2018-11-06+09%3A18%3A34&version=1.0
     * status : succeed
     */

    private int certCount;
    private String bizNo;
    private String body;
    private String status;

    public int getCertCount() {
        return certCount;
    }

    public void setCertCount(int certCount) {
        this.certCount = certCount;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
