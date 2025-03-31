package com.tus.anyDo.IndividualProject.mapper;

import com.tus.anyDo.IndividualProject.dto.TaskCreateRequest;
import com.tus.anyDo.IndividualProject.dto.TaskResponseDto;
import com.tus.anyDo.IndividualProject.dto.TaskUpdateRequest;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.Task;
import com.tus.anyDo.IndividualProject.model.User;

public class TaskMapper {
	private TaskMapper() {}
	
	public static final void toTaskResponseDto(Task task, TaskResponseDto taskResponseDto) {
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
	
	public static final void toTask(TaskCreateRequest taskCreateRequest, User creator, Project project, Task task) {
        task.setCreator(creator);
        task.setProject(project);
        task.setStatus(taskCreateRequest.getStatus());
        task.setTaskName(taskCreateRequest.getName());
        task.setUser(creator);
	}
	
	public static final void toTask(TaskUpdateRequest taskUpdateRequest, User creator, Project project, Task task) {
		task.setCreator(creator);
        task.setProject(project);
        task.setStatus(taskUpdateRequest.getStatus());
        task.setTaskName(taskUpdateRequest.getTaskName());
        task.setUser(creator);
	}

}
