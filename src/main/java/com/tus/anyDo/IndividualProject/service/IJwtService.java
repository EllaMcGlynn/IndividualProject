package com.tus.anyDo.IndividualProject.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.tus.anyDo.IndividualProject.exception.IncompleteUserDetailsException;


public interface IJwtService {
	
	String generateToken(UserDetails user) throws IncompleteUserDetailsException;

	String extractUsername(String token);

	boolean isTokenValid(String token, UserDetails userDetails);
}
