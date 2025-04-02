package com.tus.any_do.individual_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.tus.any_do.individual_project.dto.ProjectCreateRequest;
import com.tus.any_do.individual_project.dto.ProjectResponseDto;
import com.tus.any_do.individual_project.dto.ProjectUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToProjectException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;
import com.tus.any_do.individual_project.service.IJwtService;
import com.tus.any_do.individual_project.service.IProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	private IJwtService jwtService;
	private IProjectService projectService;

	public ProjectController(IJwtService jwtService, IProjectService projectService) {
		this.jwtService = jwtService;
		this.projectService = projectService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public List<ProjectResponseDto> getProjects(@RequestHeader("Authorization") String token) throws UserNotFoundException {
		String username = jwtService.extractUsername(token.substring(7));
		return projectService.getAllMyProjects(username);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public long createProject(
			@RequestHeader("Authorization") String token,
			@Valid @RequestBody ProjectCreateRequest projectCreateRequest
	) throws UserNotFoundException {
		String username = jwtService.extractUsername(token.substring(7));
		return projectService.createProject(projectCreateRequest, username).getId();
	}
	
	@DeleteMapping("/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ResponseEntity<Void> deleteProject(
			@RequestHeader("Authorization") String token,
			@PathVariable("projectId") long projectId
	) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		String username = jwtService.extractUsername(token.substring(7));
		projectService.deleteProject(projectId, username);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
	
	@PutMapping("/update/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ProjectResponseDto updateProject(
			@RequestHeader("Authorization") String token,
			@PathVariable Long projectId, 
			@Valid @RequestBody ProjectUpdateRequest projectUpdateRequest
	) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {
		String username = jwtService.extractUsername(token.substring(7));
		return projectService.updateProject(projectId, projectUpdateRequest, username);

	}
	
	@GetMapping("/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ProjectResponseDto getProjectById(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long projectId
    ) throws UserNotFoundException, ProjectNotFoundException, UnauthorizedAccessToProjectException {    
        String username = jwtService.extractUsername(token.substring(7));
        return projectService.getProjectById(projectId, username);
    }
}
