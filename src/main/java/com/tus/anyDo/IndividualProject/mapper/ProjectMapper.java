package com.tus.anyDo.IndividualProject.mapper;

import java.util.ArrayList;

import com.tus.anyDo.IndividualProject.dto.ProjectCreateRequest;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.model.User;

public final class ProjectMapper {
	
	public static void toProject(ProjectCreateRequest projectCreateRequest, User user, Project project) {
		project.setCreator(user);
		project.setProjectName(projectCreateRequest.getProjectName());
		project.setTasks(new ArrayList<>());
	}
}
