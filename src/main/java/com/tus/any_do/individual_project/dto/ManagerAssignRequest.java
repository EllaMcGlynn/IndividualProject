package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.TaskStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManagerAssignRequest {
	@NotEmpty(message = "You must provide a task")
    private String taskName;
	
	@NotNull(message = "You must assign the task to a user")
    private String assignedUser;
	
    private Long projectId;
    
    @NotNull(message = "You must provide a valid task status")
    private TaskStatus status; 

}
