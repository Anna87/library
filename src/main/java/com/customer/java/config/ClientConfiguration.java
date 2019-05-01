package com.customer.java.config;

import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    /*@Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    /*
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }*/

    @Bean
    public FormEncoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
/*
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
    */
    /*
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            //requestTemplate.header("user", "ajeje");
            //requestTemplate.header("password", "brazof");
            requestTemplate.header("Accept", "application/json");
        };
    }*/
}
