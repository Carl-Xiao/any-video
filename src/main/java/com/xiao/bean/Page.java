package com.xiao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页工具类
 * @author Carl-Xiao 2018-12-11
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {
    private T data;
    private List<T> datas;
    private Long prev;
    private Long after;
    private Long limit;

}
