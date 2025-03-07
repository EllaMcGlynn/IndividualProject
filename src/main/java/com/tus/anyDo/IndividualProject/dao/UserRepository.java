package com.tus.anyDo.IndividualProject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.anyDo.IndividualProject.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}