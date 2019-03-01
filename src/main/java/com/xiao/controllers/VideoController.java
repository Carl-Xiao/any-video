package com.xiao.controllers;

import com.xiao.bean.VideoInfo;
import com.xiao.services.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-04
 * @description xiaobowen@newrank.cn
 */
@Api(value = "视频上传类型")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("video")
public class VideoController {
    VideoService videoService;

    @ApiOperation(value = "查询所有", notes = "查询当前所有文档类型")
    @RequestMapping(value = "/findall", method = RequestMethod.GET)
    public List<VideoInfo> findall() {
        return videoService.findAll();
    }

    @ApiOperation(value = "插入一条数据")
    @RequestMapping(value = "insertInfo", method = RequestMethod.POST)
    public Integer insertInfo(VideoInfo videoInfo) {
        Integer is = videoService.insertInfo(videoInfo);
        log.info("插入表是否" + is);
        log.info("获得主键" + videoInfo.getId());
        return is;
    }
    @ApiOperation(value = "更新一条信息", notes = "update")
    @RequestMapping(value = "updateInfo", method = RequestMethod.POST)
    public Integer updateInfo(VideoInfo videoInfo) {
        Integer is = videoService.updateInfo(videoInfo);
        return is;
    }
    @ApiOperation(value = "删除一条文档信息", notes = "delete")
    @RequestMapping(value = "deleteInfo", method = RequestMethod.POST)
    public Integer deleteInfo(@RequestParam(value = "id") Integer id) {
        Integer is = videoService.deleteInfo(id);
        return is;
    }


}
