package com.library.java.security;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class UserHeaderAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UserHeaderAuthenticationFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        final String username = request.getHeader("username");

        final String requestAuthorities = request.getHeader("authorities");
        final List<String> authorities = nonNull(requestAuthorities)
                ? Lists.newArrayList(Splitter.on(", ").split(requestAuthorities))
                : Collections.EMPTY_LIST;

        final List<SimpleGrantedAuthority> roles = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, roles);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain,
            final Authentication authenticationResult
    ) throws IOException, ServletException {
        final SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authenticationResult);
        SecurityContextHolder.setContext(newContext);
        filterChain.doFilter(request, response);
    }

}
