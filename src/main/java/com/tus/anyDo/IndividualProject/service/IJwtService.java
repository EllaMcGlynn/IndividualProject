package com.tus.anyDo.IndividualProject.service;


import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.tus.anyDo.IndividualProject.exception.IncompleteUserDetailsException;

import io.jsonwebtoken.Claims;

public interface IJwtService {
	
	String generateToken(UserDetails user) throws IncompleteUserDetailsException;

	Claims extractAllClaims(String token);

	String extractUsername(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	boolean isTokenValid(String token, UserDetails userDetails);
}
