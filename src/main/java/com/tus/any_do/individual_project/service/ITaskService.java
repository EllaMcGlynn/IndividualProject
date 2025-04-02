package com.tus.any_do.individual_project.service;

import com.tus.any_do.individual_project.dto.ManagerAssignRequest;
import com.tus.any_do.individual_project.dto.ManagerAssignResponse;
import com.tus.any_do.individual_project.dto.TaskCreateRequest;
import com.tus.any_do.individual_project.dto.TaskResponseDto;
import com.tus.any_do.individual_project.dto.TaskUpdateRequest;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.TaskNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToTaskException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;

import java.util.List;

public interface ITaskService {

	TaskResponseDto createTask(TaskCreateRequest taskCreateRequest, String username) throws ProjectNotFoundException, UserNotFoundException;

	List<TaskResponseDto> getTasksByUsername(String username) throws UserNotFoundException;
    
    void deleteTask(String username, long taskId) throws TaskNotFoundException, UnauthorizedAccessToTaskException;

    TaskResponseDto getTaskById(String username, Long taskId) throws TaskNotFoundException, UnauthorizedAccessToTaskException;

    TaskResponseDto updateTask(String username, long taskId, TaskUpdateRequest taskUpdateRequest) throws UnauthorizedAccessToTaskException, UserNotFoundException, ProjectNotFoundException, TaskNotFoundException;
	
    ManagerAssignResponse assignTask(String username, ManagerAssignRequest managerAssignRequest) throws ProjectNotFoundException, UserNotFoundException;
    
	List<TaskResponseDto> getManagerTasks(String username) throws UserNotFoundException;
}
