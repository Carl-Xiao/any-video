package com.xiao.services;

import com.xiao.bean.VideoBibiData;
import com.xiao.utils.CommonUtils;
import com.xiao.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author Carl-Xiao 2019-03-04
 */
@Service
@Slf4j
public class IndexService {
    @Autowired
    VideoBibiDataService videoBibiDataService;

    @Autowired
    ApiService apiService;

    @Autowired
    OkHttpUtils okHttpUtils;

    public List<VideoBibiData> bibilShareRecommend() {
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, 0L, 4L);
        return lists;
    }

    public List<VideoBibiData> bibilShare(Long pagesize) {
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, 0L, pagesize);
        return lists;
    }

    public List<Integer> countBibilRm(Long pageSize) {
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        Long number = videoBibiDataService.countBibiData(videoBibiData);
        long size = number / pageSize;
        long mod = number % pageSize;
        if (mod > 0) {
            size = size + 1;
        }
        List<Integer> nums = CommonUtils.parseList((int) size);
        return nums;
    }

    public Model viewVideo(String md5, Model model) {
        VideoBibiData videoBibiData = videoBibiDataService.getInfoByPage(Integer.parseInt(md5));
        Long aid = videoBibiData.getAid();
        Long cid = videoBibiData.getCid();
        String title = videoBibiData.getPart();
        String url = "<iframe height='400' width='500' " +
                "src='//player.bilibili.com/player.html?aid=" + aid + "&cid=" + cid + "&page=" + md5 + "'scrolling='no' border='0'" +
                " frameborder='no' framespacing='0' allowfullscreen='true'> </iframe>";
        model.addAttribute("title", title);
        model.addAttribute("url", url);
        return model;
    }

    public Model qqViewVideo(String md5, Model model) {

        return model;
    }

    public Object sohuRecommend() {






        return null;
    }
}
