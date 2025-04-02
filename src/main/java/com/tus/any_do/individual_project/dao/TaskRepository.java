package com.tus.any_do.individual_project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.any_do.individual_project.model.Task;
import com.tus.any_do.individual_project.model.TaskStatus;
import com.tus.any_do.individual_project.model.User;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Find tasks by user ID (this should still be a common use case)
    List<Task> findByUserId(Long userId);
    
    // Find tasks by status
    List<Task> findByStatus(TaskStatus status);
    
    // Find tasks by project
    List<Task> findByProjectId(Long projectId);
    
    List<Task> findByCreator(User user);
}
