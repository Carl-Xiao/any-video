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
        log.info("插入电视剧");
        List<TencentData> tencentDataList = resultItems.get(CrawKey.tencentUsDrama);
        tencentDataList.parallelStream().forEach(s -> tencentDao.inserIntoTencent(s));
    }
}
