package com.library.java.config;

import feign.codec.Decoder;
import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RequiredArgsConstructor
@Configuration
public class ClientConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

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
}
