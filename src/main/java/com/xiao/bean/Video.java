package com.xiao.bean;

import lombok.Data;

/**
 * @author Carl-Xiao 2019-03-07
 */
@Data
public class Video {
    // 视频名称
    private String title;

    // 视频图片
    private String image;

    // 视频播放地
    private String playUrl;
    // 播放类型
    private String type;

    // 视频源地址
    private String value;

    // 其他信息
    private String other;
}
