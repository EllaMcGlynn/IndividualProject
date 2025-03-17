package com.tus.anyDo.IndividualProject.mapper;

import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.model.Task;

public class TaskMapper {
	
	
	public static final void  toTaskResponseDto(Task task, TaskResponseDto taskResponseDto) {
		taskResponseDto.setId(task.getId());
		if (task.getProject() == null) {
			taskResponseDto.setProjectName(null);
		} else {
			taskResponseDto.setProjectName(task.getProject().getProjectName());
		}
		taskResponseDto.setStatus(task.getStatus());
		taskResponseDto.setTaskName(task.getTaskName());
		taskResponseDto.setAssignedUser(task.getUser().getUsername());
	}

}
