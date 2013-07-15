package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.ArchivesRoomApp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ArchivesRoomFlowTest extends DbTest {
	
	private ArchivesRoomApp archivesRoomApp;
	
	@Before
	public void setUp() throws Exception {
		initBasicPageObjects();
		archivesRoomApp = new ArchivesRoomApp(driver);
	}
	
	@Test
	public void requestRecord() throws Exception {
		loginPage.logInAsAdmin();
		
		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.requestRecord();
		
		appDashboard.openArchivesRoomApp();
		
		String dossierNumber = archivesRoomApp.createRecord(testPatient.getIdentifier());
		archivesRoomApp.sendDossier(dossierNumber);
		archivesRoomApp.returnRecord(dossierNumber);
		
		appDashboard.goToPatientPage(testPatient.getId());
		
		assertThat(patientDashboard.getDossierNumber(), is(dossierNumber));
		assertThat(patientDashboard.canRequestRecord(), is(true));
	}
	
}
