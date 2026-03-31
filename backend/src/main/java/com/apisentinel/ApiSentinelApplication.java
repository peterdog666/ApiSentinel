package com.apisentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ApiSentinel 应用程序入口
 * 
 * @author ApiSentinel Team
 */
@SpringBootApplication
@EnableScheduling
public class ApiSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSentinelApplication.class, args);
    }
}
