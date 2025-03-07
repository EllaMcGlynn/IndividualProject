package com.tus.anyDo.IndividualProject.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.exception.InvalidCredentialsException;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.IAuthService;

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
