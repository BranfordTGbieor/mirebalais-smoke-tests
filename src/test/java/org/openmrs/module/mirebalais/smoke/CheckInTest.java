/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.mirebalais.smoke;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.PatientDatabaseHandler;
import org.openmrs.module.mirebalais.smoke.pageobjects.CheckInFormPage;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class CheckInTest extends DbTest {

    public CheckInFormPage newCheckIn;
    public Patient testPatient;

    @BeforeClass
    public static void prepare() throws Exception {
        logInAsAdmin();
    }

    @Before
    public void setUp() throws Exception {
        testPatient = PatientDatabaseHandler.insertNewTestPatient();
        initBasicPageObjects();
        newCheckIn = new CheckInFormPage(driver);
        appDashboard.startClinicVisit();

    }
	
	@Test
	public void createCheckInAndRemoveIt() throws Exception {

        newCheckIn.enterInfoFillingTheFormTwice(testPatient.getIdentifier());

		assertThat(newCheckIn.isPatientSearchDisplayed(), is(true));
		
		appDashboard.goToPatientPage(testPatient.getId());
		assertThat(patientDashboard.getVisits().size(), is(1));
		assertTrue(patientDashboard.hasActiveVisit());
		assertThat(patientDashboard.countEncountersOfType(PatientDashboard.CHECKIN_CREOLE_NAME), is(1));
		
		patientDashboard.deleteFirstEncounter();
		
		assertThat(patientDashboard.countEncountersOfType(PatientDashboard.CHECKIN_CREOLE_NAME), is(0));
		assertTrue(patientDashboard.hasActiveVisit());
	}

}
