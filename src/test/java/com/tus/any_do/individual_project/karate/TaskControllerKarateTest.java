package com.tus.any_do.individual_project.karate;

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
		"classpath:test_data/karate_sql_files/task_controller/users.sql",
		"classpath:test_data/karate_sql_files/task_controller/projects.sql",
		"classpath:test_data/karate_sql_files/task_controller/tasks.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = {
    	"classpath:test_data/karate_sql_files/project_controller/cleanup.sql"
    },
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
public class TaskControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/task_controller_features";
	
	@Karate.Test
	Karate runTeamworkerCreatesTask() {
	    return Karate.run(FOLDER + "/create_task_teamworker.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runGetTasks() {
	    return Karate.run(FOLDER + "/get_tasks.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runGetManagerTasks() {
	    return Karate.run(FOLDER + "/get_manager_tasks.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runGetTaskById() {
	    return Karate.run(FOLDER + "/get_task_by_id.feature").relativeTo(getClass());
	}

	@Karate.Test
	Karate runDeleteTask() {
	    return Karate.run(FOLDER + "/delete_task.feature").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate runUpdateTask() {
		return Karate.run(FOLDER + "/update_task.feature").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate runCreateTaskForTeamworker() {
		return Karate.run(FOLDER + "/create_task_for_teamworker.feature").relativeTo(getClass());
	}
}

