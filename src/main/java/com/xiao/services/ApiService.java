package com.xiao.services;

import com.alibaba.fastjson.JSONObject;
import com.xiao.bean.Episode;
import com.xiao.bean.VideoDrama;
import com.xiao.dao.ApiSohuDao;
import com.xiao.utils.CommonUtils;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Service
@Slf4j
public class ApiService {
    private String infoUrl = "https://m.tv.sohu.com/phone_playinfo";

    private String getCookieUrl = "https://pv.sohu.com/suv/?t?=key_548_257?r?=";

    private final String UA = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Mobile Safari/537.36";

    @Autowired
    OkHttpUtils okHttpUtils;

    @Autowired
    ApiSohuDao apiSohuDao;

    public String getSUVCookie(String movieUrl) {
        if (CommonUtils.isEmpty(movieUrl)) {
            //测试 直接从浏览器找
            return "1903161050452CP6";
        }
        String tParm = CommonUtils.timeSixtyBit();
        String url = getCookieUrl.replace("key", tParm) + movieUrl;
        log.info("请求URL" + url);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("user-agent", UA);
        headerMap.put("referer", movieUrl);
        String suv = "";
        try {
            Response response = okHttpUtils.doGetCall(url);
            Headers headers = response.headers();
            List<String> cookies = headers.values("set-cookie");
            String suvCookie = "";
            for (String cookie : cookies) {
                int length = cookie.indexOf("SUV");
                if (length >= 0) {
                    suvCookie = cookie;
                    break;
                }
            }
            String[] cos = suvCookie.split(";");
            suvCookie = cos[0];
            log.info("cookie" + suvCookie);
            suv = suvCookie.substring(5, suvCookie.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("SUV:"+suv);
        return suv;
    }

    public Episode getInfo(String vid) {
        Episode episode = apiSohuDao.getEpisodeByVid(vid);
        if (episode != null) {
            return episode;
        }
        String uid = getSUVCookie("");
        Map<String, Object> parms = new HashMap<>();
        String utcTime = CommonUtils.timeUtcTime();
        String callback = "jsonpxkey_61_4".replace("key", utcTime);
        String api_key = "f351515304020cad28c92f70f002261c";
        String appid = "tv";
        String muid = CommonUtils.timeSixtyBit();
        String partner = "1";
        String plat = "17";
        String pt = "5";
        String qd = "680";
        String site = "1";
        String src = "11060001";
        String ssl = "1";
        String sver = "1.0";
        String _c = "1";
        parms.put("callback", callback);
        parms.put("api_key", api_key);
        parms.put("appid", appid);
        parms.put("muid", muid);
        parms.put("partner", partner);
        parms.put("plat", plat);
        parms.put("pt", pt);
        parms.put("qd", qd);
        parms.put("site", site);
        parms.put("src", src);
        parms.put("ssl", ssl);
        parms.put("sver", sver);
        parms.put("uid", uid);
        parms.put("_c", _c);
        parms.put("_", utcTime);
        parms.put("vid", vid);

        String episodeText = "";
        try {
            Response response = okHttpUtils.doGetCall(infoUrl, parms);
            if (response.isSuccessful()) {
                episodeText = response.body().string();
                episodeText = episodeText.replace(callback + "(", "");
                episodeText = episodeText.substring(0, episodeText.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sourceUrl = "";
        String playUrl = "";

        if (!CommonUtils.isEmpty(episodeText)) {
            JSONObject objectMap = JSONObject.parseObject(episodeText);
            JSONObject dataMap = objectMap.getJSONObject("data");
            //视频源地址
            sourceUrl = (String) dataMap.get("original_video_url");
            JSONObject urlMap = dataMap.getJSONObject("urls");

            JSONObject playUrlMap = urlMap.getJSONObject("mp4");
            playUrl = playUrlMap.toJSONString();
        }
        episode = new Episode();
        episode.setVid(vid);
        episode.setEpisodeText(episodeText);
        episode.setPlayUrl(playUrl);
        episode.setSourceUrl(sourceUrl);
        apiSohuDao.insertIntoEpisode(episode);
        return episode;
    }

    public List<VideoDrama> epsiodes(String playList) {
        return apiSohuDao.dramaPlayEpsiodes(playList);
    }

}
