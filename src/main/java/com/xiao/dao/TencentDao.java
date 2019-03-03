package com.xiao.dao;

import com.xiao.bean.TencentData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author carl-Xiao
 */
@Component
@Mapper
public interface TencentDao {
    @Insert("insert into video_tencent_data(vid,href,name,episode) values(#{vid},#{href},#{name},#{episode}) ")
    Integer inserIntoTencent(TencentData tencentData);
}
