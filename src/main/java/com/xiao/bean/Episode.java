package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2019-03-06
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Episode {
    private String playlist_id;
    private String vid;
    private String sourceUrl;
    private String playUrl;
    private String episodeText;
    private String name;

}
