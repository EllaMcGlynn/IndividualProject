package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.TaskStatus;

import java.util.List;

public interface ITaskService {

    Task createTask(String username, String taskName, Long projectId, TaskStatus status);

    List<Task> getTasksByUserId(Long userId);

    List<Task> getTasksByStatus(TaskStatus status);

    List<Task> getTasksByProject(Long projectId);
    
    void deleteTask(Long taskId);

	Task getTaskById(Long taskId);
}
