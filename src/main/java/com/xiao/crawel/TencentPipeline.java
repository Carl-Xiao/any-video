package com.xiao.crawel;

import com.xiao.bean.CrawKey;
import com.xiao.bean.TencentData;
import com.xiao.dao.TencentDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Repository
@Slf4j
public class TencentPipeline implements Pipeline {

    @Autowired
    TencentDao tencentDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("title:" + resultItems.get("title"));
        String md5 = resultItems.get("md5");
        Integer episode = tencentDao.getHighestEpisodeByMd5(md5);
        if (episode == null) {
            episode = 0;
        }
        log.info("highest episode:" + episode);
        List<TencentData> tencentDataList = resultItems.get(CrawKey.tencentUsDrama);
        Integer finalEpisode = episode;
        tencentDataList.parallelStream().forEach(s -> {
                    String collecEpisode = s.getEpisode();
                    if (Integer.parseInt(collecEpisode) > finalEpisode) {
                        tencentDao.inserIntoTencent(s);
                    }
                }
        );
    }
}
