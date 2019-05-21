package com.shuyun.qapp.bean;

import lombok.Data;

/**
 * @Package: com.shuyun.qapp.bean
 * @ClassName: GameListBeans
 * @Description: 游戏中心列表
 * @Author: ganquan
 * @CreateDate: 2019/5/14 14:02
 */
@Data
public class GameListBeans {

    /**
     * name : 友玩-游戏名称
     * url : http://10test6-wap.stg3.1768.com/?act=landing&st=goto_page&track_u=skyjd&goUrl=http%3a%2f%2f10test6-wap.stg3.1768.com%2f%3fact%3dgame_collection_mquepage%26track_u%3dskyjd
     */

    private String name;
    private String url;
    private String picture;

}
