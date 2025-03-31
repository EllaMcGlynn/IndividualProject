package com.tus.anyDo.IndividualProject.karate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.intuit.karate.junit5.Karate;
import com.tus.anyDo.IndividualProject.dao.ProjectRepository;
import com.tus.anyDo.IndividualProject.dao.TaskRepository;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProjectControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/project_controller_features";
	
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private TaskRepository taskRepo;
	
	@BeforeAll
	void setupAll() {
		
	}
	
	@Karate.Test
	Karate runAll() {
		return Karate.run(
			FOLDER + "/create_my_project.feature",
			FOLDER + "/get_my_projects.feature",
			FOLDER + "/get_by_projectID.feature",
			FOLDER + "/update_project.feature",
			FOLDER + "/delete_project.feature"
		).relativeTo(getClass());
	}
	
	@AfterAll
	void teardownAll() {
		taskRepo.deleteAll();
		projectRepo.deleteAll();	
	}

}