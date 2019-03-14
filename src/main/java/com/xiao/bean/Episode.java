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
    public String vid;
    public String sourceUrl;
    public String playUrl;
    public String episodeText;
}
