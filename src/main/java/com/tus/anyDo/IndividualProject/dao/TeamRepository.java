package com.tus.anyDo.IndividualProject.dao;

import com.tus.anyDo.IndividualProject.model.Team;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByTeamName(String teamName);
    
}
