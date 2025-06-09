package com.aibracadabra.security.config;

import com.aibracadabra.security.filter.JsonLogoutFilter;
import com.aibracadabra.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.aibracadabra.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class FilterChainConfig {

	private final JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JsonLogoutFilter jsonLogoutFilter;

	@Bean
	SecurityFilterChain restApiFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(AbstractHttpConfigurer::disable);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterAt(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jsonLogoutFilter, AuthenticationFilter.class);
		http.addFilterAt(jwtAuthenticationFilter, AuthenticationFilter.class);
		http.cors(Customizer.withDefaults());

		http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
			// ==== 공지사항 ====
			.requestMatchers(POST, "/api/v1/notice/**").hasRole("ADMIN")
			.requestMatchers(PUT, "/api/v1/notice/**").hasRole("ADMIN")
			.requestMatchers(DELETE, "/api/v1/notice/**").hasRole("ADMIN")
			.requestMatchers(GET, "/api/v1/notice/**").permitAll()
	      .requestMatchers(GET, "/api/v1/board/images/**").permitAll()

			// ==== 회원 ====
			.requestMatchers(GET, "/api/v1/member/me").hasRole("USER")
			.requestMatchers(PUT, "/api/v1/member/**").hasRole("USER")
			.requestMatchers(DELETE, "/api/v1/member/**").hasRole("USER")

			// ==== 게시판 ====
			.requestMatchers(POST, "/api/v1/board/**").hasRole("USER")
			.requestMatchers(PUT, "/api/v1/board/**").hasRole("USER")
			.requestMatchers(DELETE, "/api/v1/board/**").hasRole("USER")
			.requestMatchers(GET, "/api/v1/board/**").permitAll()

			// ==== 여행 계획 ====
			.requestMatchers("/api/v1/plans/**").hasRole("USER")

			// 그 외 요청은 인증 불필요
			.anyRequest().permitAll());


		return http.build();
	}
}
