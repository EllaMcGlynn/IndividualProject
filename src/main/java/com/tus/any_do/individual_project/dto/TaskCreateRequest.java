package com.tus.any_do.individual_project.dto;

import com.tus.any_do.individual_project.model.TaskStatus;

import lombok.Data;

@Data
public class TaskCreateRequest {
    private String name; 
    private Long projectId; 
    private TaskStatus status; 

}
