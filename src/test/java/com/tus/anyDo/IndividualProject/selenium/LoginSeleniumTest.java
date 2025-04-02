package com.tus.anyDo.IndividualProject.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
	scripts = {
		"classpath:test_data/selenium_sql_files/login_selenium/users.sql",
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = {
    	"classpath:test_data/selenium_sql_files/login_selenium/cleanup.sql"
    },
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
class LoginSeleniumTest {
	private static final String BASE_URL = "http://localhost:8082";
	private static final String DASHBOARD_URL = "http://localhost:8082/index.html";
	
	private WebDriverWait waitDriver;
	private WebDriver driver;

	@BeforeAll
	void setup() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(BASE_URL + "/index.html");
		waitDriver = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
    void testLogin() {
        WebElement usernameField = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-sign-in")));
        WebElement passwordField = driver.findElement(By.id("password-sign-in"));
        WebElement loginButton = driver.findElement(By.id("login-btn"));
        usernameField.sendKeys("TestTeamworker");
        passwordField.sendKeys("TestPassword1");
        
        
        loginButton.click();
        waitDriver.until(ExpectedConditions.stalenessOf(loginButton));
        
        assertEquals(DASHBOARD_URL, driver.getCurrentUrl());
    }

	@AfterAll
	void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}
}