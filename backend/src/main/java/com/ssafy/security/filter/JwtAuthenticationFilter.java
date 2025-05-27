package com.ssafy.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.service.MemberService;
import com.ssafy.restcontroller.ResponseEntityHelper;
import com.ssafy.security.dto.CustomUserDetails;
import com.ssafy.security.service.JwtProvider;
import com.ssafy.util.CookieUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements ResponseEntityHelper {

	private final JwtProvider jwtProvider;
	private final MemberService memberService;
	private final ObjectMapper mapper;

	private @Value("${spring.security.authentication.access-token-name}") String ACCESS_TOKEN_NAME;
	private @Value("${spring.security.authentication.refresh-token-name}") String REFRESH_TOKEN_NAME;
	private @Value("${spring.security.authentication.access-token-expire-millis}") Long ACCESS_TOKEN_EXPIRE_MILLIS;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String accessToken = (String) CookieUtil.extractCookieValue(ACCESS_TOKEN_NAME, request);
			String refreshToken = (String) CookieUtil.extractCookieValue(REFRESH_TOKEN_NAME, request);

			// 1) Access Token 에서 Claims 추출 시도.
			Claims accessClaims = null;
			try {
				if (accessToken != null) {
					accessClaims = jwtProvider.parseAccessToken(accessToken);
				}
			} catch (ExpiredJwtException expEx) {
				// 토큰이 만료된 경우
				writeError(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
			} catch (JwtException badEx) {
				// 토큰이 위조된 경우
				SecurityContextHolder.clearContext();
				writeError(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
				return;
			}

			// 2) accessClaims가 null 인 경우 Refresh Token 에서 갱신 시도.
			if (accessClaims == null && refreshToken != null) {
				try {
					jwtProvider.parseRefreshToken(refreshToken);
					String newAccessToken = jwtProvider.renewAccessToken(refreshToken);
					accessClaims = jwtProvider.parseAccessToken(newAccessToken);

					response.setHeader(HttpHeaders.SET_COOKIE,
							ResponseCookie.from(ACCESS_TOKEN_NAME, newAccessToken)
									.httpOnly(true)
									.secure(true)
									.path("/")
									.maxAge(ACCESS_TOKEN_EXPIRE_MILLIS / 1000)
									.build().toString());

				} catch (JwtException e) {
					SecurityContextHolder.clearContext();
					filterChain.doFilter(request, response);
					return;
				}
			}

			// 3) accessClaims를 이용해 인증된 Authentication을 SecurityContext에 저장함.
			if (accessClaims != null) {
				String userId = accessClaims.getSubject();
				
				CustomUserDetails user = new CustomUserDetails(memberService.get(Integer.parseInt(userId)));
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				log.debug("Authentication: {}", auth);
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

			filterChain.doFilter(request, response);
			return ;
		} catch (ExpiredJwtException ex) {
			// 1) 토큰 만료 → 401 리턴
			writeError(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");

        } catch (JwtException ex) {
			// 2) 위조·파싱 실패 → 401 리턴
			writeError(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");

        } catch (RecordNotFoundException ex) {
			// 3) DB 조회 실패(사용자 없음) → 401 리턴
			writeError(response, HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.");

        } catch (Exception ex) {
			// 5) 기타(DB 에러 등) → 500 리턴
			log.error("인증 필터 처리 중 예외", ex);
			writeError(response, HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");
        }

	}

	// TODO 이거 로직 손댐
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getServletPath();
	    // 1) v1 이 아닌 모든 요청 -> 필터링 안 함
	    if (!path.startsWith("/api/v1")) {
	        return true;
	    }
	    // 2) 이미지 조회 API 만 필터링 안 함
	    if (request.getMethod().equals("GET") && path.startsWith("/api/v1/board/images/")) {
	        return true;
	    }
	    // 3) 그 외 모든 /api/v1/** 요청은 인증 로직 실행
	    return false;
		
	}

	private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
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
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
		response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

		ResponseEntity<?> entity = handleResponse(message, status);

		int statusNumber = entity.getStatusCode().value();
		Object body = entity.getBody();

		response.setStatus(statusNumber);
		response.setContentType("application/json;charset=UTF-8");
		mapper.writeValue(response.getWriter(), body);
	}

}
