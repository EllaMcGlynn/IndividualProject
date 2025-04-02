package com.tus.any_do.individual_project.service;

import java.util.List;

import com.tus.any_do.individual_project.dto.ProjectCreateRequest;
import com.tus.any_do.individual_project.dto.ProjectResponseDto;
import com.tus.any_do.individual_project.dto.ProjectUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToProjectException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;
import com.tus.any_do.individual_project.model.Project;

public interface IProjectService {

    Project createProject(ProjectCreateRequest projectCreateRequest, String username) throws UserNotFoundException;
    
    List<ProjectResponseDto> getAllMyProjects(String username) throws UserNotFoundException;

    ProjectResponseDto getProjectById(long projectId, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;

    ProjectResponseDto updateProject(long projectId, ProjectUpdateRequest projectUpdateRequest, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;
	
	void deleteProject(long projectId, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;
}
