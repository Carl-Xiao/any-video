package com.xiao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Carl-Xiao 2018-12-04
 *
 */
@SpringBootApplication
@EnableScheduling
public class XiaoAppliction {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(XiaoAppliction.class);
        app.run(args);
    }

}
