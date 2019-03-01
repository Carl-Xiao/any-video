package com.xiao.services;

import com.xiao.bean.Page;
import com.xiao.bean.VideoBibiData;
import com.xiao.dao.VideoBibiDataDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carl-Xiao 2018-12-08
 *
 */
@Service
@AllArgsConstructor
public class VideoBibiDataService {
    VideoBibiDataDao videoBibiDataDao;

    public List<VideoBibiData> findAll(VideoBibiData videoBibiData, Long start, Long size) {
        Page<VideoBibiData> page = new Page();
        page.setPrev(start);
        page.setAfter(size);
        page.setData(videoBibiData);
        return videoBibiDataDao.findAll(page);
    }

    public Integer inserIntoVideoBibi(VideoBibiData videoBibiData) {
        return videoBibiDataDao.inserIntoVideoBibi(videoBibiData);
    }

    public VideoBibiData getInfoByPage(Integer page) {
        return videoBibiDataDao.getInfoByPage(page);
    }


    public Long countBibiData(VideoBibiData videoBibiData) {
        return videoBibiDataDao.countBibiData(videoBibiData);
    }


}
