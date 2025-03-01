package com.tus.anyDo.IndividualProject.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.anyDo.IndividualProject.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}