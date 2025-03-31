package com.tus.anyDo.IndividualProject.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.tus.anyDo.IndividualProject.exception.IncompleteUserDetailsException;
import com.tus.anyDo.IndividualProject.model.Role;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.impl.JwtService;

class JwtServiceTest {
	private static final String TEST_SECRET = "d60940f8702212104bbd24381f138a68d35699f51caf57aeb44951da755a9187";
	private final String username = "username123";
	private final String password = "password1";
	private final Role userRole = Role.ROLE_PROJECT_MANAGER;
	private final User validUserDetails = new User();
	private JwtService jwtService;
	
	@BeforeEach
	void setup() {
		jwtService = new JwtService();
		jwtService.setSecret(TEST_SECRET);
	}
	
	// ----- generateToken() ------
	@Test
	void testNoRoleSupplied() {
		validUserDetails.setUsername(username);
		validUserDetails.setPassword(password);
		
		Throwable e = assertThrows(IncompleteUserDetailsException.class, () -> {
			jwtService.generateToken(validUserDetails);
		});
		
		assertEquals("User role must be provided", e.getMessage());
	}
	
	@ParameterizedTest // Also covering extractUsername() and isTokenValid() - is this the best approach?
	@ValueSource(strings = {"testUsername1", "testUsername2", "testUsername3", "testUsername4", "otherUsername"})
	void testGenerateToken(String expectedUsername) {
		validUserDetails.setUsername(expectedUsername);
		validUserDetails.setPassword(password);
		validUserDetails.setRole(userRole);
		
		String token = jwtService.generateToken(validUserDetails);
		
		String actualUsername = jwtService.extractUsername(token);
		assertEquals(expectedUsername, actualUsername);
		assertTrue(jwtService.isTokenValid(token, validUserDetails));
	}
}
