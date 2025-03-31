//package com.tus.anyDo.IndividualProject.selenium;
//
//import org.junit.jupiter.api.*;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//
//import java.time.Duration;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class UITests {
//	private WebDriverWait waitDriver;
//	private WebDriver driver;
//	private final String BASE_URL = "http://localhost:8082"; 
//
//	@BeforeAll
//	void setup() {
//		WebDriverManager.edgedriver().setup();
//		driver = new EdgeDriver();
//		driver.manage().window().maximize();
//		driver.get(BASE_URL + "/index.html");
//		waitDriver = new WebDriverWait(driver, Duration.ofSeconds(100));
//	}
//
//	@Test
//    void testLogin() {
//        WebElement usernameField = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-sign-in")));
//        WebElement passwordField = driver.findElement(By.id("password-sign-in"));
//        WebElement loginButton = driver.findElement(By.id("login-btn"));
//        usernameField.sendKeys("testTeamworker");
//        passwordField.sendKeys("Password1");
//        loginButton.click();
//        WebElement openTaskModalButton = waitDriver.until(ExpectedConditions.presenceOfElementLocated(By.id("openTaskModal")));
//    }
//
//	@AfterAll
//	void teardown() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}
//}