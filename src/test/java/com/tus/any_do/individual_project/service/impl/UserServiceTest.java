package com.tus.any_do.individual_project.service.impl;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.dto.UserRegisterRequest;
import com.tus.any_do.individual_project.exception.UserAlreadyExistsException;
import com.tus.any_do.individual_project.model.Role;
import com.tus.any_do.individual_project.model.User;

class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	private UserService userService;
	
	@BeforeEach
	void setup() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		
		userService = new UserService(userRepository, passwordEncoder);
	}
	
	@Test
	void testUserAlreadyExistsThrown() {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_TEAMWORKER;
		final UserRegisterRequest userReq = new UserRegisterRequest();
		userReq.setUsername(USERNAME);
		userReq.setPassword(PASSWORD);
		userReq.setRole(role);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User()));
		
		Throwable e = assertThrows(UserAlreadyExistsException.class, () -> {
			userService.createUser(userReq);
		});
		assertEquals("User with username " + USERNAME + " already exists.", e.getMessage());
		verify(userRepository, new Times(0)).save(any());
	}
	
	@Test
	void testValidUserCreation() throws UserAlreadyExistsException {
		final String USERNAME = "username";
		final String PASSWORD = "password";
		final Role role = Role.ROLE_TEAMWORKER;
		final UserRegisterRequest userReq = new UserRegisterRequest();
		userReq.setUsername(USERNAME);
		userReq.setPassword(PASSWORD);
		userReq.setRole(role);
		
		when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
		
		userService.createUser(userReq);
		
		verify(userRepository, new Times(1)).save(any());
		
	}
}
