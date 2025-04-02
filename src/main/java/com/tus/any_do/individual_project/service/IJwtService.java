package com.tus.any_do.individual_project.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.tus.any_do.individual_project.exception.IncompleteUserDetailsException;


public interface IJwtService {
	
	String generateToken(UserDetails user) throws IncompleteUserDetailsException;

	String extractUsername(String token);

	boolean isTokenValid(String token, UserDetails userDetails);
}
