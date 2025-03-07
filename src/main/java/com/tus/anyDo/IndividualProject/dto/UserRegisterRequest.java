package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.Role;

import lombok.Data;

@Data
public class UserRegisterRequest {
	private String username;
	private String password;
	private Role role;
}
