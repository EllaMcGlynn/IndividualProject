package com.tus.anyDo.IndividualProject.karate;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.intuit.karate.junit5.Karate;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(
	scripts = {
		"classpath:test_data/karate_sql_files/project_controller/users.sql",
		"classpath:test_data/karate_sql_files/project_controller/projects.sql",
		"classpath:test_data/karate_sql_files/project_controller/tasks.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = {
    	"classpath:test_data/karate_sql_files/project_controller/cleanup.sql"
    },
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
public class ProjectControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/project_controller_features";
	
	@Karate.Test
	Karate runCreateProject() {
	    return Karate.run(FOLDER + "/create_my_project.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runGetProjects() {
	    return Karate.run(FOLDER + "/get_my_projects.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runGetProjectById() {
	    return Karate.run(FOLDER + "/get_by_project_id.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runUpdateProject() {
	    return Karate.run(FOLDER + "/update_project.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runDeleteProject() {
	    return Karate.run(FOLDER + "/delete_project.feature").relativeTo(getClass());
	}
	
}



