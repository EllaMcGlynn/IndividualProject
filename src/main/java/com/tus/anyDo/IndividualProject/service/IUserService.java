package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.dto.UserRegisterRequest;
import com.tus.anyDo.IndividualProject.exception.UserAlreadyExistsException;

public interface IUserService {

	public void createUser(UserRegisterRequest user) throws UserAlreadyExistsException;
	
}
