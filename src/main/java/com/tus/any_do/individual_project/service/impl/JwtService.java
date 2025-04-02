package com.tus.any_do.individual_project.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.tus.any_do.individual_project.exception.IncompleteUserDetailsException;
import com.tus.any_do.individual_project.service.IJwtService;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@Setter
public class JwtService implements IJwtService {
	private static final String MISSING_ROLE_EXCEPTION = "User role must be provided";
	
	@Value("${security.jwt.secret}")
	private String secret;

	public String generateToken(UserDetails user) throws IncompleteUserDetailsException {
		String username = user.getUsername();
		Optional<? extends GrantedAuthority> roleOptional = user.getAuthorities().stream().findFirst();
		
		if (roleOptional.isEmpty()) {
			throw new IncompleteUserDetailsException(MISSING_ROLE_EXCEPTION);
		}
		
		GrantedAuthority role = roleOptional.get();
		
		return Jwts.builder().setSubject(username).claim("role", role.getAuthority())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1-hour expiration
				.signWith(getSigningKey(), SignatureAlgorithm.HS256) // SHA-256 signing
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername());
	}
	
	
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes); // Securely generates an HMAC SHA-256 key
	}
}