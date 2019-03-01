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
 * @author Carl-Xiao 2018-12-19
 *
 */
@RestController
@RequestMapping(value = "admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    VideoBibiDataService videoBibiDataService;

    @RequestMapping(value = "rm", method = RequestMethod.POST)
    public List<VideoBibiData> pagInation(@RequestParam(value = "page") Long parm, @RequestParam(value = "pageSize") Long pageSize) {
        VideoBibiData videoBibiData = new VideoBibiData();
        videoBibiData.setType(0);
        if (parm < 0) {
            return null;
        }
        long after = parm * pageSize;
        List<VideoBibiData> lists = videoBibiDataService.findAll(videoBibiData, after, pageSize);
        return lists;
    }


}
