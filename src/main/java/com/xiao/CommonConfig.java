package com.xiao;

import com.xiao.utils.CacheUtils;
import com.xiao.utils.OkHttpUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Configuration
public class CommonConfig {
    @Bean
    public OkHttpUtils okHttpUtils(){
        return new OkHttpUtils();
    }

    @Bean
    public CacheUtils cacheUtils(){
        return new CacheUtils();
    }


}
