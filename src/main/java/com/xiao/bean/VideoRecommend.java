package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2019-03-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoRecommend {
    private String vid;
    private String name;
    private String showName;
    private String desc;
    private String img_url;
    private String play;
}
