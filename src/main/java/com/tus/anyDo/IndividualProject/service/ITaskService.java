package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.TaskStatus;
import com.tus.anyDo.IndividualProject.model.User;

import java.util.List;

public interface ITaskService {

    Task createTask(User user, String taskName, User creator, Long projectId, TaskStatus status);

    List<Task> getTasksByUserId(Long userId);

    List<Task> getTasksByStatus(TaskStatus status);

    List<Task> getTasksByProject(Long projectId);
    
    void deleteTask(Long taskId);

	Task getTaskById(Long taskId);

	Task updateTask(Task task);
	
	Task assignTask(User manager, String taskName, Project project, User assignedUser, TaskStatus status);
	
	List<Task> getManagerTasks(User user);
}
