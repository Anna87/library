package com.library.java.security.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import static com.library.java.constants.Constants.AUTHORITIES;
import static com.library.java.constants.Constants.ROLE_ADMIN;

@Component
public class HeadersInterceptor implements RequestInterceptor{

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORITIES, ROLE_ADMIN);
    }
}
