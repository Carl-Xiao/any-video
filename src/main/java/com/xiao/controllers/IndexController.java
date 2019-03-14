package com.xiao.controllers;

import com.xiao.services.IndexService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Carl-Xiao 2018-12-10
 * @description xiaobowen@newrank.cn
 */
@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {
    IndexService indexService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("rmTvs", indexService.bibilShareRecommend());
        model.addAttribute("sohuTvs", indexService.sohuRecommend());
        return "index";
    }

    public static final Long pageSize = 8L;

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String admin(Model model) {
        model.addAttribute("rmTvs", indexService.bibilShare(pageSize));
        model.addAttribute("sizes", indexService.countBibilRm(pageSize));
        return "admin";
    }
    @RequestMapping(value = "bibil/{type}", method = RequestMethod.GET)
    public String bibilViewVideo(@PathVariable("type") String type,Model model) {
        indexService.viewVideo(type, model);
        return "view";
    }

}
