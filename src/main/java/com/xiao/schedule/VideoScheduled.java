package com.xiao.schedule;

import com.xiao.crawel.BibilPageProcessor;
import com.xiao.crawel.BibilPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @author carl-Xiao
 * http://www.pppet.net/ cron在线表达式生成器
 */
@Component
@Slf4j
public class VideoScheduled {
    @Autowired
    BibilPipeline bibilPipeline;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void runningMan() {
        //2018数据已经不合更新，没必要再采集
        //2019数据采集
        log.info("开始采集RunningMan2019合集");
        Spider spider = Spider.create(new BibilPageProcessor());
        spider.addUrl("https://www.bilibili.com/video/av40112794");
        spider.addPipeline(bibilPipeline);
        spider.setExitWhenComplete(true);
        spider.start();
    }

}
