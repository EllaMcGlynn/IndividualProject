package com.tus.anyDo.IndividualProject.service.impl;

import com.tus.anyDo.IndividualProject.dao.TaskRepository;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.TaskStatus;
import com.tus.anyDo.IndividualProject.model.User;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.service.ITaskService;
import com.tus.anyDo.IndividualProject.service.IProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IProjectService projectService;

    // Constructor injection for required dependencies
    public TaskService(TaskRepository taskRepository, IProjectService projectService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

    @Override
    public Task createTask(User user, String taskName, User creator, Long projectId, TaskStatus status) {
        // Retrieve the Project object by projectId (may be null if no project is assigned)
        Project project = (projectId != null) ? projectService.getProjectById(projectId) : null;

        // Create a new Task with the given attributes and associations
        Task task = new Task();
        task.setCreator(creator);
        task.setProject(project);
        task.setStatus(status);
        task.setTaskName(taskName);
        task.setUser(user);

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
    
    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        
        if (task != null) {
            taskRepository.delete(task);  
        } else {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);  
        }
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null); 
    }

	@Override
	public Task updateTask(Task task) {
		return taskRepository.save(task);
	}


	@Override
	public Task assignTask(User manager, String taskName, Project project, User assignedUser, TaskStatus status) {
	    if (assignedUser == null) {
	        throw new IllegalArgumentException("Assigned user cannot be null");
	    }

	    if (taskName == null || taskName.trim().isEmpty()) {
	        throw new IllegalArgumentException("Task name cannot be empty");
	    }

	    Task task = new Task();
	    task.setCreator(manager);
	    task.setProject(project);
	    task.setStatus(status);
	    task.setTaskName(taskName);
	    task.setUser(assignedUser);
	    
	    return taskRepository.save(task);
	}

	@Override
	public List<Task> getManagerTasks(User user) {
		return taskRepository.findByCreator(user);
	}
	
}

