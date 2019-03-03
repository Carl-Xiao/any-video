package com.xiao.crawel;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

@Slf4j
public class TencentPageProcessor implements PageProcessor {
    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    private Site site = Site.me()
            .setDomain("https://v.qq.com/")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000)
            .setUserAgent(AGENT);

    @Override
    public void process(Page page) {
        log.info("获取数据");
        Html html = page.getHtml();

        Document document =  html.getDocument();

        Elements elements =  document.getElementsByAttributeValueContaining("data-tpl","episode");
        if(elements.size()>0){
            Element element = elements.first();
            Elements elements1 =  element.getElementsByTag("span");
            element = elements1.get(1);
            log.info(element.html());
            log.info(element.data());
            log.info(element.text());

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new TencentPageProcessor()).addUrl("https://v.qq.com/x/cover/pgd7q0o4xlhe3r8/b0029shkvvr.html").run();
    }

}
