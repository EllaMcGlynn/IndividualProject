package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.Role;

import lombok.Data;

@Data
public class UserRegisterRequest {
	private String username;
	private String password;
	private Role role;
}
