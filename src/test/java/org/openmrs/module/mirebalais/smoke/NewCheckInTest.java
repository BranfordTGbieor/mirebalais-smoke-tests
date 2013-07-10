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
import org.junit.Test;
import org.openmrs.module.mirebalais.smoke.pageobjects.NewCheckIn;
import org.openmrs.module.mirebalais.smoke.pageobjects.PatientDashboard;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class NewCheckInTest extends DbTest {
	private NewCheckIn newCheckIn;
    private WebDriverWait wait5Seconds = new WebDriverWait(driver, 5);

    @Before
	public void setUp() throws Exception {
        super.setUp();

		initBasicPageObjects();
		newCheckIn = new NewCheckIn(driver);
	}
    
	@Test
	public void createRetrospectiveCheckInAndRemoveIt() throws Exception {
        loginPage.logInAsAdmin();

        appDashboard.startClinicVisit();
        newCheckIn.checkInPatientFillingTheFormTwice(testPatient.getIdentifier());
        
        assertThat(newCheckIn.isPatientSearchDisplayed(), is(true));

        appDashboard.findPatientById(testPatient.getIdentifier());
        assertThat(patientDashboard.getVisits().size(), is(1));
        assertTrue(patientDashboard.hasActiveVisit());
        assertThat(patientDashboard.countEncouters(PatientDashboard.CHECKIN), is(1));

        patientDashboard.deleteEncounter(PatientDashboard.CHECKIN);

        wait5Seconds.until(invisibilityOfElementLocated(By.id("delete-encounter-dialog")));

        assertThat(patientDashboard.countEncouters(PatientDashboard.CHECKIN), is(0));
        assertTrue(patientDashboard.hasActiveVisit());
    }

}
