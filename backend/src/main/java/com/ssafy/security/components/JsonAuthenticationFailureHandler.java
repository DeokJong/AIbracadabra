package com.ssafy.security.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.restcontroller.ResponseEntityHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler, ResponseEntityHelper {

  private final ObjectMapper mapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {

    ResponseEntity<?> entity = handleResponse("로그인 실패", HttpStatus.UNAUTHORIZED);

    int status = entity.getStatusCode().value();
    Object body = entity.getBody();

    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
    mapper.writeValue(response.getWriter(), body);
  }
}

