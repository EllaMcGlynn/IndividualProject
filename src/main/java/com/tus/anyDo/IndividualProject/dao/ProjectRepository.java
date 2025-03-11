package com.tus.anyDo.IndividualProject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tus.anyDo.IndividualProject.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectName(String name);
    
    List<Project> findByCreator_Username(String username);
}
