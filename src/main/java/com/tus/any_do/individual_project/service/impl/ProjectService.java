package com.tus.any_do.individual_project.service.impl;

import com.tus.any_do.individual_project.dao.ProjectRepository;
import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.dto.ProjectCreateRequest;
import com.tus.any_do.individual_project.dto.ProjectResponseDto;
import com.tus.any_do.individual_project.dto.ProjectUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToProjectException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;
import com.tus.any_do.individual_project.mapper.ProjectMapper;
import com.tus.any_do.individual_project.model.Project;
import com.tus.any_do.individual_project.model.User;
import com.tus.any_do.individual_project.service.IProjectService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService implements IProjectService {
	private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Cannot find User with username: ";
	private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService(UserRepository userRepository, ProjectRepository projectRepository) {
    	this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Project createProject(ProjectCreateRequest projectCreateRequest, String username) throws UserNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + username));
		
		Project project = new Project();
		ProjectMapper.toProject(projectCreateRequest, user, project);
		return projectRepository.save(project);
    }

    @Override
    public ProjectResponseDto getProjectById(long projectId, String username) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
    	userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + username));
    	
    	Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + projectId + " does not exist."));
		
		if (!project.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToProjectException("You do not have access to the project with id: " + projectId);
		}

        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        ProjectMapper.toProjectResponseDto(project, projectResponseDto);
        return projectResponseDto;
    }

	@Override
	@Transactional
	public void deleteProject(long projectId, String username) throws UserNotFoundException, UnauthorizedAccessToProjectException, ProjectNotFoundException{
		userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + username));
		
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + projectId + " does not exist."));
		
		if (!project.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToProjectException("You do not have access to the project with id: " + projectId);
		}
		
		projectRepository.deleteById(projectId);
	}

	@Override
	@Transactional
	public ProjectResponseDto updateProject(
			long projectId, 
			ProjectUpdateRequest projectUpdateRequest, 
			String username
	) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + username));
		
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + projectId + " does not exist."));

		if (!project.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToProjectException("You do not have access to the project with id: " + projectId);
		}
		
		// Copy details from projectUpdateRequest to project
		ProjectMapper.toProject(projectUpdateRequest, user, project);
		// Save updated details
		projectRepository.save(project);
		
		// Copy updated project details to ProjectResponseDto
		ProjectResponseDto projectResponseDto = new ProjectResponseDto();
		ProjectMapper.toProjectResponseDto(project, projectResponseDto);

		return projectResponseDto;
	}

	@Override
	public List<ProjectResponseDto> getAllMyProjects(String username) {
		return projectRepository.findByCreator_Username(username).stream().map((project) -> {
			ProjectResponseDto projectResponseDto = new ProjectResponseDto();
			ProjectMapper.toProjectResponseDto(project, projectResponseDto);
			return projectResponseDto;
		}).toList();
	}
	
}