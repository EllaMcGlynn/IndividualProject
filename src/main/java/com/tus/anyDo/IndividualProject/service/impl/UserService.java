package com.tus.anyDo.IndividualProject.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tus.anyDo.IndividualProject.constants.UserMessages;
import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.dto.UserRegisterRequest;
import com.tus.anyDo.IndividualProject.exception.UserAlreadyExistsException;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.IUserService;

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
