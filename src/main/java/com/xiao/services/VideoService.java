package com.xiao.services;

import com.xiao.bean.VideoInfo;
import com.xiao.dao.VideoDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-05
 *
 */
@Service
@AllArgsConstructor
public class VideoService {
    private VideoDao videoDao;

    /**
     * 寻找所有类型数据
     *
     * @return
     */
    public List<VideoInfo> findAll() {
        return videoDao.findall();
    }

    /**
     * 获取一种类型type
     *
     * @param id
     * @return
     */
    public VideoInfo getById(Long id) {
        return videoDao.getById(id);
    }

    ;

    /**
     * 上传视频类型
     *
     * @return 返回主键
     */
    public Integer insertInfo(VideoInfo videoInfo) {
        return videoDao.insertInfo(videoInfo);
    }

    ;

    /**
     * 更新视频类型
     *
     * @param videoInfo
     * @return
     */
    public Integer updateInfo(VideoInfo videoInfo) {
        return videoDao.updateInfo(videoInfo);
    }

    /**
     * 删除视频
     * @param id 主键
     * @return
     */
    public Integer deleteInfo(Integer id) {
        return videoDao.deleteInfo(id);

    }
}
