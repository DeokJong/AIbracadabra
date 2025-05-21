package com.ssafy.security.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUsernamePasswordAuthenticationFilter extends AuthenticationFilter {

  public JsonUsernamePasswordAuthenticationFilter(@Lazy @Qualifier("authenticationManager") AuthenticationManager authenticationManager,
                                                  @Qualifier("jsonLoginAuthenticationConverter") AuthenticationConverter authenticationConverter,
                                                  @Qualifier("jsonAuthenticationSuccessHandler") AuthenticationSuccessHandler authenticationSuccessHandler,
                                                  @Qualifier("jsonAuthenticationFailureHandler") AuthenticationFailureHandler authenticationFailureHandler,
                                                  @Value("${spring.security.authentication.login-url}") String loginUrl) {
    super(authenticationManager, authenticationConverter);
    setSuccessHandler(authenticationSuccessHandler);
    setFailureHandler(authenticationFailureHandler);
    setRequestMatcher(new AntPathRequestMatcher(loginUrl, "POST"));
  }

  @PostConstruct
  public void log() {
    log.debug("JsonUsernamePasswordAuthenticationFilter initialized");
    log.debug("Request URL: {}", getRequestMatcher());
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String contentType = request.getContentType();
    return contentType == null || !contentType.startsWith("application/json");
  }

  @Override
  protected boolean shouldNotFilterAsyncDispatch() {
    // 비동기 요청에 대해서는 필터링을 하지 않음
    return true;
  }

  @Override
  protected boolean shouldNotFilterErrorDispatch() {
    // 에러 요청에 대해서는 필터링을 하지 않음
    return true;
  }

}
