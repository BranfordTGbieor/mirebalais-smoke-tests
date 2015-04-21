package org.openmrs.module.mirebalais.smoke;

import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.PatientDatabaseHandler;
import org.openmrs.module.mirebalais.smoke.pageobjects.AwaitingAdmissionApp;
import org.openmrs.module.mirebalais.smoke.pageobjects.InPatientList;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InPatientTest extends DbTest {

    private static final String PRIMARY_DIAGNOSIS = "IGU";

	private InPatientList inPatientList;

    private AwaitingAdmissionApp awaitingAdmissionApp;
	
	@Test
	public void admitPatientTransferInsideHospitalAndFilterWard() throws Exception {
		Patient testPatient = PatientDatabaseHandler.insertNewTestPatient();
		Patient testPatient2 = PatientDatabaseHandler.insertNewTestPatient();
		initBasicPageObjects();

		inPatientList = new InPatientList(driver);
        awaitingAdmissionApp = new AwaitingAdmissionApp(driver);

        logInAsPhysicianUser("Sal Gason");

		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.startVisit();

		String admissionPlace = patientDashboard.addConsultNoteWithAdmissionToLocation(PRIMARY_DIAGNOSIS, 3);
		assertThat(patientDashboard.countEncountersOfType(PatientDashboard.CONSULTATION_CREOLE_NAME), is(1));

        patientDashboard.addAdmissionNoteWithDefaultLocation(PRIMARY_DIAGNOSIS);
        assertThat(patientDashboard.countEncountersOfType(PatientDashboard.ADMISSION_CREOLE_NAME), is(1));

        patientDashboard.gotoAppDashboard();
		assertFirstAdmittedAndCurrentWardAre(testPatient.getIdentifier(), admissionPlace, admissionPlace);

		appDashboard.goToPatientPage(testPatient.getId());

		String transferPlace = patientDashboard.addConsultNoteWithTransferToLocation(PRIMARY_DIAGNOSIS, 4);

		assertThat(patientDashboard.countEncountersOfType(PatientDashboard.CONSULTATION_CREOLE_NAME), is(2));
		assertThat(patientDashboard.countEncountersOfType(PatientDashboard.TRANSFER_CREOLE_NAME), is(1));
        patientDashboard.gotoAppDashboard();
		assertFirstAdmittedAndCurrentWardAre(testPatient.getIdentifier(), admissionPlace, transferPlace);

		appDashboard.goToPatientPage(testPatient2.getId());
		patientDashboard.startVisit();
		patientDashboard.addConsultNoteWithAdmissionToLocation(PRIMARY_DIAGNOSIS, 3);
		
		appDashboard.openInPatientApp();
		inPatientList.filterBy(transferPlace);
		inPatientList.waitUntilInpatientListIsFilteredBy(transferPlace);
	}
	
	private void assertFirstAdmittedAndCurrentWardAre(String identifier, String firstAdmitted, String currentWard)
	        throws Exception {
		appDashboard.openInPatientApp();
		
		assertThat(inPatientList.getCurrentWard(identifier), is(currentWard));
		assertThat(inPatientList.getFirstAdmitted(identifier), is(firstAdmitted));
	}
}
