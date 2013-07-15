package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConsultationTest extends DbTest {

    @Before
	public void setUp() throws Exception {
		super.setUp();
		
		initBasicPageObjects();

        login();
		
		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.startVisit();
	}
	
	@Test
	public void addConsultationToAVisitWithoutCheckin() throws Exception {
		patientDashboard.addConsultNoteWithDischarge();
		
		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.CONSULTATION), is(1));
	}
	
	@Test
	public void addConsultationNoteWithDeathAsDispositionClosesVisit() throws Exception {
		patientDashboard.addConsultNoteWithDeath();
		
		assertThat(patientDashboard.isDead(), is(true));
		assertThat(patientDashboard.hasActiveVisit(), is(false));
		assertThat(patientDashboard.startVisitButtonIsVisible(), is(false));
	}
	
	@Test
	public void addEDNote() throws Exception {
		patientDashboard.addEmergencyDepartmentNote();
		
		assertThat(patientDashboard.countEncoutersOfType(PatientDashboard.CONSULTATION), is(1));
	}
}
