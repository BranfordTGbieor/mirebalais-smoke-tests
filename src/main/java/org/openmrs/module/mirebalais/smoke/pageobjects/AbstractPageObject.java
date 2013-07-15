/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openmrs.module.mirebalais.smoke.helper.SmokeTestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class AbstractPageObject {

    protected SmokeTestProperties properties = new SmokeTestProperties();

    protected WebDriver driver;
    protected WebDriverWait wait5seconds;
    private String baseServerUrl;
    private LoginPage loginPO;

    public AbstractPageObject(WebDriver driver) {
        this.driver = driver;
        this.loginPO = new LoginPage(driver);
        this.wait5seconds = new WebDriverWait(driver, 30);
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
        clickOn(By.id("right-arrow-yellow"));
    }
    
    public void clickYellowCheckMark() {
    	clickOn(By.id("checkmark-yellow"));
    }
    
    public void setClearTextToField(By element, String text) {
        setText(driver.findElement(element), text);
    }
    
    public void setClearTextToField(String textFieldId, String text) {
        setText(driver.findElement(By.id(textFieldId)), text);
    }
    
    public void setClearTextToFieldThruSpan(String spanId, String text) {
    	setText(findTextFieldInsideSpan(spanId), text);
	}
    
    private void setText(WebElement element, String text) {
    	element.clear();
		element.sendKeys(text);
        element.sendKeys(Keys.RETURN);
    }
    
    private WebElement findTextFieldInsideSpan(String spanId) {
    	return driver.findElement(By.id(spanId)).findElement(By.tagName("input"));
    }
    
    public String format(String patientName) {
		int index = patientName.indexOf(" ");
		return new StringBuffer()
					.append(patientName.substring(index).trim())
					.append(", ")
					.append(patientName.substring(0,index).trim())
					.toString();
	}
    
    public void findPatientById(String patientIdentifier, String idField) throws Exception {
		WebElement element = driver.findElement(By.id(idField));
		element.sendKeys(patientIdentifier);
		clickOnTheRightPatient(patientIdentifier);
	}
    
    private void clickOnTheRightPatient(String patientIdentifier) throws Exception {
	    clickOnOptionLookingForText(patientIdentifier, By.cssSelector("li.ui-menu-item"));
	}
    
    public void clickOnOptionLookingForText(String text, By by) throws Exception {
		getOptionBasedOnText(text,by).click();
	}
    
    public WebElement getOptionBasedOnText(String text, By elementIdentifier) throws Exception {
		List<WebElement> options = driver.findElements(elementIdentifier);
		for (WebElement option : options) {
	        if(option.getText().contains(text)) {
	            return option;
	        }
	    } 
		throw new Exception("Option not found");
    }
    
    public void clickOnRandomOption(By elementIdentifier) {
    	getRandomOption(elementIdentifier).click();
    }
    
    public void clickOnRandomOptionIgnoringFirst(By elementIdentifier) {
    	getRandomOptionExcludingFirst(elementIdentifier).click();
    }
    
    public WebElement getRandomOption(By elementIdentifier) {
    	List<WebElement> options = driver.findElements(elementIdentifier);
    	return options.get((int)(Math.random() * options.size()));
    }
    
    public WebElement getRandomOptionExcludingFirst(By elementIdentifier) {
    	List<WebElement> options = driver.findElements(elementIdentifier);
    	return options.get(1+(int)(Math.random() * options.size()-1));
    }
   
    public void clickOn(By elementId) {
    	driver.findElement(elementId).click();
	}

    public void hoverOn(By elementId) {
        Actions builder = new Actions(driver);
        Actions hover = builder.moveToElement(driver.findElement(elementId));
        hover.perform();
    }
    
}
