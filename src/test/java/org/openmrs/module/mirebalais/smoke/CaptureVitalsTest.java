package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;
import org.openmrs.module.mirebalais.smoke.pageobjects.VitalsApp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CaptureVitalsTest extends DbTest {
	
	private VitalsApp vitals;
	
	@Before
	public void setUp() throws Exception {
		initBasicPageObjects();
		vitals = new VitalsApp(driver);
	}
	
	@Test
	public void checkInAndCaptureVitalsThruVitalsApp() throws Exception {
		loginPage.logInAsAdmin();
		
		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.startVisit();
		
		appDashboard.openCaptureVitalsApp();
		vitals.captureVitalsForPatient(testPatient.getIdentifier());
		assertThat(vitals.isSearchPatientDisplayed(), is(true));
		
		appDashboard.goToPatientPage(testPatient.getId());
		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.VITALS), is(1));
	}
	
}
