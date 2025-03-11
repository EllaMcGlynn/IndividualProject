package com.tus.anyDo.IndividualProject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.mapper.ProjectMapper;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.IJwtService;
import com.tus.anyDo.IndividualProject.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	private ProjectRepository projectRepository;
	private IJwtService jwtService;
	private IUserService userService;
	
	public ProjectController(ProjectRepository projectRepository, IJwtService jwtService, IUserService userService) {
		this.projectRepository = projectRepository;
		this.jwtService = jwtService;
		this.userService = userService;
	}
	
	@GetMapping
	public List<Project> getProjects(@RequestHeader("Authorization") String token) {
		String username = jwtService.extractUsername(token.substring(7));
		return projectRepository.findByCreator_Username(username);
	}
	
	
	@PostMapping
	public long createProject(@RequestHeader("Authorization") String token, @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws UserNotFoundException {
		String username = jwtService.extractUsername(token.substring(7));
		User user = userService.getUserByUsername(username);
		
		Project project = new Project();
		ProjectMapper.toProject(projectCreateRequest, user, project);
		projectRepository.save(project);
		
		return project.getId();
	}
}
