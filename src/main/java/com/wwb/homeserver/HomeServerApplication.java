package com.wwb.homeserver;

import com.wwb.homeserver.config.feign.EnableCustomFeignRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author weibo
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.wwb"})
@EnableCustomFeignRequestInterceptor
//@EnabledCustomRibbonConfiguration
public class HomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeServerApplication.class, args);
    }

}
