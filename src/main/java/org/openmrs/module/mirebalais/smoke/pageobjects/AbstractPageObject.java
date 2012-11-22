package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AbstractPageObject {

    protected SmokeTestProperties properties = new SmokeTestProperties();

    protected WebDriver driver;
    private String baseServerUrl;
    private LoginPage loginPO;

    public AbstractPageObject(WebDriver driver) {
        this.driver = driver;
        this.loginPO = new LoginPage(driver);
        setBaseServerUrl();
    }

    protected void gotoPage(String addressSufix) {
        driver.get(baseServerUrl + addressSufix);
    }

    protected void login(String username, String password) {
        gotoPage("");
        loginPO.logIn(username, password);
    }

    private void setBaseServerUrl() {
        String serverUrl = System.getProperty("baseServerUrl");
        this.baseServerUrl = (serverUrl == null || serverUrl.isEmpty() ? properties.getWebAppUrl() : serverUrl);
    }
    
    public void clickNext() {
        driver.findElement(By.id("right-arrow-yellow")).click();
    }
    
    public void clickYellowCheckMark() {
        driver.findElement(By.id("checkmark-yellow")).click();
    }
    
    public void setClearTextToField(String textFieldId, String text) {
    	WebElement element = driver.findElement(By.id(textFieldId));
		element.clear();
		element.sendKeys(text);
    }
}
