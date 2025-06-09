package com.aibracadabra.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
	@Schema(description = "회원 이메일", example = "aibracadabra@aibracadabra.com")
	@Email
	private String email;

	@Schema(description = "회원 비밀번호", example = "password123")
	private String password;
}
