package org.openmrs.module.mirebalais.smoke;

import org.junit.After;
import org.junit.Before;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.SmokeTestDriver;
import org.openmrs.module.mirebalais.smoke.pageobjects.AppDashboard;
import org.openmrs.module.mirebalais.smoke.pageobjects.LoginPage;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientRegistrationDashboard;
import org.openmrs.module.mirebalais.smoke.pageobjects.Registration;
import org.openqa.selenium.WebDriver;

public abstract class BasicMirebalaisSmokeTest {
	
	protected static LoginPage loginPage;
	
	protected WebDriver driver;
	
	protected AppDashboard appDashboard;
	
	protected Registration registration;
	
	protected PatientRegistrationDashboard patientRegistrationDashboard;
	
	protected PatientDashboard patientDashboard;
	
	protected Patient testPatient;
	
	@Before
	public void startWebDriver() {
		driver = new SmokeTestDriver().getDriver();
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
		testPatient = new Patient(patientRegistrationDashboard.getIdentifier(), patientRegistrationDashboard.getName(),
		        null, null, null, null, null, null);
	}
	
	protected void login() {
		new LoginPage(driver).logInAsAdmin();
	}
}
