package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.CheckIn;
import org.openmrs.module.mirebalais.smoke.pageobjects.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PullRequestScenarioTest extends BasicMirebalaisSmokeTest {

	private CheckIn checkIn;
	
	@Before
    public void setUp() {
        loginPage = new LoginPage(driver);
    	loginPage.logInAsAdmin();

		initBasicPageObjects();
		checkIn = new CheckIn(driver);
	}

	@Test
	public void createsARecord() throws InterruptedException {
		createPatient();

        appDashboard.openStartClinicVisitApp();
		checkIn.checkInPatient(testPatient);
		appDashboard.openArchivesRoomApp();

        WebDriverWait wait = new WebDriverWait(driver, 1000);
        wait.until(ExpectedConditions.textToBePresentInElement(By.id("create_requests_table"), testPatient.getName()));
        wait.until(ExpectedConditions.textToBePresentInElement(By.id("create_requests_table"), testPatient.getIdentifier()));
	}

}