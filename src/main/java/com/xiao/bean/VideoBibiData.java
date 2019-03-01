package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2018-12-08
 * @description xiaobowen@newrank.cn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoBibiData {
    private Long aid;
    private Long cid;
    private Long page;
    private String part;
    private Integer type;

}
