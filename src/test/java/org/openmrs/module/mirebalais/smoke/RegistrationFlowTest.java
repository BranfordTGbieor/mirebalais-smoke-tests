package org.openmrs.module.mirebalais.smoke;

import static org.junit.Assert.assertTrue;

import org.openmrs.module.mirebalais.smoke.pageobjects.IdentificationSteps;
import org.openmrs.module.mirebalais.smoke.pageobjects.LoginPage;
import org.openmrs.module.mirebalais.smoke.pageobjects.Registration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class RegistrationFlowTest  {

    private static WebDriver driver;
    private static Wait<WebDriver> wait;
    
    private LoginPage loginPage;
    private IdentificationSteps identificationSteps;
    private Registration registration;
    
    
    @Before
    public void setUp() {
    	System.setProperty("webdriver.chrome.driver","chromedriver");
    	driver = new ChromeDriver();
    	
    	wait = new WebDriverWait(driver, 30);
		driver.get("http://bamboo.pih-emr.org:8080/mirebalais");
		//driver.get("http://10.27.15.55:8080/mirebalais/");
    
		loginPage = new LoginPage(driver);
		identificationSteps = new IdentificationSteps(driver, wait);
		registration = new Registration(driver, wait);
    }

    @After
    public void tearDown() {
    	driver.close();
    }
     
    @Test
    @Ignore
    public void registerPatientWithoutPrintingCard() {
    	loginPage.logIn("admin", "Admin123");
    	identificationSteps.setLocationAndChooseRegisterTask();
    	registration.goThruRegistrationProcessWithoutPrintingCard();
    	
    	assertTrue(driver.findElement(By.tagName("body")).getText().contains("Patient dashboard"));
    }
    
    @Test
    public void registerPatientdPrintingCard() {
    	loginPage.logIn("admin", "Admin123");
    	identificationSteps.setLocationAndChooseRegisterTask();
    	registration.goThruRegistrationProcessPrintingCard();
    	
    	wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webDriver) {
				return webDriver.findElement(By.id("scanPatientIdentifier")).isDisplayed();
			}
		});
    	assertTrue(driver.findElement(By.tagName("body")).getText().contains("Please scan ID card to proceed..."));
    	// TODO: make sure it printed
    }
}