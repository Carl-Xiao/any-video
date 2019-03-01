package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2018-12-04
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfo {
    private Long id;
    private String name;
    private String head_img_url;
    private String play_url;
    private Long type;
    private Integer status;
    private String type_name;

}
