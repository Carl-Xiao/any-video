package com.xiao.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author Carl-Xiao 2019-04-17
 */
public class CacheUtils {
    CacheBuilder suvCacheBuilder = CacheBuilder.newBuilder();
    public Cache cache ;
    public CacheUtils(){
        suvCacheBuilder.expireAfterAccess(3, TimeUnit.HOURS);
        suvCacheBuilder.expireAfterWrite(3, TimeUnit.HOURS);
        suvCacheBuilder.refreshAfterWrite(1,TimeUnit.SECONDS);
        cache = suvCacheBuilder.build();
    }
    public void putCache(String key,String value){
        this.cache.put(key,value);
    }

    public String getCache(String key){
        return (String) cache.getIfPresent(key);
    }


}
