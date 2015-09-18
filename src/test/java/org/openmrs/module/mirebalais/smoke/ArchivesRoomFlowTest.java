package org.openmrs.module.mirebalais.smoke;

import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.PatientDatabaseHandler;
import org.openmrs.module.mirebalais.smoke.pageobjects.ArchivesRoomApp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ArchivesRoomFlowTest extends DbTest {
	
	@Test
	public void requestRecord() throws Exception {
        Patient testPatient = PatientDatabaseHandler.insertNewTestPatient();
        initBasicPageObjects();
        ArchivesRoomApp archivesRoomApp = new ArchivesRoomApp(driver);

		logInAsAdmin("Achiv Santral");
		
		appDashboard.goToClinicianFacingDashboard(testPatient.getId());
		clinicianDashboard.requestRecord();
		
		appDashboard.openArchivesRoomApp();
		
		String dossierNumber = archivesRoomApp.createRecord(testPatient.getIdentifier());
		archivesRoomApp.sendDossier(dossierNumber);
		archivesRoomApp.returnRecord(dossierNumber);
		
		appDashboard.goToClinicianFacingDashboard(testPatient.getId());
		
		assertThat(clinicianDashboard.getDossierNumber(), is(dossierNumber));
		assertThat(clinicianDashboard.canRequestRecord(), is(true));
	}
	
}
