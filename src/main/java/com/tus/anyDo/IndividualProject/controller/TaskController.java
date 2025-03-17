package com.tus.anyDo.IndividualProject.controller;

import com.tus.anyDo.IndividualProject.dto.ManagerAssignRequest;
import com.tus.anyDo.IndividualProject.dto.ManagerAssignResponse;
import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;
import com.tus.anyDo.IndividualProject.mapper.TaskMapper;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.ITaskService;
import com.tus.anyDo.IndividualProject.service.IUserService;

import jakarta.transaction.Transactional;

import com.tus.anyDo.IndividualProject.service.IJwtService;
import com.tus.anyDo.IndividualProject.service.IProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final ITaskService taskService;
	private final IUserService userService;
	private final IJwtService jwtService;
	private final IProjectService projectService;

	// Constructor injection for dependencies
	public TaskController(ITaskService taskService, IUserService userService, IJwtService jwtService,
			IProjectService projectService) {
		this.taskService = taskService;
		this.userService = userService;
		this.jwtService = jwtService;
		this.projectService = projectService;
	}


	@PostMapping("/add")
	@PreAuthorize("hasRole('TEAMWORKER')")
	public ResponseEntity<TaskResponseDto> createTask(@RequestHeader("Authorization") String token,
			@RequestBody TaskCreateRequest taskCreateRequest) throws UserNotFoundException {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

		// Retrieve the user by their username
		User creator = userService.getUserByUsername(username);
		
		// Create the task and associate it with the user, project (if any), and status
		Task task = taskService.createTask(creator, taskCreateRequest.getName(), creator, taskCreateRequest.getProjectId(),
				taskCreateRequest.getStatus());

		TaskResponseDto taskResponseDto = new TaskResponseDto();
		TaskMapper.toTaskResponseDto(task, taskResponseDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('TEAMWORKER')")
	public ResponseEntity<List<TaskResponseDto>> getTasks(@RequestHeader("Authorization") String token) throws UserNotFoundException {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));

		// Retrieve the user by their username
		User user = userService.getUserByUsername(username);

		// Fetch all tasks for the user
		List<TaskResponseDto> tasks = taskService.getTasksByUserId(user.getId())
				.stream()
				.map(task -> {
					TaskResponseDto taskResponse = new TaskResponseDto();
					TaskMapper.toTaskResponseDto(task, taskResponse);
					return taskResponse;
				})
				.toList();

        // Return the task as a response entity with an OK status
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
	@GetMapping("/manager/list")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	@Transactional
	public ResponseEntity<List<TaskResponseDto>> getManagerTasks(
			@RequestHeader("Authorization") String token
	) throws UserNotFoundException {
		
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));

		// Retrieve the user by their username
		User user = userService.getUserByUsername(username);

		// Fetch all tasks for the user
		List<TaskResponseDto> tasks = taskService.getManagerTasks(user)
				.stream()
				.map(task -> {
					TaskResponseDto taskResponse = new TaskResponseDto();
					TaskMapper.toTaskResponseDto(task, taskResponse);
					return taskResponse;
				})
				.toList();

        // Return the task as a response entity with an OK status
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	@DeleteMapping("/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<Void> deleteTask(@RequestHeader("Authorization") String token,
			@PathVariable("taskId") Long taskId) {
		String username = jwtService.extractUsername(token.substring(7)); 
		Task task = taskService.getTaskById(taskId);

		if (task == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Task not found
		}
		if (!task.getCreator().getUsername().equals(username)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Users cannot delete tasks created by other users
		}
		taskService.deleteTask(taskId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@GetMapping("/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER')")
    public ResponseEntity<TaskResponseDto> getTaskById(@RequestHeader("Authorization") String token,
                                                       @PathVariable Long taskId) {
        // Extract the username from the JWT token
        String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

        // Retrieve the task by its ID
        Task task = taskService.getTaskById(taskId);

        // Check if the task exists
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Task not found
        }

        // Check if the task belongs to the logged-in user
        if (!task.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // User does not have permission
        }

        // Convert the task to a response DTO
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        TaskMapper.toTaskResponseDto(task, taskResponseDto);

        // Return the task as a response entity with an OK status
        return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
    }
	
	@PutMapping("/update/{taskId}")
	@PreAuthorize("hasRole('TEAMWORKER')")
	public ResponseEntity<TaskResponseDto> updateTask(@RequestHeader("Authorization") String token,
	        @PathVariable Long taskId, @RequestBody TaskUpdateRequest taskUpdateRequest) throws UserNotFoundException {
	    // Extract the username from the JWT token
	    String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

	    // Retrieve the user by their username
	    User user = userService.getUserByUsername(username);

	    // Retrieve the existing task by its ID
	    Task task = taskService.getTaskById(taskId);
	    
	    // Check if the task exists and if the user has permission to update it
	    if (task == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
	    if (!task.getUser().getUsername().equals(username)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	    }

	    // Update the task's properties with the new values from the request
	    if (taskUpdateRequest.getTaskName() != null) {
	        task.setTaskName(taskUpdateRequest.getTaskName());
	    }

	    if (taskUpdateRequest.getProjectId() != null) {
	        // Retrieve the project by ID
	        Project project = projectService.getProjectById(taskUpdateRequest.getProjectId());
	        
	        if (project != null) {
	            task.setProject(project); // Set the project object, not just the projectId
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // If project doesn't exist
	        }
	    }

	    if (taskUpdateRequest.getStatus() != null) {
	        task.setStatus(taskUpdateRequest.getStatus());
	    }

	    // Save the updated task
	    taskService.updateTask(task);

	    // Convert the updated task to a response DTO
	    TaskResponseDto taskResponseDto = new TaskResponseDto();
	    TaskMapper.toTaskResponseDto(task, taskResponseDto);

	    // Return the updated task as a response entity with an OK status
	    return ResponseEntity.status(HttpStatus.OK).body(taskResponseDto);
	}
	
	
	@PostMapping("/manager/add")
	@PreAuthorize("hasRole('PROJECT_MANAGER')")
	public ResponseEntity<ManagerAssignResponse> createTaskForWorker(
	        @RequestHeader("Authorization") String token,
	        @RequestBody ManagerAssignRequest managerAssignRequest) throws UserNotFoundException {

	    String username = jwtService.extractUsername(token.substring(7));
	    User manager = userService.getUserByUsername(username);

	    if (managerAssignRequest.getAssignedUser() == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }

	    User assignedUser = userService.getUserByUsername(managerAssignRequest.getAssignedUser());
	    if (assignedUser == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }

	    Project project = null;
	    if (managerAssignRequest.getProjectId() != null) {
	        project = projectService.getProjectById(managerAssignRequest.getProjectId());
	        if (project == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	    }

	    Task task = taskService.assignTask(manager, managerAssignRequest.getTaskName(), project, assignedUser, managerAssignRequest.getStatus());

	    // Create response DTO
	    ManagerAssignResponse response = new ManagerAssignResponse();
	    response.setTaskId(task.getId());
	    response.setTaskName(task.getTaskName());
	    response.setAssignedTo(assignedUser.getUsername());
	    response.setStatus(task.getStatus());
	    response.setProjectName(project != null ? project.getProjectName() : "No Project");

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}





