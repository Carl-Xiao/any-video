package com.xiao.services;

import com.alibaba.fastjson.JSONObject;
import com.xiao.bean.Video;
import com.xiao.utils.CommonUtils;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Service
@Slf4j
public class TencentService {
    public final String qqInfoUrl = "https://h5vv.video.qq.com/getinfo";
    private final static String GUID = "";
    public final String cookie = "";
    @Autowired
    OkHttpUtils okHttpUtils;


    public Video getInfo(String vid) {
        Video video = new Video();
        Map<String, String> vidInfo = new HashMap<>();
        vidInfo.put("vid", vid);
        vidInfo.put("platform", "10901");
        vidInfo.put("sdtfrom", "v1010");
        vidInfo.put("format", "10209");
        vidInfo.put("otype", "json");
        vidInfo.put("defn", "fhd");
        vidInfo.put("defaultfmt", "fhd");
        Response response = null;
        String result = "";
        try {
            response = okHttpUtils.doPostFormCall(qqInfoUrl, vidInfo);
            int code = response.code();
            if (code == 200) {
                result = response.body().string();
                result = result.replace("QZOutputJson=", "");
                result = result.substring(0, result.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!CommonUtils.isEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            initVideo(video, json);
        }
        log.info("视频基本信息："+video.toString());
        return video;
    }

    /**
     * 初始化视频信息
     */
    private void initVideo(Video video, JSONObject json) {
        JSONObject videoJson = json.getJSONObject("vl").getJSONArray("vi").getJSONObject(0);
        int random = RandomUtils.nextInt(0,3);
        String url = videoJson.getJSONObject("ul").getJSONArray("ui").getJSONObject(random).getString("url");
        String vkey = videoJson.getString("fvkey");
        String fn = videoJson.getString("fn");
        String file = fn.replace("mp4", "1.mp4");
        String title = videoJson.getString("ti");
        String firstPlayUrl = playUrl(url, file, vkey);
        String size = videoJson.getJSONObject("cl").getString("fc");
        video.setPlayUrl(firstPlayUrl);
        video.setImage("");
        video.setTitle(title);
        video.setType("qq");
        video.setOther(size);
    }

    /**
     * 片段播放地址
     */
    private String playUrl(String url, String part, String vkey) {
        return url + part + "?sdtfrom=" + "v1010" + "&guid=" + GUID + "&vkey=" + vkey;
    }


}
