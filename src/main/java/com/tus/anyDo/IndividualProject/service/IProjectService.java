package com.tus.anyDo.IndividualProject.service;

import java.util.List;

import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.dto.ProjectResponseDto;
import com.tus.anyDo.IndividualProject.dto.ProjectUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.ProjectNotFoundException;
import com.tus.anyDo.IndividualProject.exception.UnauthorizedAccessToProjectException;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.model.Project;

public interface IProjectService {

    Project createProject(ProjectCreateRequest projectCreateRequest, String username) throws UserNotFoundException;
    
    List<ProjectResponseDto> getAllMyProjects(String username) throws UserNotFoundException;

    ProjectResponseDto getProjectById(long projectId, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;

    ProjectResponseDto updateProject(long projectId, ProjectUpdateRequest projectUpdateRequest, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;
	
	void deleteProject(long projectId, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException;
}
