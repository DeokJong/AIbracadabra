package com.aibracadabra.restcontroller;

import com.aibracadabra.model.dto.request.LoginRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class RestAuthControllerImpl implements RestAuthController {

	@Override
	public void loginForSwagger(LoginRequest loginRequest) {
	}

	@Override
	public void logoutForSwagger() {
	}

}
