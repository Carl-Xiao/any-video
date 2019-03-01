package com.xiao.crawel;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author Carl-Xiao 2019-03-01
 * B站爬取RunngingMan分享视频
 */
@Slf4j
public class BibilPageProcessor implements PageProcessor {
    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    private Site site = Site.me()
            .setDomain("https://www.bilibili.com/")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000)
            .setUserAgent(AGENT);

    @Override
    public void process(Page page) {
        Selectable selectable = page.getHtml().xpath("//script/text()");
        String ss = selectable.get();
        log.info(ss);
    }

    @Override
    public Site getSite() {
        return site;

    }
    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.bilibili.com/video/av18089528/?p=1").run();
//        Spider.create(new GithubRepoPageProcessor()).test("https://github.com/code4craft") "https://github.com/sssvip";
    }

}
