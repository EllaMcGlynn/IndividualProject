package com.tus.anyDo.IndividualProject.karate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.intuit.karate.junit5.Karate;
import com.tus.anyDo.IndividualProject.dao.UserRepository;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SecurityControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/security_controller_features";
	
	@Autowired
	private UserRepository userRepository;

	@Karate.Test
	Karate runAll() {
		return Karate.run(
			 FOLDER + "/login_as_teamworker.feature",
			 FOLDER + "/login_as_project_manager.feature",
			 FOLDER + "/register_teamworker.feature",
			 FOLDER + "/register_project_manager.feature"
		).relativeTo(getClass());
	}
	
	// Remove added users from registration so next test class starts fresh
	@AfterAll
	void teardownAll() {
		userRepository.delete(userRepository.findByUsername("teamworker-register1").get());
		userRepository.delete(userRepository.findByUsername("project-manager-register1").get());
	}
}
