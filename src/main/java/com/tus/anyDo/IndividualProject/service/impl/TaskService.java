package com.tus.anyDo.IndividualProject.service.impl;

import com.tus.anyDo.IndividualProject.dao.TaskRepository;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.TaskStatus;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.service.ITaskService;
import com.tus.anyDo.IndividualProject.service.IUserService;
import com.tus.anyDo.IndividualProject.service.IProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IUserService userService;
    private final IProjectService projectService;

    // Constructor injection for required dependencies
    public TaskService(TaskRepository taskRepository, IUserService userService, IProjectService projectService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectService = projectService;
    }

    @Override
    public Task createTask(String username, String taskName, Long projectId, TaskStatus status) {
        // Retrieve the User object by username
        User user = userService.getUserByUsername(username);
        
        // Retrieve the Project object by projectId (may be null if no project is assigned)
        Project project = (projectId != null) ? projectService.getProjectById(projectId) : null;

        // Create a new Task with the given attributes and associations
        Task task = new Task(taskName, user, project, status);

        // Save the task to the database
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        // Fetch tasks that are associated with the given userId
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        // Fetch tasks that are associated with the given status
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByProject(Long projectId) {
        // Fetch tasks that are associated with the given projectId
        return taskRepository.findByProjectId(projectId);
    }
}
