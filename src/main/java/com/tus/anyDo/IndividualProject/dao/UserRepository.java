package com.tus.anyDo.IndividualProject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.anyDo.IndividualProject.model.Role;
import com.tus.anyDo.IndividualProject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(Role role);
}