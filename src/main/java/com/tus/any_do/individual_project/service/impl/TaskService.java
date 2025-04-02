package com.tus.any_do.individual_project.service.impl;

import com.tus.any_do.individual_project.dao.ProjectRepository;
import com.tus.any_do.individual_project.dao.TaskRepository;
import com.tus.any_do.individual_project.dao.UserRepository;
import com.tus.any_do.individual_project.dto.ManagerAssignRequest;
import com.tus.any_do.individual_project.dto.ManagerAssignResponse;
import com.tus.any_do.individual_project.dto.TaskCreateRequest;
import com.tus.any_do.individual_project.dto.TaskResponseDto;
import com.tus.any_do.individual_project.dto.TaskUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.TaskNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToTaskException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;
import com.tus.any_do.individual_project.mapper.TaskMapper;
import com.tus.any_do.individual_project.model.Project;
import com.tus.any_do.individual_project.model.Task;
import com.tus.any_do.individual_project.model.User;
import com.tus.any_do.individual_project.service.ITaskService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {
	private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    // Constructor injection for required dependencies
    public TaskService(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
    	this.taskRepository = taskRepository;
        this.projectRepository = projectRepository; 
    }

    @Override
    public TaskResponseDto createTask(TaskCreateRequest taskCreateRequest, String username) throws ProjectNotFoundException, UserNotFoundException {
    	// Retrieve the user by their username - throw exception if doesn't exist
		User creator = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));
		
    	// Retrieve the Project object by projectId (may be null if no project is assigned)
		Long projectId = taskCreateRequest.getProjectId();
        Project project = (projectId != null) 
        		? projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " does not exist.")) 
        		: null;

        // Create a new Task with the given attributes and associations
        Task task = new Task();
        TaskMapper.toTask(taskCreateRequest, creator, project, task);
        
        // Save task
        taskRepository.save(task);
        
        // Map task to TaskResponseDto and return
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        TaskMapper.toTaskResponseDto(task, taskResponseDto);

        // Save the task to the database
        return taskResponseDto;
    }

    @Override
    public List<TaskResponseDto> getTasksByUsername(String username) throws UserNotFoundException {
		// Retrieve the user by their username
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));
		
		// Fetch all tasks for the user
		return taskRepository.findByUserId(user.getId())
				.stream()
				.map(task -> {
					TaskResponseDto taskResponse = new TaskResponseDto();
					TaskMapper.toTaskResponseDto(task, taskResponse);
					return taskResponse;
				})
				.toList();
    }
    
    @Override
    public void deleteTask(String username, long taskId) throws TaskNotFoundException, UnauthorizedAccessToTaskException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id: " + taskId + " cannot be found."));

		if (!task.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToTaskException("You do not have access to task with id: " + taskId); // Users cannot delete tasks created by other users
		}
		
		taskRepository.delete(task);
		
    }

    @Override
    public TaskResponseDto getTaskById(String username, Long taskId) throws TaskNotFoundException, UnauthorizedAccessToTaskException {
    	// Retrieve the task by its ID
    	Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id: " + taskId + " cannot be found."));

		if (!task.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToTaskException("You do not have access to task with id: " + taskId); // Users cannot delete tasks created by other users
		}

        // Convert the task to a response DTO
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        TaskMapper.toTaskResponseDto(task, taskResponseDto);

        // Return the task as a response entity with an OK status
        return taskResponseDto;
    }

	@Override
	public TaskResponseDto updateTask(
			String username, 
			long taskId, 
			TaskUpdateRequest taskUpdateRequest
	) throws UnauthorizedAccessToTaskException, UserNotFoundException, ProjectNotFoundException, TaskNotFoundException {
		// Retrieve the user by their username
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));
	
		// Fetch project if applicable
		Long projectId = taskUpdateRequest.getProjectId();
		Project project = (projectId != null) 
        		? projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " does not exist.")) 
        		: null;
		
		// Retrieve the task by its ID
    	Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with id: " + taskId + " cannot be found."));

		if (!task.getCreator().getUsername().equals(username)) {
			throw new UnauthorizedAccessToTaskException("You do not have access to task with id: " + taskId); // Users cannot delete tasks created by other users
		}

		TaskMapper.toTask(taskUpdateRequest, user, project, task);
		taskRepository.save(task);

	    // Convert the updated task to a response DTO
	    TaskResponseDto taskResponseDto = new TaskResponseDto();
	    TaskMapper.toTaskResponseDto(task, taskResponseDto);
		return taskResponseDto;
	}


	@Override
	public ManagerAssignResponse assignTask(String username, ManagerAssignRequest managerAssignRequest) throws ProjectNotFoundException, UserNotFoundException {
		// Retrieve the user by their username
		User manager = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));
		User assignedUser = userRepository.findByUsername(managerAssignRequest.getAssignedUser()).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));

		Long projectId = managerAssignRequest.getProjectId();
		Project project = (projectId != null) 
        		? projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " does not exist.")) 
        		: null;

	    Task task = new Task();
	    task.setCreator(manager);
	    task.setProject(project);
	    task.setStatus(managerAssignRequest.getStatus());
	    task.setTaskName(managerAssignRequest.getTaskName());
	    task.setUser(assignedUser);
	    
	    taskRepository.save(task);

	    // Create response DTO
	    ManagerAssignResponse response = new ManagerAssignResponse();
	    response.setTaskId(task.getId());
	    response.setTaskName(task.getTaskName());
	    response.setAssignedTo(assignedUser.getUsername());
	    response.setStatus(task.getStatus());
	    response.setProjectName(project != null ? project.getProjectName() : "No Project");

	    return response;
	}

	@Override
	public List<TaskResponseDto> getManagerTasks(String username) throws UserNotFoundException {
		// Retrieve the user by their username
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist."));	

		// Fetch all tasks for the user
		return taskRepository.findByCreator(user)
				.stream()
				.map(task -> {
					TaskResponseDto taskResponse = new TaskResponseDto();
					TaskMapper.toTaskResponseDto(task, taskResponse);
					return taskResponse;
				})
				.toList();
	}
	
}

