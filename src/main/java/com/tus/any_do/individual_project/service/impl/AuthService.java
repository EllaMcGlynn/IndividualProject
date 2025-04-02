package com.tus.any_do.individual_project.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.exception.InvalidCredentialsException;
import com.tus.any_do.individual_project.model.User;
import com.tus.any_do.individual_project.service.IAuthService;

@Service
public class AuthService implements IAuthService {
	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
    @Override
    public UserDetails authenticate(String userName, String rawPassword) throws InvalidCredentialsException {
    	Optional<User> userOptional = userRepository.findByUsername(userName);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            	throw new InvalidCredentialsException("Invalid password");
            }
            
            return user;
        }
        
        throw new InvalidCredentialsException("Invalid username");
    }
}
