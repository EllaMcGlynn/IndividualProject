package com.tus.any_do.individual_project.mapper;

import java.util.ArrayList;

import com.tus.any_do.individual_project.dto.ProjectCreateRequest;
import com.tus.any_do.individual_project.dto.ProjectResponseDto;
import com.tus.any_do.individual_project.dto.ProjectUpdateRequest;
import com.tus.any_do.individual_project.model.Project;
import com.tus.any_do.individual_project.model.User;

public final class ProjectMapper {
	private ProjectMapper() {}
	
	public static void toProject(ProjectCreateRequest projectCreateRequest, User user, Project project) {
		project.setCreator(user);
		project.setProjectName(projectCreateRequest.getProjectName());
		project.setTasks(new ArrayList<>());
	}
	
	public static void toProjectResponseDto(Project project, ProjectResponseDto projectResponseDto) {
		projectResponseDto.setProjectId(project.getId());
		projectResponseDto.setProjectName(project.getProjectName());
	}
	
	public static void toProject(ProjectUpdateRequest projectUpdateRequest, User user, Project project) {
		project.setCreator(user);
		project.setProjectName(projectUpdateRequest.getProjectName());
	}
}
