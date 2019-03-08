package com.xiao.dao;

import com.xiao.bean.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author Carl-Xiao 2018-12-04
 */
@Component
@Mapper
public interface VideoDao {

    @Insert("insert into video(title,playUrl,type,part,vid,fn,format,value) " +
            " values(#{title},#{playUrl},#{type},#{part},#{vid},#{fn},#{format},#{value})")
    int insertVideo(Video video);

    @Select("select title,playUrl,type,part,vid from video where vid =#{0}")
    Video getVideoPlayInfo(String vid);

}
