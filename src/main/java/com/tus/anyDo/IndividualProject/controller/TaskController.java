package com.tus.anyDo.IndividualProject.controller;

import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.mapper.TaskMapper;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.service.ITaskService;
import com.tus.anyDo.IndividualProject.service.IUserService;
import com.tus.anyDo.IndividualProject.service.IJwtService;
import com.tus.anyDo.IndividualProject.service.IProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	// Endpoint for creating a task
	@PostMapping("/add")
	public ResponseEntity<TaskResponseDto> createTask(@RequestHeader("Authorization") String token,
			@RequestBody TaskCreateRequest taskCreateRequest) {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

		// Retrieve the user by their username
		User user = userService.getUserByUsername(username);

		// Create the task and associate it with the user, project (if any), and status
		Task task = taskService.createTask(username, taskCreateRequest.getName(), taskCreateRequest.getProjectId(),
				taskCreateRequest.getStatus());

		TaskResponseDto taskResponseDto = new TaskResponseDto();
		TaskMapper.toTaskResponseDto(task, taskResponseDto);

		// Return the created task as a response entity with a CREATED status
		return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDto);
	}

	// Endpoint to get all tasks for the logged-in user
	@GetMapping("/list")
	public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7));

		// Retrieve the user by their username
		User user = userService.getUserByUsername(username);

		// Fetch all tasks for the user
		List<Task> tasks = taskService.getTasksByUserId(user.getId());

		// Return the list of tasks with an OK status
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	@DeleteMapping("/{taskId}")
	public ResponseEntity<Void> deleteTask(@RequestHeader("Authorization") String token,
			@PathVariable("taskId") Long taskId) {
		// Extract the username from the JWT token
		String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

		// Retrieve the task to check ownership and existence
		Task task = taskService.getTaskById(taskId);

		// Check if the task exists and if it belongs to the user
		if (task == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Task not found
		}

		// Verify if the task belongs to the user
		if (!task.getUser().getUsername().equals(username)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // User does not have permission
		}

		// Proceed to delete the task
		taskService.deleteTask(taskId);

		// Return a successful response with NO_CONTENT status
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	 @GetMapping("/{taskId}")
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
	public ResponseEntity<TaskResponseDto> updateTask(@RequestHeader("Authorization") String token,
	        @PathVariable Long taskId, @RequestBody TaskUpdateRequest taskUpdateRequest) {
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




}
