package com.tus.any_do.individual_project.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tus.any_do.individual_project.constants.UserMessages;
import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.dto.UserRegisterRequest;
import com.tus.any_do.individual_project.exception.UserAlreadyExistsException;
import com.tus.any_do.individual_project.model.User;
import com.tus.any_do.individual_project.service.IUserService;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void createUser(UserRegisterRequest userReq) throws UserAlreadyExistsException {
    	User user = new User();
    	user.setUsername(userReq.getUsername());
        user.setPassword(passwordEncoder.encode(userReq.getPassword())); // Encrypt the password before saving
        user.setRole(userReq.getRole());
        
        Optional<User> userOptional = userRepository.findByUsername(userReq.getUsername());
        if (userOptional.isPresent()) {
        	throw new UserAlreadyExistsException(UserMessages.USER_ALREADY_EXISTS_MESSAGE(userReq.getUsername()));
        }
        userRepository.save(user);
    }
}
