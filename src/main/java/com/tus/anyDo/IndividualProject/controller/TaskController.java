package com.tus.anyDo.IndividualProject.controller;

import com.tus.anyDo.IndividualProject.dto.ManagerAssignRequest;
import com.tus.anyDo.IndividualProject.dto.ManagerAssignResponse;
import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.ProjectNotFoundException;
import com.tus.anyDo.IndividualProject.exception.TaskNotFoundException;
import com.tus.anyDo.IndividualProject.exception.UnauthorizedAccessToTaskException;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.service.ITaskService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import com.tus.anyDo.IndividualProject.service.IJwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final ITaskService taskService;
	private final IJwtService jwtService;

	// Constructor injection for dependencies
	public TaskController(
			ITaskService taskService,
			IJwtService jwtService
	) {
		this.taskService = taskService;
		this.jwtService = jwtService;
	}


	@PostMapping("/add")
	@PreAuthorize("hasRole('TEAMWORKER')")
	@ResponseStatus(HttpStatus.CREATED)
	public TaskResponseDto createTask(
			@RequestHeader("Authorization") String token,
			@Valid @RequestBody TaskCreateRequest taskCreateRequest
	) throws UserNotFoundException, ProjectNotFoundException {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));
		return taskService.createTask(taskCreateRequest, username);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('TEAMWORKER')")
	public List<TaskResponseDto> getTasks(@RequestHeader("Authorization") String token) throws UserNotFoundException {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));
        return taskService.getTasksByUsername(username);
	}
	
	
	@GetMapping("/manager/list")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	@Transactional
	public ResponseEntity<List<TaskResponseDto>> getManagerTasks(
			@RequestHeader("Authorization") String token
	) throws UserNotFoundException {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getManagerTasks(username));
	}

	@DeleteMapping("/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<Void> deleteTask(
			@RequestHeader("Authorization") String token,
			@PathVariable("taskId") Long taskId
	) throws TaskNotFoundException, UnauthorizedAccessToTaskException {
		String username = jwtService.extractUsername(token.substring(7));
		taskService.deleteTask(username, taskId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@GetMapping("/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER')")
    public TaskResponseDto getTaskById(
    		@RequestHeader("Authorization") String token,
            @PathVariable Long taskId
    ) throws TaskNotFoundException, UnauthorizedAccessToTaskException {
        // Extract the username from the JWT token
        String username = jwtService.extractUsername(token.substring(7));
        return taskService.getTaskById(username, taskId);
    }
	
	@PutMapping("/update/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER')")
	public TaskResponseDto updateTask(
			@RequestHeader("Authorization") String token,
	        @PathVariable Long taskId, 
	        @Valid @RequestBody TaskUpdateRequest taskUpdateRequest
	) throws UserNotFoundException, UnauthorizedAccessToTaskException, ProjectNotFoundException, TaskNotFoundException {
	    // Extract the username from the JWT token
	    String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix
	    return taskService.updateTask(username, taskId, taskUpdateRequest);
	}
	
	
	@PostMapping("/manager/add")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ManagerAssignResponse createTaskForWorker(
	        @RequestHeader("Authorization") String token,
	        @Valid @RequestBody ManagerAssignRequest managerAssignRequest
	) throws UserNotFoundException, ProjectNotFoundException {
		String username = jwtService.extractUsername(token.substring(7));
		return taskService.assignTask(username, managerAssignRequest);
	}

}



