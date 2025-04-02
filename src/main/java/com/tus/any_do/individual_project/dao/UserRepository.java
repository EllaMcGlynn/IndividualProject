package com.tus.any_do.individual_project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.any_do.individual_project.model.Role;
import com.tus.any_do.individual_project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(Role role);
}