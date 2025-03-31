package com.tus.anyDo.IndividualProject.karate;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.intuit.karate.junit5.Karate;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProjectControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/project_controller_features";

	@Karate.Test
	Karate runAll() {
		return Karate.run(
			FOLDER + "/get_my_projects.feature"
		).relativeTo(getClass());
	}

}