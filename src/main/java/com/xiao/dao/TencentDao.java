package com.xiao.dao;

import com.xiao.bean.TencentData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author carl-Xiao
 */
@Component
@Mapper
public interface TencentDao {
    @Insert("insert into video_tencent_data(vid,href,name,episode) values(#{vid},#{href},#{name},#{episode}) ")
    Integer inserIntoTencent(TencentData tencentData);

    @Select("select * from video_tencent_data where #{md5}=#{0} order by episode asc ")
    List<TencentData> getAllEpisodeByMd5(String name);

    @Select(" SELECT md5,`name` FROM video_tencent_data GROUP BY md5 limit 4 ")
    List<TencentData> getRecommendLimitFour();


}
