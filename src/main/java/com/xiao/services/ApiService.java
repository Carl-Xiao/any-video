package com.xiao.services;

import com.xiao.bean.Episode;
import com.xiao.bean.Video;
import com.xiao.dao.VideoDao;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Service
@Slf4j
public class ApiService {

    private final static String GUID = "";
    public final String cookie = "";
    @Autowired
    OkHttpUtils okHttpUtils;

    @Autowired
    VideoDao videoDao;

    public Video getInfo(String vid) {

        return null;
    }


    public Episode getVidRealUrlPart(String vid, String index) {
        //视频基本信息从数据库获取
        return null;
    }
}
