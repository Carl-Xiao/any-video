package com.xiao.crawel;


import com.alibaba.fastjson.JSONObject;
import com.xiao.bean.CrawKey;
import com.xiao.bean.SohuEpisode;
import com.xiao.bean.VideoDrama;
import com.xiao.dao.ApiSohuDao;
import com.xiao.dao.VideoBibiDataDao;
import com.xiao.utils.JSONUtils;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SohuPipeline implements Pipeline {
    @Autowired
    VideoBibiDataDao videoBibiDataDao;
    @Autowired
    OkHttpUtils okHttpUtils;

    @Autowired
    ApiSohuDao apiSohuDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        SohuEpisode sohuEpisode = resultItems.get(CrawKey.SOHUEPISODE);
        log.info("sohuEpisode:" + sohuEpisode.toString());
        Map<String, Object> parms = new HashMap<>();
        parms.put("playlistid", sohuEpisode.getPlaylistid());
        parms.put("o_playlistId", sohuEpisode.getO_playlistId());
        parms.put("vid", sohuEpisode.getVid());
        parms.put("pianhua", "0");
        parms.put("pageRule", "undefined");
        parms.put("pagesize", "999");
        parms.put("order", "0");
        parms.put("cnt", "1");
        parms.put("ssl", "0");
        parms.put("preVideoRule", "3");
        parms.put("callback", "__get_videolist");
        String content = "";
        try {
            Response response = okHttpUtils.doGetCall("https://pl.hd.sohu.com/videolist", parms);
            if (response.isSuccessful()) {
                content = response.body().string();
                content = content.replace("__get_videolist(", "");
                content = content.substring(0, content.length() - 2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> contentMap = JSONUtils.getObjectMap(content);
        List<JSONObject> videos = (List<JSONObject>) contentMap.get("videos");
        log.info("episodeSize:" + videos.size());
        for (JSONObject video : videos) {
            String name = video.get("name") + "";
            String vid = video.get("vid") + "";
            String showName = video.get("showName") + "";
            VideoDrama videoDrama = new VideoDrama();
            videoDrama.setVid(vid);
            videoDrama.setName(name);
            String sort = showName.substring(1, showName.length()-1);
            videoDrama.setSort(sort);
            videoDrama.setShowName(showName);
            apiSohuDao.insertIntoVideoDrama(videoDrama);
        }
    }

}
