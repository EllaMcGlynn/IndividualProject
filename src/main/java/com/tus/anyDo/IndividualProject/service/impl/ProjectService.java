package com.tus.anyDo.IndividualProject.service.impl;

import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.model.Project;
import com.tus.anyDo.IndividualProject.service.IProjectService;
import org.springframework.stereotype.Service;

@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProject(String projectName) {
        // Create and save a new project
        Project project = new Project();
        project.setProjectName(projectName);
        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

	@Override
	public void deleteProject(long projectID) {
		projectRepository.deleteById(projectID);
	}

	@Override
	public Project updateProject(Project project) {
		return projectRepository.save(project);
	}
}