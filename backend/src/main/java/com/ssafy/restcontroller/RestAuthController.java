package com.ssafy.restcontroller;

import com.ssafy.model.dto.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Auth", description = "인증 API")
public interface RestAuthController {
	@Operation(summary = "로그인", description = "사용자 로그인 API")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "로그인 성공",
			content = @Content(
				schema = @Schema(implementation = Void.class)
			)
		),
		@ApiResponse(responseCode = "401", description = "인증되지 않음")
	})
	@ResponseBody
	@PostMapping("/login")
	void loginForSwagger(@RequestBody LoginRequest loginRequest);

	@Operation(summary = "로그인", description = "사용자 로그인 API")
	@PostMapping("/logout")
	void logoutForSwagger();


}
