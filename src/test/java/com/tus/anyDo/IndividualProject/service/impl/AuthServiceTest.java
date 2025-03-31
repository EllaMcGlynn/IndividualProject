package com.tus.anyDo.IndividualProject.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.exception.InvalidCredentialsException;
import com.tus.anyDo.IndividualProject.model.User;

class AuthServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthService authService;
	
	@BeforeEach
	void setup() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		authService = new AuthService(userRepository, passwordEncoder);
		
	}
	
	@Test
	void testUserDoesNotExist() {
		final String INVALID_USERNAME = "invalidUsername";
		final String PASSWORD = "password";
		when(userRepository.findByUsername(INVALID_USERNAME)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authService.authenticate(INVALID_USERNAME, PASSWORD);
		});
		
		assertEquals("Invalid username", e.getMessage());
		verify(passwordEncoder, new Times(0)).matches(any(), any());
	}
	
	@Test
	void testInvalidCredentials() {
		final String VALID_USERNAME = "validUsername";
		final String HASHED_VALID_PASSWORD = "hashedValidPassword";
		final String INVALID_PASSWORD = "invalidPassword";
		
		User user = new User();
		user.setUsername(VALID_USERNAME);
		user.setPassword(HASHED_VALID_PASSWORD);
		when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(INVALID_PASSWORD, HASHED_VALID_PASSWORD)).thenReturn(false);
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authService.authenticate(VALID_USERNAME, INVALID_PASSWORD);
		});
		
		assertEquals("Invalid password", e.getMessage());
		verify(userRepository, new Times(1)).findByUsername(VALID_USERNAME);
		verify(passwordEncoder, new Times(1)).matches(INVALID_PASSWORD, HASHED_VALID_PASSWORD);
	}
	
	
	@Test
	void testValidCredentials() throws InvalidCredentialsException {
		final String VALID_USERNAME = "validUsername";
		final String HASHED_VALID_PASSWORD = "hashedValidPassword";
		final String VALID_PASSWORD = "validPassword";
		
		User user = new User();
		user.setUsername(VALID_USERNAME);
		user.setPassword(HASHED_VALID_PASSWORD);
		when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(VALID_PASSWORD, HASHED_VALID_PASSWORD)).thenReturn(true);
		
		UserDetails userReturned = authService.authenticate(VALID_USERNAME, VALID_PASSWORD);
		
		assertEquals(VALID_USERNAME, userReturned.getUsername());
		assertEquals(HASHED_VALID_PASSWORD, userReturned.getPassword());
		verify(userRepository, new Times(1)).findByUsername(VALID_USERNAME);
		verify(passwordEncoder, new Times(1)).matches(VALID_PASSWORD, HASHED_VALID_PASSWORD);
	}
}
