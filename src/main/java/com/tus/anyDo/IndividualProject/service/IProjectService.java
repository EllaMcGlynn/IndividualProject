package com.tus.anyDo.IndividualProject.service;

import com.tus.anyDo.IndividualProject.model.Project;

public interface IProjectService {

    Project createProject(String projectName);

    Project getProjectById(Long projectId);

	void deleteProject(long projectID);
}
