package com.shuyun.qapp.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

import java.util.List;

import lombok.Data;

/**
 * 分类题组bean
 */
@Data
public class GroupClassifyBean {

    /**
     * err : 00000
     * ver : 1
     * dat : [{"id":0,"name":"活动","opportunity":0},{"id":1,"name":"节目答题","parentId":0,"opportunity":1,"children":[{"id":234,"name":"全民大探班40期","parentId":1,"opportunity":1}]},{"id":106,"name":"旅游","parentId":0,"opportunity":1,"children":[{"id":186,"name":"指尖中国行","parentId":106,"opportunity":1},{"id":187,"name":"世界旅游","parentId":106,"opportunity":1}]},{"id":107,"name":"文学","parentId":0,"opportunity":1,"children":[{"id":108,"name":"中国诗词大全","parentId":107,"opportunity":1},{"id":114,"name":"红楼梦专题","parentId":107,"opportunity":1},{"id":115,"name":"西游记专题","parentId":107,"opportunity":1},{"id":116,"name":"三国演义专题","parentId":107,"opportunity":1},{"id":117,"name":"水浒传专题","parentId":107,"opportunity":1},{"id":118,"name":"鲁迅系列","parentId":107,"opportunity":1},{"id":227,"name":"唐诗","parentId":107,"opportunity":1},{"id":237,"name":"宋词","parentId":107,"opportunity":1},{"id":239,"name":"成语","parentId":107,"opportunity":1},{"id":262,"name":"经典武侠小说","parentId":107,"opportunity":1},{"id":283,"name":"元曲","parentId":107,"opportunity":1}]},{"id":122,"name":"体育","parentId":0,"opportunity":1,"children":[{"id":123,"name":"足球","parentId":122,"opportunity":1},{"id":124,"name":"篮球","parentId":122,"opportunity":1},{"id":126,"name":"乒乓球","parentId":122,"opportunity":1},{"id":134,"name":"奥运会","parentId":122,"opportunity":1}]},{"id":139,"name":"历史","parentId":0,"opportunity":1,"children":[{"id":140,"name":"夏商周","parentId":139,"opportunity":1},{"id":141,"name":"春秋战国","parentId":139,"opportunity":1},{"id":142,"name":"秦汉","parentId":139,"opportunity":1},{"id":143,"name":"三国两晋南北朝","parentId":139,"opportunity":1},{"id":145,"name":"隋唐","parentId":139,"opportunity":1},{"id":147,"name":"宋朝历史","parentId":139,"opportunity":1},{"id":148,"name":"元朝历史","parentId":139,"opportunity":1},{"id":149,"name":"明朝历史","parentId":139,"opportunity":1},{"id":150,"name":"清朝历史","parentId":139,"opportunity":1},{"id":243,"name":"上下五千年","parentId":139,"opportunity":1}]},{"id":155,"name":"影视剧","parentId":0,"opportunity":1,"children":[{"id":157,"name":"国产电影","parentId":155,"opportunity":1},{"id":162,"name":"热播国产剧","parentId":155,"opportunity":1},{"id":200,"name":"动漫","parentId":155,"opportunity":1},{"id":276,"name":"热播电影","parentId":155,"opportunity":1}]},{"id":172,"name":"生活","parentId":0,"opportunity":1,"children":[{"id":173,"name":"星座","parentId":172,"opportunity":1},{"id":175,"name":"民俗文化","parentId":172,"opportunity":1},{"id":177,"name":"中华美食","parentId":172,"opportunity":1},{"id":206,"name":"养生","parentId":172,"opportunity":1},{"id":225,"name":"育儿知识","parentId":172,"opportunity":1},{"id":226,"name":"营养学知识","parentId":172,"opportunity":1},{"id":233,"name":"歇后语","parentId":172,"opportunity":1},{"id":244,"name":"生活小窍门","parentId":172,"opportunity":1},{"id":256,"name":"健康小贴士","parentId":172,"opportunity":1},{"id":268,"name":"家有萌宠","parentId":172,"opportunity":1},{"id":270,"name":"传染病预防","parentId":172,"opportunity":1},{"id":271,"name":"美容护肤","parentId":172,"opportunity":1},{"id":274,"name":"二十四节气","parentId":172,"opportunity":1},{"id":275,"name":"重阳节","parentId":172,"opportunity":1},{"id":279,"name":"冬季养生","parentId":172,"opportunity":1},{"id":280,"name":"2017年度热搜","parentId":172,"opportunity":1},{"id":281,"name":"春节","parentId":172,"opportunity":1},{"id":282,"name":"春季养生","parentId":172,"opportunity":1}]},{"id":192,"name":"音乐","parentId":0,"opportunity":1,"children":[{"id":194,"name":"全民来猜歌","parentId":192,"opportunity":1},{"id":196,"name":"内地流行音乐","parentId":192,"opportunity":1},{"id":197,"name":"港台流行音乐","parentId":192,"opportunity":1}]},{"id":218,"name":"民族复兴之路","parentId":0,"opportunity":1,"children":[{"id":219,"name":"\u201c一带一路\u201d专题","parentId":218,"opportunity":1}]},{"id":231,"name":"建军90周年专题","parentId":0,"opportunity":1,"children":[{"id":223,"name":"军旗飘扬","parentId":231,"opportunity":1},{"id":224,"name":"军歌嘹亮","parentId":231,"opportunity":1}]},{"id":232,"name":"活动专区","parentId":0,"opportunity":1,"children":[{"id":240,"name":"张灯结彩闹元宵","parentId":232,"opportunity":1},{"id":241,"name":"中国传统文化","parentId":232,"opportunity":1},{"id":245,"name":"玩转娱乐圈","parentId":232,"opportunity":1},{"id":265,"name":"端午活动题组","parentId":232,"opportunity":1},{"id":272,"name":"七夕活动题组","parentId":232,"opportunity":1}]},{"id":247,"name":"签到题","parentId":0,"opportunity":1,"children":[{"id":248,"name":"签到二级","parentId":247,"opportunity":1}]},{"id":253,"name":"教育","parentId":0,"opportunity":1,"children":[{"id":255,"name":"基金从业资格","parentId":253,"opportunity":1},{"id":258,"name":"会计基础","parentId":253,"opportunity":1},{"id":266,"name":"驾考科目一","parentId":253,"opportunity":1},{"id":267,"name":"算数","parentId":253,"opportunity":1}]}]
     */

    /**
     * id : 0
     * name : 活动
     * opportunity : 0
     * parentId : 0
     * children : [{"id":234,"name":"全民大探班40期","parentId":1,"opportunity":1}]
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//题组id
    private String name;//题组名称
    private Long opportunity;//消耗答题机会次数0:表示不消耗答题机会次数
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;//上级题组
    private String picture;//题组主图
    private Boolean isFlag;//是否点击选中
    private Long guideId;//指南id
    private String merchantName;//题组参与的活动商户名称
    private List<ChildrenBean> children;



    @Override
    public String toString() {
        return "GroupClassifyBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", opportunity=" + opportunity +
                ", parentId=" + parentId +
                ", picture='" + picture + '\'' +
                ", isFlag=" + isFlag +
                ", guideId=" + guideId +
                ", merchantName='" + merchantName + '\'' +
                ", children=" + children +
                '}';
    }
    @Data
    public static class ChildrenBean {
        /**
         * id : 234
         * name : 全民大探班40期
         * parentId : 1
         * opportunity : 1
         */
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;//题组id
        private String name;//题组名称
        private Long opportunity;//消耗答题机会次数0:表示不消耗答题机会次数
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long parentId;//上级题组
        private String picture;//题组主图
        private Boolean isFlag;//是否点击选中
        private Long guideId;//指南id
        private String merchantName;//题组参与的活动商户名称
        private String h5Url; //答题url
        private String opportunityLabel; //不消耗答题次数
        private String tag;//答题攻略
        private List<TagsBean> tags;  //积分、现金、准确率

        //任意位置logo配置
        private List<AdConfigs> adConfigs;

        //是否推荐
        private Boolean recommend;
        private String remark;


        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", opportunity=" + opportunity +
                    ", parentId=" + parentId +
                    ", picture='" + picture + '\'' +
                    ", isFlag=" + isFlag +
                    ", guideId=" + guideId +
                    ", merchantName='" + merchantName + '\'' +
                    '}';
        }
    }
    @Data
    public static class TagsBean {
        /**
         * groupId : 611
         * remark : 10%
         * tagName : 获得积分
         */

        private Long groupId;
        private String remark;
        private String tagName;


    }

    @Data
    public static class AdConfigs {

        /**
         * type : 1
         * location : 8
         * width : 150
         * height : 39
         * padding : 8,8,8,8
         * shadow : 1
         * shadowColor : #000000
         * shadowAlpha : 0.2
         * shadowRadius : 8,8,0,0
         * imageUrl : https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/pingan/pahys_logo2.png
         */

        private Long type;
        private Long location;
        private Long width;
        private Long height;
        private String padding;
        private String margin;
        private Long shadow;
        private String shadowColor;
        private String shadowAlpha;
        private String shadowRadius;
        private String imageUrl;


    }

}
