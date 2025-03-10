package com.tus.anyDo.IndividualProject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tus.anyDo.IndividualProject.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectName(String name);
}
