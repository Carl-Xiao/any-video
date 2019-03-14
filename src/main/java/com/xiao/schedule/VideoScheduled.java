package com.xiao.schedule;

import com.xiao.crawel.BibilPageProcessor;
import com.xiao.crawel.BibilPipeline;
import com.xiao.crawel.SohuPipeline;
import com.xiao.crawel.SohuProcessor;
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

    @Autowired
    SohuPipeline sohuPipeline;

    /**
     * 每周一10点AM。8点PM
     */
    @Scheduled(cron = "0 0 10,20 ? * MON")
    public void runningMan() {
        //2019数据采集
        log.info("开始采集RunningMan2019合集");
        Spider spider = Spider.create(new BibilPageProcessor());
        spider.addUrl("https://www.bilibili.com/video/av40112794");
        spider.addPipeline(bibilPipeline);
        spider.setExitWhenComplete(true);
        spider.start();
    }
    /**
     * 搜狐视频
     */
    @Scheduled(cron = "0 */1 * * * ? ")
    public void souhuVideo() {
        String url = "https://tv.sohu.com/v/MjAxNzExMDkvbjYwMDI0NzEwMi5zaHRtbA==.html";
        Spider spider = Spider.create(new SohuProcessor());
        spider.addUrl(url);
        spider.addPipeline(sohuPipeline);
        spider.setExitWhenComplete(true);
        spider.start();
    }

}
