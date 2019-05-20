package com.customer.java.config;

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
import java.util.List;
import java.util.stream.Collectors;

public class UserHeaderAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UserHeaderAuthenticationFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        final String username = request.getHeader("sub");

        final List<String> authorities =
                Lists.newArrayList(Splitter.on(", ").split(request.getHeader("authorities")));

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

    // A (temporary) class just to represent the user credentials
    private static class UserCredentials {

        private String username, password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
