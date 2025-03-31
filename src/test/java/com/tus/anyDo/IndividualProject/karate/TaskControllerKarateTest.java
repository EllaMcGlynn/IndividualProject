package com.tus.anyDo.IndividualProject.karate;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.intuit.karate.junit5.Karate;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class TaskControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/task_controller_features";
	
	
	@Karate.Test
	Karate runAll() {
		return Karate.run(
			FOLDER + "/create_task_teamworker.feature",
			FOLDER + "/get_tasks.feature",
			FOLDER + "/get_manager_tasks.feature",
			FOLDER + "/get_task_by_id.feature",
			FOLDER + "/delete_task.feature"
		).relativeTo(getClass());
	}

}