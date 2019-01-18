package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

/**
 * 查看答题结果返回数据
 */

public class LookAnswerResultBean {

    /**
     * {"err":"00000","ver":1,"dat":{"total":8,"actual":8,"accuracy":0.00,"correct":0,"error":8,"timeout":0,"examTime":1528249251571,"finishTime":1528249259625,"result":0,"questions":[{"id":109297,"title":"夏季养心尤其关键，下列属于护心食物的是？","orderNo":2,"result":0,"oks":"235189","answer":"235191","examTime":1528249252346,"finishTime":0,"options":[{"id":235189,"title":"其余都对","orderNo":1},{"id":235190,"title":"冬瓜","orderNo":4},{"id":235192,"title":"芹菜","orderNo":3},{"id":235191,"title":"豆类","orderNo":2}]},{"id":109303,"title":"对于夏季依然坚持锻炼身体的人可以选择练（ ）。","orderNo":1,"result":0,"oks":"235208","answer":"235210","examTime":1528249251259,"finishTime":0,"options":[{"id":235209,"title":"咏春拳","orderNo":3},{"id":235208,"title":"太极拳","orderNo":4},{"id":235211,"title":"虎拳","orderNo":2},{"id":235210,"title":"长拳","orderNo":1}]},{"id":109332,"title":"下列属于防暑药物的是？","orderNo":5,"result":0,"oks":"235306","answer":"235307","examTime":1528249255776,"finishTime":0,"options":[{"id":235306,"title":"其余都对","orderNo":3},{"id":235309,"title":"无极丹","orderNo":2},{"id":235308,"title":"清凉油","orderNo":4},{"id":235307,"title":"藿香正气水","orderNo":1}]},{"id":109348,"title":"夏季防晒的办法是？","orderNo":6,"result":0,"oks":"235355","answer":"235356","examTime":1528249257019,"finishTime":0,"options":[{"id":235355,"title":"其余都对","orderNo":4},{"id":235358,"title":"戴帽戴墨镜","orderNo":1},{"id":235357,"title":"打伞","orderNo":3},{"id":235356,"title":"涂防晒霜","orderNo":2}]},{"id":109394,"title":"夏日治疗风热感冒的中成药是哪种？","orderNo":4,"result":0,"oks":"235505","answer":"235508","examTime":1528249254591,"finishTime":0,"options":[{"id":235505,"title":"板蓝根冲剂","orderNo":1},{"id":235508,"title":"清暑益气丸","orderNo":2},{"id":235506,"title":"祛暑丸","orderNo":4},{"id":235507,"title":"藿香正气丸","orderNo":3}]},{"id":109402,"title":"夏天不适宜喝哪种粥？","orderNo":8,"result":0,"oks":"235539","answer":"235537","examTime":1528249259123,"finishTime":0,"options":[{"id":235537,"title":"杏仁粥","orderNo":1},{"id":235539,"title":"腊八粥","orderNo":3},{"id":235540,"title":"苦瓜粥","orderNo":4},{"id":235538,"title":"荷叶粥","orderNo":2}]},{"id":109414,"title":"夏季最佳的衣服颜色是？","orderNo":3,"result":0,"oks":"235586","answer":"235583","examTime":1528249253424,"finishTime":0,"options":[{"id":235583,"title":"白色","orderNo":1},{"id":235586,"title":"红色","orderNo":2},{"id":235585,"title":"蓝色","orderNo":3},{"id":235584,"title":"黑色","orderNo":4}]},{"id":109449,"title":"夏季女性应多喝什么来养生？","orderNo":7,"result":0,"oks":"235725","answer":"235723","examTime":1528249258106,"finishTime":0,"options":[{"id":235724,"title":"冰水","orderNo":4},{"id":235725,"title":"汤","orderNo":2},{"id":235726,"title":"酒","orderNo":1},{"id":235723,"title":"饮料","orderNo":3}]}]}}
     * err : 00000
     * ver : 1
     * dat : {"total":3,"actual":3,"accuracy":0,"correct":0,"error":3,"timeout":0,"examTime":1526875504882,"finishTime":1526875510896,"result":0,"questions":[{"id":532,"title":"请问《画壁》的主演孙俪毕业于哪个院校？","orderNo":2,"result":0,"oks":"2120","answer":"2121","examTime":1526875504680,"finishTime":1526875506688,"options":[{"id":2120,"title":"上海戏剧学院","orderNo":4},{"id":2121,"title":"中国传媒大学","orderNo":1},{"id":2118,"title":"中央戏剧学院","orderNo":3},{"id":2119,"title":"北京电影学院","orderNo":2}]},{"id":26969,"title":"乒乓球十九世纪始于哪个国家？","orderNo":1,"result":0,"oks":"96487","answer":"96484","examTime":1526875503651,"finishTime":1526875504680,"options":[{"id":96484,"title":"中国","orderNo":1},{"id":96486,"title":"法国","orderNo":2},{"id":96485,"title":"瑞典","orderNo":4},{"id":96487,"title":"英国","orderNo":3}]},{"id":26977,"title":"我国发现最早的纸币是在哪个时期？","orderNo":3,"result":0,"oks":"96507","answer":"96508","examTime":1526875506688,"finishTime":1526875510295,"options":[{"id":96508,"title":"元朝","orderNo":1},{"id":96506,"title":"唐朝","orderNo":3},{"id":96507,"title":"宋朝","orderNo":4},{"id":96509,"title":"明朝","orderNo":2}]}]}
     */

    /**
     * total : 3
     * actual : 3
     * accuracy : 0
     * correct : 0
     * error : 3
     * timeout : 0
     * examTime : 1526875504882
     * finishTime : 1526875510896
     * result : 0
     * questions : [{"id":532,"title":"请问《画壁》的主演孙俪毕业于哪个院校？","orderNo":2,"result":0,"oks":"2120","answer":"2121","examTime":1526875504680,"finishTime":1526875506688,"options":[{"id":2120,"title":"上海戏剧学院","orderNo":4},{"id":2121,"title":"中国传媒大学","orderNo":1},{"id":2118,"title":"中央戏剧学院","orderNo":3},{"id":2119,"title":"北京电影学院","orderNo":2}]},{"id":26969,"title":"乒乓球十九世纪始于哪个国家？","orderNo":1,"result":0,"oks":"96487","answer":"96484","examTime":1526875503651,"finishTime":1526875504680,"options":[{"id":96484,"title":"中国","orderNo":1},{"id":96486,"title":"法国","orderNo":2},{"id":96485,"title":"瑞典","orderNo":4},{"id":96487,"title":"英国","orderNo":3}]},{"id":26977,"title":"我国发现最早的纸币是在哪个时期？","orderNo":3,"result":0,"oks":"96507","answer":"96508","examTime":1526875506688,"finishTime":1526875510295,"options":[{"id":96508,"title":"元朝","orderNo":1},{"id":96506,"title":"唐朝","orderNo":3},{"id":96507,"title":"宋朝","orderNo":4},{"id":96509,"title":"明朝","orderNo":2}]}]
     */

    private Long total;//答题实际回答的数量
    private Long actual;//答题实际回答的数量
    private Double accuracy;//答题正确率
    private Long correct;//回答正确的数量
    private Long error;//回答错误的数量
    private Long timeout;//超时未回答的数量
    private Long examTime;//开始答题时间
    private Long finishTime;//完成答题时间 可能为空（中途退出）
    /**
     * 答题结果:
     * 0——未中奖
     * 1——中奖了
     */
    private Long result;
    private List<QuestionsBean> questions;

    public Long getTotal() {
        return total;
    }

    public Long getActual() {
        return actual;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Long getCorrect() {
        return correct;
    }

    public Long getError() {
        return error;
    }

    public Long getTimeout() {
        return timeout;
    }

    public Long getExamTime() {
        return examTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public Long getResult() {
        return result;
    }

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "LookAnswerResultBean{" +
                "total=" + total +
                ", actual=" + actual +
                ", accuracy=" + accuracy +
                ", correct=" + correct +
                ", error=" + error +
                ", timeout=" + timeout +
                ", examTime=" + examTime +
                ", finishTime=" + finishTime +
                ", result=" + result +
                ", questions=" + questions +
                '}';
    }

    public static class QuestionsBean {
        /**
         * id : 532
         * title : 请问《画壁》的主演孙俪毕业于哪个院校？
         * orderNo : 2
         * result : 0
         * oks : 2120
         * answer : 2121
         * examTime : 1526875504680
         * finishTime : 1526875506688
         * options : [{"id":2120,"title":"上海戏剧学院","orderNo":4},{"id":2121,"title":"中国传媒大学","orderNo":1},{"id":2118,"title":"中央戏剧学院","orderNo":3},{"id":2119,"title":"北京电影学院","orderNo":2}]
         */
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;//题目id
        private String title;//题目标题
        private String description;//题目描述
        private String remark;//题目备注
        /**
         * 题目类型
         * 1——文本题目
         * 2——图文题目
         */
        private Long type;
        private Long orderNo;//题目的排序 从1开始排序
        /**
         * 答题结果
         * 0——错误
         * 1——正确
         * -1——超时
         * 空——中途异常
         */
        private Long result;
        private String oks;//正确答案
        private String answer;//回答的选项
        private Long examTime;//本题目开始答题时间
        private Long finishTime;//本题目开始答题时间
        private List<PictureBean> picture;//题目的图片 type=2有效
        private List<OptionsBean> options;//题目的选项

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getRemark() {
            return remark;
        }

        public Long getType() {
            return type;
        }

        public Long getOrderNo() {
            return orderNo;
        }

        public Long getResult() {
            return result;
        }

        public String getOks() {
            return oks;
        }

        public String getAnswer() {
            return answer;
        }

        public Long getExamTime() {
            return examTime;
        }

        public Long getFinishTime() {
            return finishTime;
        }

        public List<PictureBean> getPicture() {
            return picture;
        }

        public List<OptionsBean> getOptions() {
            return options;
        }

        @Override
        public String toString() {
            return "QuestionsBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", remark='" + remark + '\'' +
                    ", type=" + type +
                    ", orderNo=" + orderNo +
                    ", result=" + result +
                    ", oks='" + oks + '\'' +
                    ", answer='" + answer + '\'' +
                    ", examTime=" + examTime +
                    ", finishTime=" + finishTime +
                    ", picture=" + picture +
                    ", options=" + options +
                    '}';
        }

        public static class PictureBean {
            private String url;//图片的地址

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "PictureBean{" +
                        "url='" + url + '\'' +
                        '}';
            }
        }

        public static class OptionsBean {
            /**
             * id : 2120
             * title : 上海戏剧学院
             * orderNo : 4
             */

            private String id;//选项id
            private String title;//选项标题
            private String description;//选项描述
            private String remark;//选项备注
            private String picture;//选项图片
            private Long orderNo;//选项的排序

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }

            public String getRemark() {
                return remark;
            }

            public String getPicture() {
                return picture;
            }

            public Long getOrderNo() {
                return orderNo;
            }

            @Override
            public String toString() {
                return "OptionsBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", remark='" + remark + '\'' +
                        ", picture='" + picture + '\'' +
                        ", orderNo=" + orderNo +
                        '}';
            }
        }
    }
}
