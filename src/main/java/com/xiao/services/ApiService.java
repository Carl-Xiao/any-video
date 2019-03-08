package com.xiao.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiao.bean.Episode;
import com.xiao.bean.Video;
import com.xiao.dao.VideoDao;
import com.xiao.utils.CommonUtils;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
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
public class ApiService {
    public final String qqInfoUrl = "https://h5vv.video.qq.com/getinfo";
    public final String qqInfoKey = "http://h5vv.video.qq.com/getkey";

    private final static String GUID = "";
    public final String cookie = "";
    @Autowired
    OkHttpUtils okHttpUtils;

    @Autowired
    VideoDao videoDao;

    public Video getInfo(String vid) {
        Video video = videoDao.getVideoPlayInfo(vid);
        if (video != null) {
            log.info("已存在视频基本信息");
            return video;
        }
        video = new Video();
        video.setVid(vid);
        Map<String, String> vidInfo = new HashMap<>();
        vidInfo.put("vid", vid);
//        vidInfo.put("platform", "10901");
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
        log.info("执行结果"+result);
        if (!CommonUtils.isEmpty(result)) {
            JSONObject json = JSONObject.parseObject(result);
            initVideo(video, json);
        }
        log.info("视频基本信息：" + video.toString());
        int insertVideo = videoDao.insertVideo(video);
        log.info("插入视频记录" + insertVideo);
        return video;
    }

    /**
     * 初始化视频信息
     */
    private void initVideo(Video video, JSONObject json) {
        JSONObject videoJson = json.getJSONObject("vl").getJSONArray("vi").getJSONObject(0);
        JSONArray jsonArray = videoJson.getJSONObject("ul").getJSONArray("ui");
        String url = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int vt = Integer.parseInt(jsonObject.getString("vt"));
            if (vt > 200) {
                url = jsonObject.getString("url");
                break;
            }
        }
        String vkey = videoJson.getString("fvkey");
        String fn = videoJson.getString("fn");
        String[] formats = fn.split("\\.");
        String format = formats[1];
        String file = fn.replace("mp4", "1.mp4");
        String title = videoJson.getString("ti");
        String firstPlayUrl = playUrl(url, file, vkey);
        String size = videoJson.getJSONObject("cl").getString("fc");
        video.setPlayUrl(firstPlayUrl);
        video.setTitle(title);
        video.setType("qq");
        video.setPart(size);
        video.setFn(fn);
        video.setFormat(format);
        video.setValue(url);
    }

    /**
     * 片段播放地址
     */
    private String playUrl(String url, String part, String vkey) {
        return url + part + "?sdtfrom=" + "v1010" + "&guid=" + GUID + "&vkey=" + vkey;
    }

    public Episode getVidRealUrlPart(String vid, String index) {
        //视频基本信息从数据库获取
        Video video = getInfo(vid);
        String fileName = video.getFn();
        String format = video.getFormat().replace("p", "10");
        String file = fileName.replace(".mp4", index + ".mp4");
        log.info("视频基本信息文件" + file);

        String url = video.getValue();
        Map<String, String> parms = new HashMap<>();
        parms.put("vid", vid);
        parms.put("platform", "10901");
        parms.put("sdtfrom", "v1010");
        parms.put("format", format);
        parms.put("otype", "json");
        parms.put("filename", file);
        Response response = null;
        String videoKey = "";
        try {
            response = okHttpUtils.doPostFormCall(qqInfoKey, parms);
            int code = response.code();
            log.info("code:" + code);
            String result = response.body().string();
            result = result.replace("QZOutputJson=", "");
            System.out.println(result);
            result = result.substring(0, result.length() - 1);
            videoKey = JSONObject.parseObject(result).getString("key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Episode episode = new Episode();
        String playUrl = playUrl(url,file,videoKey);
        episode.setUrl(playUrl);
        return episode;
    }
}
