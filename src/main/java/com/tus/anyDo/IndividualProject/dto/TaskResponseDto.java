package com.tus.anyDo.IndividualProject.dto;

import com.tus.anyDo.IndividualProject.model.TaskStatus;

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
