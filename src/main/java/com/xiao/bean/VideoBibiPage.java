package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2018-12-08
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoBibiPage {
    private Long cid;
    private Long page;
    private String part;
}
