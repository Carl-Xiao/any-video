package com.xiao.controllers;

import com.xiao.bean.Video;
import com.xiao.services.TencentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Carl-Xiao 2019-03-06
 */
@RestController
public class TencentController {
    @Autowired
    TencentService tencentService;

    @RequestMapping("/api/qq/{vid}")
    public Video getVidRealUrl(@PathVariable("vid") String vid) {
        return tencentService.getInfo(vid);
    }


}
