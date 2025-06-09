package com.aibracadabra.security.components;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aibracadabra.security.dto.CustomUserDetails;
import com.aibracadabra.security.service.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtProvider jwtTokenProvider;

	private @Value("${spring.security.authentication.access-token-expire-millis}") Long ACCESS_EXPIRE_MILLIS;
	private @Value("${spring.security.authentication.refresh-token-expire-millis}") Long REFRESH_EXPIRE_MILLIS;
	private @Value("${spring.security.authentication.access-token-name}") String ACCESS_TOKEN_NAME;
	private @Value("${spring.security.authentication.refresh-token-name}") String REFRESH_TOKEN_NAME;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(userDetails.getMember().getMno()));
		String accessToken = jwtTokenProvider.renewAccessToken(refreshToken);
		ResponseCookie refresh = ResponseCookie
				.from(REFRESH_TOKEN_NAME, refreshToken)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.domain("localhost")
				.maxAge(Duration.ofMillis(REFRESH_EXPIRE_MILLIS).getSeconds())
				.build();

		ResponseCookie access = ResponseCookie.from(ACCESS_TOKEN_NAME, accessToken)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.domain("localhost")
				.maxAge(Duration.ofMillis(ACCESS_EXPIRE_MILLIS).getSeconds())
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, access.toString());
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		onAuthenticationSuccess(request, response, authentication);
	}
}
