package org.openmrs.module.mirebalais.smoke;

import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.Waiter;
import org.openmrs.module.mirebalais.smoke.pageobjects.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public abstract class BasicMirebalaisSmokeTest {

    protected SmokeTestProperties properties = new SmokeTestProperties();
    protected static LoginPage loginPage;
    protected AppDashboard appDashboard;
    protected Registration registration;
    protected PatientRegistrationDashboard patientRegistrationDashboard;
    protected PatientDashboard patientDashboard;
    protected Patient testPatient;
	
	protected RemoteWebDriver driver;

    @Before
    public void startWebDriver() throws MalformedURLException {
        setupChromeDriver();
    	driver = new ChromeDriver();
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setJavascriptEnabled(true);
//        capabilities.setCapability("takesScreenshot", false);
//        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//                "src/test/resources/phantomjsdriver/mac/phantomjs");

//        driver = new PhantomJSDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
    	driver.get(new SmokeTestProperties().getWebAppUrl());
    }

	@After
    public void stopWebDriver() {
        driver.quit();
    }
	
	protected void initBasicPageObjects() {
		loginPage = new LoginPage(driver);
		registration = new Registration(driver);
		patientRegistrationDashboard = new PatientRegistrationDashboard(driver);
		patientDashboard = new PatientDashboard(driver);
		appDashboard = new AppDashboard(driver);
	}
	
	protected void createPatient() {
		appDashboard.openPatientRegistrationApp();
		registration.goThruRegistrationProcessWithoutPrintingCard(); 
		testPatient = new Patient(patientRegistrationDashboard.getIdentifier(), patientRegistrationDashboard.getName());
	}
	
	protected void startVisit() throws Exception {
		appDashboard.findPatientById(testPatient.getIdentifier());
		patientDashboard.startVisit();
		 
		Waiter.waitForElementToDisplay(By.cssSelector("div.status-container"), 5, driver);
	}
	
	private static void setupChromeDriver() {
        URL resource = null;
        ClassLoader classLoader = BasicMirebalaisSmokeTest.class.getClassLoader();

        if(SystemUtils.IS_OS_MAC_OSX) {
            resource = classLoader.getResource("chromedriver/mac/chromedriver");
        } else if(SystemUtils.IS_OS_LINUX) {
            resource = classLoader.getResource("chromedriver/linux/chromedriver");
        } else if(SystemUtils.IS_OS_WINDOWS) {
            resource = classLoader.getResource("chromedriver/windows/chromedriver.exe");
        }
        System.setProperty("webdriver.chrome.driver", resource.getPath());
    }

}