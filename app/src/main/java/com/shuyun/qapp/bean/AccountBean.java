package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * 现金流水和积分流水记录共用的javaBean
 */
@Data
public class AccountBean {

    /**
     * err : 00000
     * ver : 1
     * dat : [{"source":3,"amount":50,"status":1,"time":1525675183704,"way":2},{"source":1,"amount":0.4,"status":0,"time":1493773641000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1493773597000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1493773539000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1492128873000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1492067150000,"way":1},{"source":1,"amount":0.1,"status":0,"time":1492067077000,"way":1},{"source":4,"amount":3,"status":0,"time":1491981790000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1491957100000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1491880937000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1491880781000,"way":1},{"source":1,"amount":0.1,"status":0,"time":1491800084000,"way":1},{"source":4,"amount":0.6,"status":0,"time":1491796613000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1491453275000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1491391261000,"way":1},{"source":4,"amount":1,"status":0,"time":1491282711000,"way":1},{"source":1,"amount":0.5,"status":0,"time":1491282666000,"way":1},{"source":1,"amount":0.1,"status":0,"time":1491022805000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1491022767000,"way":1},{"source":4,"amount":10,"status":0,"time":1491011621000,"way":1},{"source":1,"amount":0.2,"status":0,"time":1491011586000,"way":1},{"source":4,"amount":1,"status":0,"time":1490963427000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1490830338000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1490830033000,"way":1},{"source":1,"amount":1.4,"status":0,"time":1490774981000,"way":1},{"source":1,"amount":0.4,"status":0,"time":1490774866000,"way":1},{"source":1,"amount":0.4,"status":0,"time":1490659243000,"way":1},{"source":1,"amount":0.5,"status":0,"time":1490585786000,"way":1},{"source":1,"amount":0.3,"status":0,"time":1490585607000,"way":1},{"source":1,"amount":0.4,"status":0,"time":1490482918000,"way":1}]
     */
    /**
     * 现金来源1:宝箱;3:提现
     */
    private Long source;
    /**
     * 现金金额 精确到分
     * 积分个数
     */
    private String amount;
    private Long status;//流水状态
    private String time;//流水时间
    private String name;//名称
    /**
     * 流水方式：
     * 1——收入
     * 2——支出
     */
    private int way;


    @Override
    public String toString() {
        return "AccountBean{" +
                "source=" + source +
                ", amount='" + amount + '\'' +
                ", status=" + status +
                ", time='" + time + '\'' +
                ", way=" + way +
                '}';
    }
}
