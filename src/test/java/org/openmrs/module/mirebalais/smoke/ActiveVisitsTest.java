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
import org.openqa.selenium.By;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class ActiveVisitsTest extends DbTest {
	
	@Before
	public void setUp() {
		initBasicPageObjects();
	}
	
	@Test
	public void shouldShowActiveVisitAfterStartVisit() throws Exception {
		loginPage.logInAsAdmin();
		
		appDashboard.openActiveVisitsApp();
		assertFalse(driver.findElement(By.id("content")).getText().contains(testPatient.getIdentifier()));
		
		appDashboard.goToPatientPage(testPatient.getId());
		patientDashboard.startVisit();
		
		appDashboard.openActiveVisitsApp();
		String contentText = driver.findElement(By.id("content")).getText();
		assertThat(contentText, containsString(testPatient.getName()));
		assertThat(contentText, containsString(testPatient.getIdentifier()));
	}
}
