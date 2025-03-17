package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.TaskStatus;

import lombok.Data;

@Data
public class ManagerAssignResponse {
    private Long taskId;
    private String taskName;
    private String assignedTo;
    private TaskStatus status;
    private String projectName;
}
