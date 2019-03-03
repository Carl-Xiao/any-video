package com.xiao.controllers;

import com.xiao.bean.VideoBibiData;
import com.xiao.services.VideoBibiDataService;
import com.xiao.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.NumberUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carl-Xiao 2018-12-10
 * @description xiaobowen@newrank.cn
 */
@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {
    VideoBibiDataService videoBibiDataService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        log.warn("log index");
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, 0L, 4L);
        model.addAttribute("rmTvs", lists);

        return "index";
    }

    public static final Long pageSize = 8L;

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String admin(Model model) {
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, 0L, pageSize);
        model.addAttribute("rmTvs", lists);
        Long number = videoBibiDataService.countBibiData(videoBibiData);
        long size = number / pageSize;
        long mod = number % pageSize;
        if (mod > 0) {
            size = size + 1;
        }
        List<Integer> nums = CommonUtils.parseList((int) size);
        model.addAttribute("sizes", nums);
        return "admin";
    }


    @RequestMapping(value = "view/{page}", method = RequestMethod.GET)
    public String viewVideo(@PathVariable("page") Integer page, Model model) {
        VideoBibiData videoBibiData = videoBibiDataService.getInfoByPage(page);
        Long aid = videoBibiData.getAid();
        Long cid = videoBibiData.getCid();
        String title = videoBibiData.getPart();
        String url = "<iframe height='400' width='500' " +
                "src='//player.bilibili.com/player.html?aid=" + aid + "&cid=" + cid + "&page=" + page + "'scrolling='no' border='0'" +
                " frameborder='no' framespacing='0' allowfullscreen='true'> </iframe>";
        model.addAttribute("title", title);
        model.addAttribute("url", url);

        return "view";
    }

    public static void main(String[] args) {
        Integer[] sequence = NumberUtils.sequence(0, 12);
        Arrays.asList(sequence).parallelStream().forEach(t -> System.out.println(t));
    }

}
