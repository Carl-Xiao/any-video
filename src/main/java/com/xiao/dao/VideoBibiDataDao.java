package com.xiao.dao;

import com.xiao.bean.Page;
import com.xiao.bean.VideoBibiData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-08
 *
 */
@Component
@Mapper
public interface VideoBibiDataDao {

    @Select("select * from video_bibi_data where type=#{data.type} ORDER BY page DESC limit #{prev},#{after}")
    List<VideoBibiData> findAll(Page<VideoBibiData> videoBibiDataPage);

    @Select("select count(1) from video_bibi_data where type=#{type}")
    Long countBibiData(VideoBibiData videoBibiData);

    @Insert("insert into video_bibi_data(aid,cid,page,part,type) values(#{aid},#{cid},#{page},#{part},#{type}) ")
    Integer inserIntoVideoBibi(VideoBibiData videoBibiData);

    @Select("select * from video_bibi_data where page=#{0}")
    VideoBibiData getInfoByPage(Integer page);
}
