package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.request.AccountLoginDto;
import com.nashtech.rookies.AssetManagement.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@EnableAutoConfiguration
@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	AuthService authService;

	@Operation(summary = "Login",
			description = "The API will return the Jwt Token, you can use it to access other API. " +
					"Please choose an account that have role is admin if you want to test all the API"
	)
	@PostMapping("/login")
	public ResponseEntity<?> LoginAccount(@Valid @RequestBody AccountLoginDto accountLoginDto) {
			
		return authService.loginAccount(accountLoginDto);
	}


}
