package com.xiao.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OKHttpProperty {
    private int connectTimeout = 60;
    private int readTimeOut = 100;
    private int writeTimeout = 60;
    private int maxIdleConnections = 5000;
    private int keepAliveDuration = 40;
    private boolean retryOnConnectionFailure = true;
}
