package com.xiao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @author Carl-Xiao 2018-12-08
 *
 */
public class JSONUtils {

    public static Map<String, Object> getObjectMap(String infos) {
        return JSON.parseObject(infos, new TypeReference<Map<String, Object>>() {
        });
    }

    public static List<String> getListString(String info) {
        return JSONObject.parseArray(info, String.class);
    }

    public static <T> List<T> getListDomain(String info, Class<T> t) {
        return JSONObject.parseArray(info, t);
    }

    public static String toJson(Object o) {
        return JSON.toJSONString(o);
    }
}
