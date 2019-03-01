package com.xiao.dao;

import com.xiao.bean.VideoInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-04
 *
 */
@Component
@Mapper
public interface VideoDao {
    @Select("SELECT a.*,b.type_name FROM video_info a LEFT JOIN video_type b ON a.type=b.id ")
    List<VideoInfo> findall();

    @Select("SELECT a.*,b.type_name FROM video_info a LEFT JOIN video_type b ON a.type=b.id where id=#{id} ")
    VideoInfo getById(Long id);


    @Insert("insert into video_info(name,head_img_url,play_url,type) " +
            " values(#{name},#{head_img_url},#{play_url},#{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertInfo(VideoInfo videoInfo);

    @Insert("update video_info set name=#{name},head_img_url=#{head_img_url},play_url=#{play_url},type=#{type} " +
            " where id =#{id}) ")
    Integer updateInfo(VideoInfo videoInfo);

    @Delete("delete from video_info where id=#{id} ")
    Integer deleteInfo(Integer id);
}
