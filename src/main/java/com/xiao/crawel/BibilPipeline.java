package com.xiao.crawel;


import com.xiao.bean.CrawKey;
import com.xiao.bean.VideoBibiData;
import com.xiao.dao.VideoBibiDataDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Repository
@Slf4j
public class BibilPipeline implements Pipeline {
    @Autowired
    VideoBibiDataDao videoBibiDataDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<VideoBibiData> bibiDats = resultItems.get(CrawKey.runnginMan2019);
        if (bibiDats.size()>0){
            bibiDats.parallelStream().forEach(t -> {
                videoBibiDataDao.inserIntoVideoBibi(t);
            });
        }
    }
}
