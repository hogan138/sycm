package com.shuyun.qapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：QMGJ
 * 创建人：${ganquan}
 * 创建日期：2018/6/24 17:38
 * 保存集合到本地
 */
public class ListDataSave {

    private SharedPreferences preferences;

    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences.Editor editor = preferences.edit();
        //转换成json数据，再保存
        String strJson = JSON.toJSONString(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.apply();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        datalist = JSON.parseObject(strJson, new TypeReference<List<T>>() {
        });
        return datalist;

    }
}

