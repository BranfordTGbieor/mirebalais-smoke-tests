package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public void logIn(String user, String password) {
		driver.findElement(By.id("username")).sendKeys(user);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("#sessionLocation li")).click();
		driver.findElement(By.id("login-button")).click();
	}

	public void logInAsAdmin() {
		this.logIn("admin", "Admin123");
	}
}
