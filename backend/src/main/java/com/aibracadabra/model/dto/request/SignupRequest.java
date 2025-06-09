package com.aibracadabra.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SignupRequest {
	@Schema(description = "회원 이름", example = "홍길동")
	String name;

	@Schema(description = "회원 이메일", example = "aibracadabra@aibracadabra.com")
	@Email
	String email;

	@Schema(description = "회원 비밀번호", example = "password123")
	String password;
	@Schema(description = "비밀번호 확인", example = "password123")
	String checkPassword;

	@Schema(description = "회원 권한", example = "USER")
	String role;
}
