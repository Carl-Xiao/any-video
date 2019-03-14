package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2019-03-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SohuEpisode {
    private String vid;
    private String playlistid;
    private String o_playlistId;
}
