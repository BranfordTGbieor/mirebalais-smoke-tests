package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MirebalaisLoginPage extends LoginPage {

    public MirebalaisLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void logIn(String user, String password, int location) {
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElements(By.cssSelector("#sessionLocation li")).get(location).click();
        driver.findElement(By.id("login-button")).click();
    }
}

