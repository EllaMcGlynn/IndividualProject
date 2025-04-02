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
		"classpath:test_data/karate_sql_files/security_controller/users.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = {
    	"classpath:test_data/karate_sql_files/project_controller/cleanup.sql"
    },
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
public class SecurityControllerKarateTest {
	private static final String FOLDER = "classpath:karate_files/security_controller_features";

	@Karate.Test
	Karate runLoginTests() {
		return Karate.run(
			FOLDER + "/login.feature"
		).relativeTo(getClass());
	}
	
	@Karate.Test
	Karate runRegistrationTests() {
		return Karate.run(
			 FOLDER + "/register.feature"
		).relativeTo(getClass());
	}
}
