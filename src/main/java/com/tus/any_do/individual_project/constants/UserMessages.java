package com.tus.any_do.individual_project.constants;


public class UserMessages {
	// Hiding default constructor
	private UserMessages() {}
	
	public static final String USER_ALREADY_EXISTS_MESSAGE(String username) {
		return "User with username " + username + " already exists.";
	}
}
