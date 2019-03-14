package com.xiao.crawel;

import com.xiao.bean.CrawKey;
import com.xiao.bean.SohuEpisode;
import com.xiao.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author Carl-Xiao 2019-03-01
 * 搜狐视频
 */
@Slf4j
public class SohuProcessor implements PageProcessor {
    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    private Site site = Site.me()
            .setDomain("https://tv.sohu.com")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000)
            .setUserAgent(AGENT);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document document = html.getDocument();
        Elements elements = document.getElementsByTag("script");
        String infoHtml = "";
        for (Element element : elements) {
            String ehtml = element.html();
            int vid = ehtml.indexOf("vid");
            int playlistId = ehtml.indexOf("playlistId");
            int oplaylistId = ehtml.indexOf("o_playlistId");
            if (vid > 0 && playlistId > 0 || oplaylistId > 0) {
                log.info("HAHA");
                infoHtml = ehtml;
                break;
            }
        }
        String[] infoHtmls = infoHtml.trim().split(";");
        String vid = "";
        String playlistId = "";
        String oplaylistId = "";
        for (String info : infoHtmls) {
            info = info.replace(" ", "").replace("\r\n", "");
            int vidIndex = info.indexOf("varvid");
            int playlistIdIndex = info.indexOf("varplaylistId");
            int oplaylistIdIndex = info.indexOf("varo_playlistId");
            if (vidIndex == 0 && CommonUtils.isEmpty(vid)) {
                vid = info;
                continue;
            }
            if (playlistIdIndex == 0 && CommonUtils.isEmpty(playlistId)) {
                playlistId = info;
                continue;
            }
            if (oplaylistIdIndex == 0 && CommonUtils.isEmpty(oplaylistId)) {
                oplaylistId = info;
                continue;
            }
        }
        vid = vid.substring(8, vid.length() - 1);
        playlistId = playlistId.substring(15, playlistId.length() - 1);
        oplaylistId = oplaylistId.substring(17, oplaylistId.length() - 1);
        if (!CommonUtils.isEmpty(vid)) {
            log.info("========获取数据==========");
            SohuEpisode sohuEpisode = new SohuEpisode();
            sohuEpisode.setVid(vid);
            sohuEpisode.setPlaylistid(playlistId);
            sohuEpisode.setO_playlistId(oplaylistId);
            page.putField(CrawKey.SOHUEPISODE, sohuEpisode);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SohuProcessor()).addUrl("https://tv.sohu.com/v/MjAxNzExMDkvbjYwMDI0NzEwMi5zaHRtbA==.html").run();
//        String vid = "var vid=\"4289454\"";

//        System.out.println(vid.replace(" ", ""));

    }

}
