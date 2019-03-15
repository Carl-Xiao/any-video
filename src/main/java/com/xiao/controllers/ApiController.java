package com.xiao.controllers;

import com.xiao.bean.Episode;
import com.xiao.services.ApiService;
import com.xiao.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

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

        String url =  episode.getPlayUrl();

        Map<String,Object> objectMap1 = JSONUtils.getObjectMap(url);

        List<String> datas = JSONUtils.getListString(objectMap1.get("nor").toString());

        model.addAttribute("url",datas.get(0));
        model.addAttribute("urls",datas);
        model.addAttribute("source",episode);
        model.addAttribute("episodes",apiService.epsiodes(play));
        model.addAttribute("play",play);

        return "sohuView";
    }


}
