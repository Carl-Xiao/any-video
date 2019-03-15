package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2019-03-14
 * 视频基本信息与描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDrama {
    private String vid;
    private String name;
    private String showName;
    private String sort;
}
