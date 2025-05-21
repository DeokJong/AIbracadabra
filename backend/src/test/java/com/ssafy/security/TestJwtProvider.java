package com.ssafy.security;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.service.MemberService;
import com.ssafy.security.service.JwtProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
		"spring.security.authentication.access-token-secret=qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm",
		"spring.security.authentication.refresh-token-secret=QWERTYUIOPASDFGHJKLZXCVBNM1234567890QWERTYUIOPASDFGHJKLZXCVBNM"
})
@Transactional
public class TestJwtProvider {
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private MemberService memberService;

	private Integer mockMemberMno;
	private final int notExistMemberMno = -1;

	@BeforeEach
	void setup() {
		Member test = Member.builder()
				.email("test@mock.com")
				.password("plain")
				.name("mock")
				.build();

		memberService.signUp(test);
		mockMemberMno = (Integer) memberService.getByEmail("test@mock.com").getMno();
	}

	@Test
	@DisplayName("주입 확인 테스트")
	void TestNull() {
		assertNotNull(memberService);
		assertNotNull(jwtProvider);
	}

	@Test
	@DisplayName("RefreshToken 적합성 확인")
	void TestRefreshToken() {
		String refreshToken = jwtProvider.generateRefreshToken(String.valueOf(mockMemberMno));

		assertNotNull(jwtProvider.parseRefreshToken(refreshToken));

	}

	@Test
	@DisplayName("AccessToken 적합성 확인")
	void TestAccessToken() {
		String refreshToken = jwtProvider.generateRefreshToken(String.valueOf(mockMemberMno));
		String accessToken = jwtProvider.renewAccessToken(refreshToken);

		assertNotNull(jwtProvider.parseAccessToken(accessToken));

	}

	@Test
	@DisplayName("parse 함수 적합성 확인")
	void TestParseFunction() {
		String refreshToken = jwtProvider.generateRefreshToken(String.valueOf(mockMemberMno));
		String accessToken = jwtProvider.renewAccessToken(refreshToken);

		assertThatExceptionOfType(SignatureException.class)
				.isThrownBy(() -> jwtProvider.parseAccessToken(refreshToken));
		assertThatExceptionOfType(SignatureException.class)
				.isThrownBy(() -> jwtProvider.parseRefreshToken(accessToken));
		assertNotNull(jwtProvider.parseRefreshToken(refreshToken));
		assertNotNull(jwtProvider.parseAccessToken(accessToken));
	}

	@Test
	@DisplayName("Token의 Subject 확인")
	void TestTokenSubject() {
		String refreshToken = jwtProvider.generateRefreshToken(String.valueOf(mockMemberMno));
		String accessToken = jwtProvider.renewAccessToken(refreshToken);

		Claims refreshClaims = jwtProvider.parseRefreshToken(refreshToken);
		Claims accessClaims = jwtProvider.parseAccessToken(accessToken);

		assertEquals(refreshClaims.getSubject(), accessClaims.getSubject());
		assertEquals(refreshClaims.getSubject(), String.valueOf(mockMemberMno));
	}

	@Test
	@DisplayName("찾을 수 없는 사람에 대한 요청")
	void TestRecordNotFound() {
		assertThatExceptionOfType(RecordNotFoundException.class)
		.isThrownBy(() -> jwtProvider.generateRefreshToken(String.valueOf(notExistMemberMno)));
	}
}
