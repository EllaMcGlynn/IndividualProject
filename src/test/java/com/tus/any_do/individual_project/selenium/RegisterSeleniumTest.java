//package com.tus.anyDo.IndividualProject.selenium;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
//
//import com.tus.anyDo.IndividualProject.model.Role;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.time.Duration;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Sql(
//    scripts = {
//    	"classpath:test_data/selenium_sql_files/register_selenium/cleanup.sql"
//    },
//    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
//)
//class RegisterSeleniumTest {
//	private static final String BASE_URL = "http://localhost:8082";
//	private static final String DASHBOARD_URL = "http://localhost:8082/?#";
//	
//	private WebDriverWait waitDriver;
//	private WebDriver driver;
//
//	@BeforeAll
//	void setup() {
//		WebDriverManager.chromedriver().setup();
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get(BASE_URL + "/index.html");
//		waitDriver = new WebDriverWait(driver, Duration.ofSeconds(10));
//	}
//
//	@Test
//    void testRegister() {
//		waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("")));
//		
//		
//        WebElement usernameField = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-sign-up")));
//        WebElement passwordField = driver.findElement(By.id("password-sign-up"));
//        Select roleField = new Select(driver.findElement(By.id("role-sign-up")));
//        WebElement registerButton = driver.findElement(By.id("signup-btn"));
//        
//        usernameField.sendKeys("TestTeamworker");
//        passwordField.sendKeys("TestPassword1");
//        Role role = Role.ROLE_TEAMWORKER;
//        roleField.selectByValue(role.toString());
//        registerButton.click();
//        
//        WebElement registerMessage = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.className("message")));
//        assertEquals("", registerMessage.getText());
//    }
//
//	@AfterAll
//	void teardown() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}
//}

