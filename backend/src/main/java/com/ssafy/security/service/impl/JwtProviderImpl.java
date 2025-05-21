package com.ssafy.security.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ssafy.model.service.MemberService;
import com.ssafy.security.service.JwtProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProviderImpl implements JwtProvider {

	private final MemberService mService;

	private final Key ACCESS_TOKEN_KEY;
	private final Key REFRESH_TOKEN_KEY;
	private final long ACCESS_TOKEN_EXPIRE_MILLIS;
	private final long REFRESH_TOKEN_EXPIRE_MILLIS;

	public JwtProviderImpl(
			@Value("${spring.security.authentication.access-token-secret}") String accessSecret,
			@Value("${spring.security.authentication.refresh-token-secret}") String refreshSecret,
			@Value("${spring.security.authentication.access-token-expire-millis}") Long accessExpireMillis,
			@Value("${spring.security.authentication.refresh-token-expire-millis}") Long refreshExpireMillis,
			MemberService mService) {
		this.ACCESS_TOKEN_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
		this.REFRESH_TOKEN_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
		this.ACCESS_TOKEN_EXPIRE_MILLIS = accessExpireMillis;
		this.REFRESH_TOKEN_EXPIRE_MILLIS = refreshExpireMillis;
		this.mService = mService;
	}

	@Override
	public String generateRefreshToken(String identificationKey) {
		mService.get(Integer.parseInt(identificationKey));
		return Jwts.builder()
				.setSubject(identificationKey)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_MILLIS))
				.signWith(REFRESH_TOKEN_KEY, SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public String renewAccessToken(String refreshToken) {
		Claims refreshClaims = parseRefreshToken(refreshToken);
		String identificationKey = refreshClaims.getSubject();

		List<GrantedAuthority> roles = mService.getGrantedAuthorityByMno(Integer.parseInt(identificationKey));

		return Jwts.builder()
				.setSubject(identificationKey)
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_MILLIS))
				.signWith(ACCESS_TOKEN_KEY)
				.compact();
	}

	@Override
	public Claims parseAccessToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(ACCESS_TOKEN_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public Claims parseRefreshToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(REFRESH_TOKEN_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
