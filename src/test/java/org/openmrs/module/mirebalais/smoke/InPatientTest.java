package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.InPatientList;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InPatientTest extends DbTest {

    private InPatientList inPatientList;
	
	@Before
	public void setUp() throws Exception {
		initBasicPageObjects();
		inPatientList = new InPatientList(driver);
    }
	
	@Test
	public void admitPatientTransferInsideHospitalAndFilterWard() throws Exception {
		login();

		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.startVisit();

        String admissionPlace = patientDashboard.addConsultNoteWithAdmission();

		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.CONSULTATION), is(1));
		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.ADMISSION), is(1));
		assertFirstAdmittedAndCurrentWardAre(admissionPlace, admissionPlace);

		appDashboard.goToPatientPage(testPatient.getId());

		String transferPlace = patientDashboard.addConsultNoteWithTransfer();

		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.CONSULTATION), is(2));
		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.TRANSFER), is(1));
		assertFirstAdmittedAndCurrentWardAre(admissionPlace, transferPlace);

		inPatientList.filterBy(transferPlace);

        assertThat(inPatientList.isListFilteredBy(transferPlace), is(true));
	}

    private void assertFirstAdmittedAndCurrentWardAre(String firstAdmitted, String currentWard) throws Exception {
		appDashboard.openInPatientApp();
		
		assertThat(inPatientList.getCurrentWard(testPatient.getIdentifier()), is(currentWard));
		assertThat(inPatientList.getFirstAdmitted(testPatient.getIdentifier()), is(firstAdmitted));
	}
}
