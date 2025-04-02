package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.TaskStatus;

import lombok.Data;

@Data
public class ManagerAssignResponse {
    private Long taskId;
    private String taskName;
    private String assignedTo;
    private TaskStatus status;
    private String projectName;
}
