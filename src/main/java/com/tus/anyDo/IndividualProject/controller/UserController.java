package com.tus.anyDo.IndividualProject.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.dto.UserResponseDto;
import com.tus.anyDo.IndividualProject.mapper.UserMapper;
import com.tus.anyDo.IndividualProject.model.Role;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserRepository userRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public List<UserResponseDto> getAllUsers() {
		return userRepository.findByRole(Role.ROLE_TEAMWORKER).stream()
				.map((user) -> {
					UserResponseDto userResponseDto = new UserResponseDto();
					UserMapper.toUserResponseDto(user, userResponseDto);
					return userResponseDto;
				}).toList();
	}
}
