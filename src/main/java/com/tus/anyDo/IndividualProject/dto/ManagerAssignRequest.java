package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.TaskStatus;

import lombok.Data;

@Data
public class ManagerAssignRequest {
    private String taskName;
    private String assignedUser;
    private Long projectId;
    private TaskStatus status; 

}
