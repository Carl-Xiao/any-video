package com.xiao.crawel;

import com.xiao.bean.CrawKey;
import com.xiao.bean.TencentData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

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
        String url = page.getUrl().get();
        int md5length = url.indexOf("cover/");
        int md5lastlength = url.lastIndexOf("/");
        String md5 = url.substring(md5length + 6, md5lastlength);

        Html html = page.getHtml();
        String title = html.xpath("//title/text()").toString();
        int length = title.indexOf("第");
        title = title.substring(0, length - 1);
        log.info("视频title:" + title);
        Document document = html.getDocument();
        Elements elements = document.getElementsByAttributeValueContaining("data-tpl", "episode");
        List<TencentData> tencentDataList = new ArrayList<>();
        if (elements.size() < 1) {
            log.info("数据结构不对");
            return;
        }
        Element element = elements.first();
        Elements elements1 = element.getElementsByTag("span");
        if (elements1.size() < 1) {
            log.info("数据结构不对");
            return;
        }
        for (Element e : elements1) {
            TencentData tencentData = new TencentData();
            String href = e.html();
            if (href.contains("预告")) {
                continue;
            }
            href = href.substring(href.lastIndexOf("href") + 6, href.lastIndexOf("_stat") - 2);
            String vid = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf(".html"));
            String text = e.text();
            tencentData.setVid(vid);
            tencentData.setHref(href);
            tencentData.setEpisode(text);
            tencentData.setName(title);
            tencentData.setMd5(md5);
            tencentDataList.add(tencentData);
        }
        page.putField("title",title);
        page.putField("md5",md5);
        page.putField(CrawKey.tencentUsDrama, tencentDataList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://v.qq.com/x/cover/pgd7q0o4xlhe3r8/b0029shkvvr.html";

//        Spider.create(new TencentPageProcessor()).addUrl("https://v.qq.com/x/cover/pgd7q0o4xlhe3r8/b0029shkvvr.html").run();
    }

}
