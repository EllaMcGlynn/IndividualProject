package com.tus.anyDo.IndividualProject.services;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tus.anyDo.IndividualProject.models.User;
import com.tus.anyDo.IndividualProject.repos.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);

		if (userOptional.isEmpty()) {
			throw new UsernameNotFoundException("Can't find user.");
		}

		return userOptional.get();
	}
}
