package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Carl-Xiao 2019-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiEpisode {
    private String title;
    private String url;
    private List<String> urls;
}
