package com.tus.anyDo.IndividualProject.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.tus.anyDo.IndividualProject.exception.IncompleteUserDetailsException;
import com.tus.anyDo.IndividualProject.service.IJwtService;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {
	private static final String SECRET_KEY = "4981f57ac11c2e710c94954e6e3620bc9930bc49404cf85294a7af28fed7ff4c"; // Secret key

	private static final String MISSING_ROLE_EXCEPTION = "User role must be provided";
	
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes); // Securely generates an HMAC SHA-256 key
	}

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

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}