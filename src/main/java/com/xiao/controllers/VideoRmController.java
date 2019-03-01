package com.xiao.controllers;

import com.xiao.bean.VideoBibiData;
import com.xiao.services.VideoBibiDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-11
 * @description xiaobowen@newrank.cn
 */
@RestController
@RequestMapping(value = "/rm")
@AllArgsConstructor
@Slf4j
public class VideoRmController {
    VideoBibiDataService videoBibiDataService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<VideoBibiData> pagInation(@RequestParam(value = "page") Long parm) {
        log.info("翻页" + parm);
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        long after = parm * 4;
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, after, 4L);
        return lists;
    }
}
