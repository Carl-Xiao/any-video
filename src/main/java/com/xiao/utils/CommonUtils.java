package com.xiao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carl-Xiao 2018-12-15
 */
public class CommonUtils {
    public static List<Integer> parseList(Integer max) {
        List<Integer> lists = new ArrayList<>(max);
        for (Integer i = 0; i < max; i++) {
            lists.add(i);
        }
        return lists;
    }

    public static boolean isEmpty(String result) {
        return result == null || "".equals(result);
    }

    public static String timeSixtyBit() {
        return (System.currentTimeMillis() * 1000 + Math.round(Math.random() * 1000)) + "";
    }

    public static String timeUtcTime() {
        return System.currentTimeMillis() + "";
    }


}
