package com.tus.anyDo.IndividualProject.controller;

import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.model.TaskStatus;
import com.tus.anyDo.IndividualProject.service.ITaskService;
import com.tus.anyDo.IndividualProject.service.IUserService;
import com.tus.anyDo.IndividualProject.service.IJwtService;
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

    // Constructor injection for dependencies
    public TaskController(ITaskService taskService, IUserService userService, IJwtService jwtService) {
        this.taskService = taskService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Endpoint for creating a task
    @PostMapping("/add")
    public ResponseEntity<Task> createTask(@RequestHeader("Authorization") String token,
                                           @RequestBody TaskCreateRequest taskCreateRequest) {
        // Extract the username from the JWT token
        String username = jwtService.extractUsername(token.substring(7)); // Removing the "Bearer " prefix

        // Retrieve the user by their username
        User user = userService.getUserByUsername(username);

        // Create the task and associate it with the user, project (if any), and status
        Task task = taskService.createTask(username, taskCreateRequest.getName(),
                                           taskCreateRequest.getProjectId(), taskCreateRequest.getStatus());

        // Return the created task as a response entity with a CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
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

    // Endpoint to get tasks filtered by their status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestHeader("Authorization") String token,
                                                        @PathVariable TaskStatus status) {
        // Extract the username from the JWT token
        String username = jwtService.extractUsername(token.substring(7));

        // Retrieve the user by their username
        User user = userService.getUserByUsername(username);

        // Fetch tasks for the user with the specified status
        List<Task> tasks = taskService.getTasksByStatus(status);

        // Return the list of tasks with an OK status
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    // Endpoint to get tasks filtered by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@RequestHeader("Authorization") String token,
                                                         @PathVariable Long projectId) {
        // Extract the username from the JWT token
        String username = jwtService.extractUsername(token.substring(7));

        // Retrieve the user by their username
        User user = userService.getUserByUsername(username);

        // Fetch tasks related to the specified project for the user
        List<Task> tasks = taskService.getTasksByProject(projectId);

        // Return the list of tasks with an OK status
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }
}
