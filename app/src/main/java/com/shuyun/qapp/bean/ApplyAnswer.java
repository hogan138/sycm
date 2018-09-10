package com.shuyun.qapp.bean;

import java.util.List;

/**
 * Created by sunxiao on 2018/5/4.
 */

public class ApplyAnswer {

    /**
     * id : 032f0c82074a48e896793be5e444d352
     * timeout : 0
     * answers : 8
     * questions : [{"id":10579,"title":"\u201c明月松间照，清泉石上流\u201d的作者是？","picture":"","oks":"37921","options":[{"id":37922,"title":"孟浩然","picture":null},{"id":37920,"title":"岑 参","picture":null},{"id":37921,"title":"王 维","picture":null},{"id":37919,"title":"高 适","picture":null}]},{"id":37245,"title":"\u201c斜阳草树，寻常巷陌，人道寄奴曾住。\u201d的作者是谁？","picture":"","oks":"126014","options":[{"id":126014,"title":"辛弃疾","picture":null},{"id":126016,"title":"苏轼","picture":null},{"id":126015,"title":"柳永","picture":null},{"id":126013,"title":"时彦","picture":null}]},{"id":36602,"title":"《宣州谢朓楼饯别校书叔云》的作者是谁？","picture":"","oks":"123447","options":[{"id":123447,"title":"李白","picture":null},{"id":123449,"title":"杜牧","picture":null},{"id":123450,"title":"苏轼","picture":null},{"id":123448,"title":"杜甫","picture":null}]},{"id":36863,"title":"下列不属于《赤壁》的是？","picture":"","oks":"124494","options":[{"id":124493,"title":"东风不与周郎便","picture":null},{"id":124492,"title":"自将磨洗认前朝","picture":null},{"id":124491,"title":"折戟沉沙铁未销","picture":null},{"id":124494,"title":"隔江犹唱后庭花","picture":null}]},{"id":25948,"title":"\u201c金凤玉露一相逢，便胜却人间无数\u201d是哪位词人的作品？","picture":"","oks":"92300","options":[{"id":92300,"title":"秦观","picture":null},{"id":92301,"title":"晏几道","picture":null},{"id":92303,"title":"柳永","picture":null},{"id":92302,"title":"李贺","picture":null}]},{"id":13049,"title":"南宋诗人范成大的号是？","picture":"","oks":"46764","options":[{"id":46766,"title":"六一居士","picture":null},{"id":46763,"title":"于湖居士","picture":null},{"id":46765,"title":"茶山居士","picture":null},{"id":46764,"title":"石湖居士","picture":null}]},{"id":36949,"title":"《题乌江亭》的作者是谁？","picture":"","oks":"124837","options":[{"id":124837,"title":"杜牧","picture":null},{"id":124835,"title":"李白","picture":null},{"id":124838,"title":"李清照","picture":null},{"id":124836,"title":"王维","picture":null}]},{"id":36699,"title":"下列关于《潼关吏》的诗句错误的是？","picture":"","oks":"123837","options":[{"id":123836,"title":"岂复忧西都","picture":null},{"id":123835,"title":"胡来但自守","picture":null},{"id":123837,"title":"障人视要处","picture":null},{"id":123838,"title":"窄狭容单车","picture":null}]}]
     */

    private String id;//本次答题的答题id
    private int timeout;//超时时长
    private int answers;//需要回答的题目数量
    private List<QuestionsBean> questions;//问题

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public List<QuestionsBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsBean> questions) {
        this.questions = questions;
    }

    public static class QuestionsBean {
        /**
         * id : 10579
         * title : “明月松间照，清泉石上流”的作者是？
         * picture :
         * oks : 37921
         * options : [{"id":37922,"title":"孟浩然","picture":null},{"id":37920,"title":"岑 参","picture":null},{"id":37921,"title":"王 维","picture":null},{"id":37919,"title":"高 适","picture":null}]
         */

        private int id;//题目id
        private String title;//题目标题
        private String picture;//题目的图片
        /**
         * 题目类型1:文本题目;2:图片题目;
         */
        private int type;
        private String oks;//正确选项
        private String oksIndex; // 正确的选项的下标
        private List<OptionsBean> options;//题目的选项

        public String getOksIndex() {
            return oksIndex;
        }

        public void setOksIndex(String oksIndex) {
            this.oksIndex = oksIndex;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getOks() {
            return oks;
        }

        public void setOks(String oks) {
            this.oks = oks;
        }

        public List<OptionsBean> getOptions() {
            return options;
        }

        public void setOptions(List<OptionsBean> options) {
            this.options = options;
        }

        public static class OptionsBean {
            /**
             * id : 37922
             * title : 孟浩然
             * picture : null
             */

            private int id;//选项id
            private String title;//选项标题
            private String picture;//选项图片

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            @Override
            public String toString() {
                return "OptionsBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", picture='" + picture + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "QuestionsBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", picture='" + picture + '\'' +
                    ", type=" + type +
                    ", oks='" + oks + '\'' +
                    ", options=" + options +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ApplyAnswer{" +
                "id='" + id + '\'' +
                ", timeout=" + timeout +
                ", answers=" + answers +
                ", questions=" + questions +
                '}';
    }
}
