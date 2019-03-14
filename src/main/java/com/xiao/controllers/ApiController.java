package com.xiao.controllers;

import com.xiao.bean.Episode;
import com.xiao.services.ApiService;
import com.xiao.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Controller
@Slf4j
public class ApiController {
    @Autowired
    ApiService apiService;

    @RequestMapping(value = "/api/sohu/{play}/{vid}", method = {RequestMethod.GET, RequestMethod.POST})
    public String getVidRealUrlIndex(@PathVariable("vid") String vid,@PathVariable("play") String play, Model model) {
        Episode episode = apiService.getInfo(vid);

        model.addAttribute("source",episode);
        model.addAttribute("episodes",CommonUtils.parseList(apiService.epsiodes(play)));
        return "sohuView";
    }


}
