package com.tus.anyDo.IndividualProject.mapper;

import com.tus.anyDo.IndividualProject.dto.UserResponseDto;
import com.tus.anyDo.IndividualProject.model.User;

public class UserMapper {
	
	public static final void toUserResponseDto(User user, UserResponseDto userResponseDto) {
		userResponseDto.setId(user.getId());
		userResponseDto.setUsername(user.getUsername());
	}
}
