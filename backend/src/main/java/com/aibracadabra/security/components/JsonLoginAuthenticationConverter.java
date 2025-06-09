package com.aibracadabra.security.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aibracadabra.model.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JsonLoginAuthenticationConverter
 * <p>
 * JSON 형식의 로그인 요청을 처리하는 AuthenticationConverter.
 * JSON 형식의 로그인 요청을 처리하기 위해 HttpServletRequest에서 LoginRequest 객체를 추출하여 UsernamePasswordAuthenticationToken 으로 변환하는 역할을 수행합니다.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JsonLoginAuthenticationConverter implements AuthenticationConverter {
  private final ObjectMapper mapper;

  @Override
  public Authentication convert(HttpServletRequest request) {
    try {
      if (request.getInputStream().available() > 1024 * 1024) {
        return null;
      }

      // 1) JSON 파싱
      LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

      // 2) 비인증 토큰 상태로 리턴
      return UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
    } catch (IOException e) {
      throw new AuthenticationServiceException("로그인 컨버팅중 예상치 못한 에러");
    }
  }

}
