package com.customer.java.config;

import feign.codec.Decoder;
import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

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

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public FormEncoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Decoder feignDecoder(){
        Decoder decoder = (response, type) -> {
            if (type instanceof Class && MultipartFile.class.isAssignableFrom((Class) type)) {
                Collection<String> contentTypes = response.headers().get("content-type");
                String contentType = "application/octet-stream";
                if (contentTypes.size() > 0) {
                    String[] temp = new String[contentTypes.size()];
                    contentTypes.toArray(temp);
                    contentType = temp[0];
                }
                byte[] bytes = StreamUtils.copyToByteArray(response.body().asInputStream());
                InMemoryMultipartFile inMemoryMultipartFile = new InMemoryMultipartFile("file","", contentType,bytes);
                return inMemoryMultipartFile;
            }
            return new SpringDecoder(messageConverters).decode(response, type);
        };
        return new ResponseEntityDecoder(decoder);
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
