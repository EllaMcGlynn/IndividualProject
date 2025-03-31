package com.tus.anyDo.IndividualProject.service.impl;

import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.dao.UserRepository;
import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.dto.ProjectResponseDto;
import com.tus.anyDo.IndividualProject.dto.ProjectUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.ProjectNotFoundException;
import com.tus.anyDo.IndividualProject.exception.UnauthorizedAccessToProjectException;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.mapper.ProjectMapper;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.IProjectService;

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