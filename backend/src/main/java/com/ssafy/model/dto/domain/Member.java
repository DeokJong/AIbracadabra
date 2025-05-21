package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 도메인 객체")
public class Member {

	@JsonProperty(access = Access.READ_ONLY)
	@Schema(description = "회원 번호", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	private int mno;

	@Schema(description = "회원 이름", example = "홍길동")
	private String name;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Schema(description = "회원 비밀번호", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
	private String password;

	@Schema(description = "회원 이메일", example = "user@example.com")
	private String email;

	@JsonProperty(access = Access.READ_ONLY)
	@Schema(description = "회원 권한", example = "ROLE_USER", accessMode = Schema.AccessMode.READ_ONLY)
	private String role;
}
