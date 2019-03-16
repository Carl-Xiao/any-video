package com.xiao.services;

import com.alibaba.fastjson.JSONObject;
import com.xiao.bean.Episode;
import com.xiao.bean.VideoDrama;
import com.xiao.dao.ApiSohuDao;
import com.xiao.utils.CommonUtils;
import com.xiao.utils.JSONUtils;
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
        log.info("SUV");
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

    public static void main(String[] args) {
        String s2 = "{\n" +
                "    \"data\": {\n" +
                "        \"cate_code\": \"101106103;101106106;101106107;101112102\",\n" +
                "        \"year\": \"2018\",\n" +
                "        \"fee\": 0,\n" +
                "        \"area_id\": 6,\n" +
                "        \"vid\": 4966860,\n" +
                "        \"video_is_fee\": 0,\n" +
                "        \"small_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_1jJW5_pic8.jpg\",\n" +
                "        \"tvPlayType\": 0,\n" +
                "        \"serious\": 0,\n" +
                "        \"tip\": \"\",\n" +
                "        \"durations\": {\n" +
                "            \"nor\": [\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"226.84\"\n" +
                "            ],\n" +
                "            \"hig\": [\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"226.84\"\n" +
                "            ],\n" +
                "            \"sup\": [\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"300.002\",\n" +
                "                \"226.84\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"caption_ver\": 1,\n" +
                "        \"isSohu\": 1,\n" +
                "        \"keyword\": \"我在大理寺当宠物;我在大理寺当宠物第1集;我在大理寺当宠物全集;我在大理寺当宠物电视剧;偶像剧;言情剧;古装剧;悬疑剧;神话剧;;\",\n" +
                "        \"url_html5\": \"https://m.tv.sohu.com/v4966860.shtml\",\n" +
                "        \"area\": \"内地剧\",\n" +
                "        \"hor_w16_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_1dQd4_pic22.jpg\",\n" +
                "        \"create_time\": 1537857720000,\n" +
                "        \"hor_big_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_7iuw0_pic6.jpg\",\n" +
                "        \"ep\": [\n" +
                "            {\n" +
                "                \"pointCharacter\": \"\",\n" +
                "                \"pointDesc\": \"御猫镇魂遭控制，猝不及防搞穿越\",\n" +
                "                \"pointTime\": \"105\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"pointCharacter\": \"\",\n" +
                "                \"pointDesc\": \"鲜肉洗澡美如画，女鬼画风吓小岚\",\n" +
                "                \"pointTime\": \"473\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"pointCharacter\": \"\",\n" +
                "                \"pointDesc\": \"养喵日常问题多，幻化人形偷溜走\",\n" +
                "                \"pointTime\": \"914\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"pointCharacter\": \"\",\n" +
                "                \"pointDesc\": \"意图穿越终失败，街头躲藏被发现\",\n" +
                "                \"pointTime\": \"1230\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"play_count\": 22536619,\n" +
                "        \"start_time\": 0,\n" +
                "        \"total_duration\": 1726.85,\n" +
                "        \"show_date\": \"\",\n" +
                "        \"url_56_html5\": \"https://m.56.com/pa/v4966860.shtml\",\n" +
                "        \"ver_big_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_9HJS2_pic7.jpg\",\n" +
                "        \"aid\": 9479802,\n" +
                "        \"cid\": 2,\n" +
                "        \"video_name\": \"我在大理寺当宠物第1集\",\n" +
                "        \"total_video_count\": 22,\n" +
                "        \"duration\": 1726.85,\n" +
                "        \"urls\": {\n" +
                "            \"mp4\": {\n" +
                "                \"nor\": [\n" +
                "                    \"https://data.vod.itc.cn/?new=/31/104/F1MKZqVpTXiRK8f9zpGXsF.mp4&vid=4966861&plat=17&mkey=a_CWznNqMHvKEzlKopjg0WnRDrAyCX3a&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/103/196/j8pgFzDQJ8hrbl03Xy95QA.mp4&vid=4966861&plat=17&mkey=4jIM4vjeONjHpephA1WPoO9W7gSVMkA4&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/157/223/v59tOZo6o8ItAiAa6foCME.mp4&vid=4966861&plat=17&mkey=JXvQJsxYMvRmCz66ed_LdobxkNp9fN9P&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/226/49/2Cd9PwT2oIfQZjL8zwDSNF.mp4&vid=4966861&plat=17&mkey=hKYJWVc7gsmT1OOVeSYees_gGQmc4qr0&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/246/116/nJ6xheEfTZ67ADW6ff0DGA.mp4&vid=4966861&plat=17&mkey=IrF-AdkIFcRAPMe8CokwGRIpKkSgaXPx&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/208/76/ejYiWbSkQPO19k99ETR8TF.mp4&vid=4966861&plat=17&mkey=8voVyJkRS3Saefu2wZfIVmpvuDMF1v3Q&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ],\n" +
                "                \"hig\": [\n" +
                "                    \"https://data.vod.itc.cn/?new=/218/220/tuMb02iASLGI9Atg78TokL.mp4&vid=4966860&plat=17&mkey=D52rCNSxlOoW07q7fqq0olVcAbwrqisy&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/96/188/HBSANWdZQnWGOy5pvmqI7D.mp4&vid=4966860&plat=17&mkey=zYyZA92mRjmYhWzcvxkx9CxJW61Afaix&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/129/228/fbipY1lKTWS8SFP83Wqp2I.mp4&vid=4966860&plat=17&mkey=XA7Ra3fual4isFm_-G0QzEEeLUlztRpb&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/246/23/cNfcYbTWS9rcnI9F9wsmBC.mp4&vid=4966860&plat=17&mkey=o1l7yvwGuqCwx3TYORsqdaJsp4KqJneJ&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/145/179/SBA1qLNFS6uh1zulITwkxD.mp4&vid=4966860&plat=17&mkey=A1__bIQqM4vvtor9i4T5hYKRCVQYBmf3&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/98/122/uAEYlR7dT98hsCytiyugYI.mp4&vid=4966860&plat=17&mkey=iTsDQJr4gLXZTverx1TOp9WPbmug5kRk&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ],\n" +
                "                \"sup\": [\n" +
                "                    \"https://data.vod.itc.cn/?new=/104/61/6hS9NqE90D9x8cRz6tLFVg.mp4&vid=4966862&plat=17&mkey=Dx26BeTMmai9NJMuP7z5Q3SJa1ztIVjW&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/233/57/vhUc12uFQPWlBWdwpLAwyC.mp4&vid=4966862&plat=17&mkey=RvNWZgo0wRQAzgcY8AANX7G-LTV4_ZSa&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/56/102/2ZU3885REnjrb9kLVyj21C.mp4&vid=4966862&plat=17&mkey=aXtn3FKEJya60DvvewgSKyk8Tkj8YLxO&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/83/112/NcuwB6dWTbG2uTYrANfFXA.mp4&vid=4966862&plat=17&mkey=ydtluEu3ko6Qi2pptg8fksDhAupA1n8x&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/234/143/izVZE0DHSleC8ADRn4PwLG.mp4&vid=4966862&plat=17&mkey=vTM4NT4HQFDYDoI7n6auOIHKRY3VERB-&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\",\n" +
                "                    \"https://data.vod.itc.cn/?new=/28/176/ZVhPK9AUIrdeoDtNHMPREH.mp4&vid=4966862&plat=17&mkey=SVX3xL7Me9C_E_tLQi2UsJJ2mo8DVhhK&ch=tv&user=api&uid=190313191758A3JT&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"downloadUrl\": [\n" +
                "                [\n" +
                "                    \"https://data.vod.itc.cn/?k=tWaizEIORhcXRYcIkgBjwJusi5StH5mkZhYJUOyXl5ux6BgXpw&a=XfGFjpCGqAOL4p3Cj6O3XUaAz95d4EOLsHJlsUIAoD2sWY6sWYNsWYWSqM9Auh2BgF4OupoBNhWAe6blyK6e0YeeeLcAMBWbeVKlPLfvgDoVqTPcWh1sfJd4WFbOfK&vid=4966862&uid=190313191758A3JT&plat=17&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ]\n" +
                "            ],\n" +
                "            \"m3u8\": {\n" +
                "                \"nor\": [\n" +
                "                    \"http://hot.vrs.sohu.com/ipad4966861_4763934195469_7177182.m3u8?vid=4966861&uid=190313191758A3JT&plat=17&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ],\n" +
                "                \"hig\": [\n" +
                "                    \"http://hot.vrs.sohu.com/ipad4966860_4763934195469_7177181.m3u8?vid=4966860&uid=190313191758A3JT&plat=17&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ],\n" +
                "                \"sup\": [\n" +
                "                    \"http://hot.vrs.sohu.com/ipad4966862_4763934195469_7177183.m3u8?vid=4966862&uid=190313191758A3JT&plat=17&SOHUSVP=Mft4AXUxYHubK_HZ2aNMgNoKXwZ9VQ-0INCwCUxPSpk&pt=5&prod=h5&pg=1&eye=0&cv=1.0.0&qd=68000&src=11060001&ca=4&cateCode=101&_c=1&appid=tv\"\n" +
                "                ]\n" +
                "            }\n" +
                "        },\n" +
                "        \"album_name\": \"我在大理寺当宠物\",\n" +
                "        \"video_order\": 1,\n" +
                "        \"pay_type\": [\n" +
                "            0\n" +
                "        ],\n" +
                "        \"video_desc\": \"茹小岚头昏脑胀地睁开眼，发现自己趴在一阴暗的小巷子内，想出声但喉咙却梗住无法发出声音。她勉力撑起身体摇摇晃晃走着，突然感觉脚悬空被人从脖子后拎了起来“抓了只小肥猫看来今天晚餐可以加菜了。”茹小岚猛一转头竟看见一满脸横肉的屠夫垂涎地看着自己，另一只手握着亮晃晃的菜刀。\",\n" +
                "        \"hor_high_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_2ewGo_pic2.jpg\",\n" +
                "        \"tv_id\": 106352081,\n" +
                "        \"end_time\": 0,\n" +
                "        \"hor_w8_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_3o3Dv_pic24.jpg\",\n" +
                "        \"ver_small_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_70lS9_pic9.jpg\",\n" +
                "        \"video_type\": 1,\n" +
                "        \"site\": 1,\n" +
                "        \"ver_high_pic \": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_6491b_pic3.jpg\",\n" +
                "        \"video_big_pic\": \"https://photocdn.tv.sohu.com/img/20180921/vrs_1537496341953_106352081_7iuw0_pic6.jpg\",\n" +
                "        \"socore\": 7.4,\n" +
                "        \"ip_limit\": 0,\n" +
                "        \"original_video_url\": \"https://tv.sohu.com/v/MjAxODA5MjUvbjYwMDU5OTExNy5zaHRtbA==.html\",\n" +
                "        \"data_type\": 2\n" +
                "    },\n" +
                "    \"statusText\": \"OK\",\n" +
                "    \"mCode\": \"99935d3dcf8d6e7bc7410f7c1458c75f\",\n" +
                "    \"status\": 200\n" +
                "}";
        JSONObject objectMap = JSONObject.parseObject(s2);
        JSONObject dataMap = objectMap.getJSONObject("data");
        //视频源地址
        String sourceUrl = (String) dataMap.get("original_video_url");
        JSONObject urlMap = dataMap.getJSONObject("urls");
        log.info("sourceUrl" + sourceUrl);

        JSONObject playUrlMap = urlMap.getJSONObject("mp4");

        String j = playUrlMap.toJSONString();

        Map<String, Object> objectMap1 = JSONUtils.getObjectMap(j);

        List<String> datas = JSONUtils.getListString(objectMap1.get("nor").toString());

        log.info(datas.get(0));

    }

}
