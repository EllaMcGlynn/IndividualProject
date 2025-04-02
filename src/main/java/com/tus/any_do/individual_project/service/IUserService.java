package com.tus.any_do.individual_project.service;

import com.tus.any_do.individual_project.dto.UserRegisterRequest;
import com.tus.any_do.individual_project.exception.UserAlreadyExistsException;

public interface IUserService {

	public void createUser(UserRegisterRequest user) throws UserAlreadyExistsException;
	
}
