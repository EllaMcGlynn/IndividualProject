package com.tus.any_do.individual_project.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.tus.any_do.individual_project.exception.InvalidCredentialsException;

public interface IAuthService {
	
	UserDetails authenticate(String username, String rawPassword) throws InvalidCredentialsException;
}
