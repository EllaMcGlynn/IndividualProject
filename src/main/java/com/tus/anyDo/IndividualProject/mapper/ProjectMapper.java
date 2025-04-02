package com.tus.anyDo.IndividualProject.mapper;

import java.util.ArrayList;

import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.dto.ProjectResponseDto;
import com.tus.anyDo.IndividualProject.dto.ProjectUpdateRequest;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.User;

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
