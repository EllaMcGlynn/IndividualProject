package com.tus.any_do.individual_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.any_do.individual_project.dto.LoginRequest;
import com.tus.any_do.individual_project.dto.LoginResponse;
import com.tus.any_do.individual_project.dto.UserRegisterRequest;
import com.tus.any_do.individual_project.exception.InvalidCredentialsException;
import com.tus.any_do.individual_project.exception.UserAlreadyExistsException;
import com.tus.any_do.individual_project.service.IAuthService;
import com.tus.any_do.individual_project.service.IJwtService;
import com.tus.any_do.individual_project.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class SecurityController {
	private IUserService userService;
	private IAuthService authService;
	private IJwtService jwtService;
	
	public SecurityController(IUserService userService, IAuthService authService, IJwtService jwtService) {
		this.userService = userService;
		this.authService = authService;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws InvalidCredentialsException {
		UserDetails user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
		String token = jwtService.generateToken(user);
		
		LoginResponse response = new LoginResponse();
		response.setJwt(token);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) throws UserAlreadyExistsException {
		userService.createUser(registerRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
