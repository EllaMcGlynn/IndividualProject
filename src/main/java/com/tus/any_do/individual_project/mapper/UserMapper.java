package com.tus.any_do.individual_project.mapper;

import com.tus.any_do.individual_project.dto.UserResponseDto;
import com.tus.any_do.individual_project.model.User;

public class UserMapper {
	
	public static final void toUserResponseDto(User user, UserResponseDto userResponseDto) {
		userResponseDto.setId(user.getId());
		userResponseDto.setUsername(user.getUsername());
	}
}
