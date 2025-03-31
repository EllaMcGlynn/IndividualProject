package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.dto.ManagerAssignRequest;
import com.tus.anyDo.IndividualProject.dto.ManagerAssignResponse;
import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.exception.ProjectNotFoundException;
import com.tus.anyDo.IndividualProject.exception.TaskNotFoundException;
import com.tus.anyDo.IndividualProject.exception.UnauthorizedAccessToTaskException;
import com.tus.anyDo.IndividualProject.exception.UserNotFoundException;

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
