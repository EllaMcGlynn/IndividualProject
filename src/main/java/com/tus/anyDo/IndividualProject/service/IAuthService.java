package com.tus.anyDo.IndividualProject.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.tus.anyDo.IndividualProject.exception.InvalidCredentialsException;

public interface IAuthService {
	
	UserDetails authenticate(String username, String rawPassword) throws InvalidCredentialsException;
}
