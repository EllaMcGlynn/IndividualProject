package com.tus.anyDo.IndividualProject.controller;

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

import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.dto.ProjectResponseDto;
import com.tus.anyDo.IndividualProject.dto.ProjectUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.mapper.ProjectMapper;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.IJwtService;
import com.tus.anyDo.IndividualProject.service.IProjectService;
import com.tus.anyDo.IndividualProject.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	private ProjectRepository projectRepository;
	private IJwtService jwtService;
	private IUserService userService;
	private IProjectService projectService;

	public ProjectController(ProjectRepository projectRepository, IJwtService jwtService, IUserService userService,
			IProjectService projectService) {
		this.projectRepository = projectRepository;
		this.jwtService = jwtService;
		this.userService = userService;
		this.projectService = projectService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public List<ProjectResponseDto> getProjects(@RequestHeader("Authorization") String token) {
		String username = jwtService.extractUsername(token.substring(7));
		return projectRepository.findByCreator_Username(username).stream().map((project) -> {
			ProjectResponseDto projectResponseDto = new ProjectResponseDto();
			ProjectMapper.toProjectResponseDto(project, projectResponseDto);
			return projectResponseDto;
		}).toList();
	}
	
	@PostMapping
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public long createProject(@RequestHeader("Authorization") String token,
			@Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws UserNotFoundException {
		String username = jwtService.extractUsername(token.substring(7));
		User user = userService.getUserByUsername(username);
		Project project = new Project();
		ProjectMapper.toProject(projectCreateRequest, user, project);
		projectRepository.save(project);
		return project.getId();
	}
	
	@DeleteMapping("/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ResponseEntity<Void> deleteProject(@RequestHeader("Authorization") String token,
			@PathVariable("projectId") long projectId) {

		String username = jwtService.extractUsername(token.substring(7));
		Project project = projectService.getProjectById(projectId);

		if (project == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		if (!project.getCreator().getUsername().equals(username)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
		}

		projectService.deleteProject(projectId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
	
	@PutMapping("/update/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ResponseEntity<ProjectResponseDto> updateProject(@RequestHeader("Authorization") String token,
			@PathVariable Long projectId, @RequestBody ProjectUpdateRequest projectUpdateRequest)
			throws UserNotFoundException {

		String username = jwtService.extractUsername(token.substring(7));
		User user = userService.getUserByUsername(username);
		Project project = projectService.getProjectById(projectId);

		if (project == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		if (!project.getCreator().getUsername().equals(username)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		
		if (projectUpdateRequest.getProjectName() != null) {
	        project.setProjectName(projectUpdateRequest.getProjectName());
	    }
		
		projectService.updateProject(project);
	    ProjectResponseDto projectResponseDto = new ProjectResponseDto();
	    ProjectMapper.toProjectResponseDto(project, projectResponseDto);
		return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);

	}
	
	@GetMapping("/{projectId}")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectResponseDto> getProjectById(@RequestHeader("Authorization") String token,
                                                       @PathVariable Long projectId) {
        
        String username = jwtService.extractUsername(token.substring(7));
        Project project = projectService.getProjectById(projectId);
        
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
        
        if (!project.getCreator().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); 
        }

        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        ProjectMapper.toProjectResponseDto(project, projectResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDto);
    }
}
