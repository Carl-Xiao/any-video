package com.xiao.dao;

import com.xiao.bean.Episode;
import com.xiao.bean.VideoDrama;
import com.xiao.bean.VideoRecommend;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Carl-Xiao 2019-03-14
 */
@Component
@Mapper
public interface ApiSohuDao {

    @Insert("insert into video_episode_info(vid,source_url,play_url,episode_text) values(#{vid},#{sourceUrl},#{playUrl},#{episodeText} )")
    int insertIntoEpisode(Episode episode);

    @Select(" select a.vid,a.source_url,a.play_url,b.`name` from video_episode_info a , video_drama_info b WHERE a.vid=b.vid AND a.vid = #{0} ")
    @Results({
            @Result(property = "vid", column = "vid"),
            @Result(property = "sourceUrl", column = "source_url"),
            @Result(property = "playUrl", column = "play_url"),
            @Result(property = "name", column = "name"),
    })
    Episode getEpisodeByVid(String vid);


    @Insert("insert into video_drama_info(vid,name,showName,`desc`) values(#{vid},#{name},#{showName},#{desc})")
    int insertIntoVideoDrama(VideoDrama videoDrama);


    @Select("select * from  video_drama_recommend limit 4 ")
    List<VideoRecommend> recommendVideoDrama();

    @Select(" SELECT vid,`name`,sort FROM video_drama_info ORDER BY sort ASC ")
    List<VideoDrama> dramaPlayEpsiodes(String playListId);

}
