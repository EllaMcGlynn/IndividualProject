package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.TaskStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateRequest {
    private String taskName;
    private Long projectId; 
    private TaskStatus status;
}
