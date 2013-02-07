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

package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AppDashboard extends AbstractPageObject {

    public static final String ARCHIVES_ROOM = "emr-archivesRoom-app";
    public static final String PATIENT_REGISTRATION = "patientRegistration-registration-app";
    public static final String START_HOSPITAL_VISIT = "patientRegistration-emergencyCheckin-app";
    public static final String START_CLINIC_VISIT = "patientRegistration-checkin-app";
    public static final String EDIT_PATIENT = "patientRegistration-lookup-app";
    public static final String FIND_PATIENT = "emr-findPatient-app";
    public static final String SYSTEM_ADMINISTRATION = "emr-systemAdministration-app";
    public static final String ACTIVE_VISITS = "emr-activeVisits-app";

    public AppDashboard(WebDriver driver) {
        super(driver);
    }

    public void openActiveVisitsApp() {
        openApp(ACTIVE_VISITS);
	}

	public void openArchivesRoomApp() {
        openApp(ARCHIVES_ROOM);
	}

	public void openPatientRegistrationApp() {
        openApp(PATIENT_REGISTRATION);
	}

    public void openStartHospitalVisitApp() {
        openApp(START_HOSPITAL_VISIT);
    }

    public void openStartClinicVisitApp() {
        openApp(START_CLINIC_VISIT);
    }

    public void editPatientApp() {
        openApp(EDIT_PATIENT);
    }

    public void openSysAdminApp() {
        openApp(SYSTEM_ADMINISTRATION);
	}
	
	public boolean isPatientRegistrationAppPresented() {
		return isAppButtonPresent(PATIENT_REGISTRATION);
	}

    public boolean isArchivesRoomAppPresented() {
        return isAppButtonPresent(ARCHIVES_ROOM);
    }
	
	public boolean isSystemAdministrationAppPresented() {
        return isAppButtonPresent(SYSTEM_ADMINISTRATION);
    }
	
	public boolean isFindAPatientAppPresented() {
		return isAppButtonPresent(FIND_PATIENT);
	}
	
	public boolean isActiveVisitsAppPresented() {
		return isAppButtonPresent(ACTIVE_VISITS);
	}

    public List<String> getAppsNames() {
        List<String> appsNames = new ArrayList<String>();
        List<WebElement> apps = driver.findElements(By.xpath("//div[@id='apps']/a"));
        for(WebElement app: apps) {
            appsNames.add(app.getText());
        }
        return appsNames;
    }

    private void clickAppButton(String appId) {
        driver.findElement(By.id(appId)).click();
   }

    private boolean isAppButtonPresent(String appId) {
        try {
            return driver.findElement(By.id(appId)) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    private void openApp(String appIdentifier) {
        driver.get(properties.getWebAppUrl());
        clickAppButton(appIdentifier);
    }
}
