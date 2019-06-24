package com.library.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
//@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
