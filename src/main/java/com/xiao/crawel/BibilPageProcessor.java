package com.xiao.crawel;

import com.xiao.bean.CrawKey;
import com.xiao.bean.VideoBibiData;
import com.xiao.bean.VideoBibiPage;
import com.xiao.utils.JSONUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        log.info("========start===========");
        Html html = page.getHtml();
        Document document = html.getDocument();
        Elements elements = document.getElementsByTag("script");
        List<VideoBibiData> bibiDats = new ArrayList<>();
        for (Element element : elements) {
            String content = element.html();
            int index = content.indexOf("window.__INITIAL_STATE__");
            if (index == 0) {
                int function = content.indexOf("(function");
                String infos = content.substring("window.__INITIAL_STATE__".length() + 1, function);
                log.info("=====first=====" + infos);
                infos = infos.substring(0, infos.lastIndexOf(";"));
                log.info("=====second=====" + infos);
                Map<String, Object> infoMap = JSONUtils.getObjectMap(infos);

                Long aid = Long.valueOf(infoMap.get("aid").toString());

                String videoData = infoMap.get("videoData").toString();
                Map<String, Object> videoDatas = JSONUtils.getObjectMap(videoData);
                List<VideoBibiPage> videoBibiPages = JSONUtils.getListDomain(videoDatas.get("pages").toString(), VideoBibiPage.class);
                for (VideoBibiPage info : videoBibiPages) {
                    String part = info.getPart();
                    if (part.contains("下期预告")) {
                        log.info("跳过下期预告");
                        continue;
                    }
                    VideoBibiData bibiData = new VideoBibiData();
                    bibiData.setAid(aid);
                    bibiData.setCid(info.getCid());
                    bibiData.setPage(info.getPage());
                    bibiData.setPart(info.getPart());
                    bibiData.setType(0);
                    bibiDats.add(bibiData);
                }
            }
            page.putField(CrawKey.runnginMan2019, bibiDats);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BibilPageProcessor()).addUrl("https://www.bilibili.com/video/av18089528/?p=1").run();
//        Spider.create(new GithubRepoPageProcessor()).test("https://github.com/code4craft") "https://github.com/sssvip";
    }

}
