package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.TaskStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponseDto {
    private Long id;

    private String taskName;

    private String projectName;
    
    private String assignedUser;

    private TaskStatus status;
}
