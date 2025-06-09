package com.aibracadabra.security.service;

import io.jsonwebtoken.Claims;

public interface JwtProvider {

	/**
	 * 식별 키를 받아서 Refresh Token을 생성합니다.
	 * 
	 * @param identificationKey
	 * @return
	 */
	String generateRefreshToken(String identificationKey);

	/**
	 * Refresh Token을 파싱 한 후, 식별 키를 얻습니다. 이후 식별키를 이용해 권한을 얻고, Access Token을 생성합니다.
	 * 
	 * @param refreshToken
	 * @return
	 */
	String renewAccessToken(String refreshToken);

	/**
	 * Access Token을 파싱합니다. 만일, 유효하지 않다면 에러를 보냅니다.
	 * 
	 * @param token
	 * @return
	 */
	Claims parseAccessToken(String token);

	/**
	 * Refresh Token을 파싱합니다. 만일, 유효하지 않다면 에러를 보냅니다.
	 * 
	 * @param token
	 * @return
	 */
	Claims parseRefreshToken(String token);
}
