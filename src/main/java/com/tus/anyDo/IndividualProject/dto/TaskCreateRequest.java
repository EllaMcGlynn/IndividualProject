package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.TaskStatus;
import lombok.Data;

@Data
public class TaskCreateRequest {
    private String name; 
    private Long projectId; 
    private TaskStatus status; 

}
