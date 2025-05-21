// src/main/java/com/ssafy/security/filter/JsonLogoutFilter.java
package com.ssafy.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonLogoutFilter extends OncePerRequestFilter {

    @Value("${spring.security.authentication.access-token-name}")
    private String ACCESS_TOKEN_NAME;

    @Value("${spring.security.authentication.refresh-token-name}")
    private String REFRESH_TOKEN_NAME;

    @Value("${spring.security.authentication.logout-url}")
    private String LOGOUT_URL;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !LOGOUT_URL.equals(request.getServletPath())
               || !"POST".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        log.debug("로그아웃 처리: SecurityContext 초기화 및 쿠키 만료");


        SecurityContextHolder.clearContext();

        ResponseCookie clearAccess = ResponseCookie.from(ACCESS_TOKEN_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        ResponseCookie clearRefresh = ResponseCookie.from(REFRESH_TOKEN_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
