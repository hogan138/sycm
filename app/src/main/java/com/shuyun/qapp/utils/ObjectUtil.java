package com.shuyun.qapp.utils;

import java.util.Map;

/**
 * @Package: com.shuyun.qapp.utils
 * @ClassName: ObjectUtil
 * @Description: java类作用描述
 * @Author: ganquan
 * @CreateDate: 2019/5/9 14:53
 */
public class ObjectUtil {

    /**
     * 类型强制转换
     *
     * @param obj
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 对象根据键取值强转
     *
     * @param obj
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object obj, Object key) {
        if (obj instanceof Map)
            return (T) ((Map) obj).get(key);

        return null;
    }
}
