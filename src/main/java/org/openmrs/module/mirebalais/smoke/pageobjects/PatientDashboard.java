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

import org.openmrs.module.mirebalais.smoke.pageobjects.forms.ConsultNoteForm;
import org.openmrs.module.mirebalais.smoke.pageobjects.forms.EmergencyDepartmentNoteForm;
import org.openmrs.module.mirebalais.smoke.pageobjects.forms.XRayForm;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class PatientDashboard extends AbstractPageObject {

	public static final String CHECKIN = "Tcheke";
	public static final String CONSULTATION = "Consultation";
	public static final String VITALS = "Siy Vito";
	public static final String RADIOLOGY = "Preskripsyon Radyoloji";
	public static final String ACTIVE_VISIT_MESSAGE = "Vizit aktiv";
	public static final String ADMISSION = "Admisyon";
	public static final String TRANSFER = "Transfè";
	private ConsultNoteForm consultNoteForm;
	private EmergencyDepartmentNoteForm eDNoteForm;
    private XRayForm xRayForm;
	private HashMap<String, By> formList;

    private By deleteEncounter = By.cssSelector("i.deleteEncounterId");
    private By confirmDeleteEncounter = By.cssSelector("#delete-encounter-dialog .confirm");
    private By actions = By.cssSelector(".actions");
    private By checkIn = By.cssSelector("i.icon-check-in");
    private By confirmStartVisit = By.cssSelector("#quick-visit-creation-dialog .confirm");

    public PatientDashboard(WebDriver driver) {
		super(driver);
		consultNoteForm = new ConsultNoteForm(driver);
		eDNoteForm = new EmergencyDepartmentNoteForm(driver);
        xRayForm = new XRayForm(driver);
		createFormsMap();
	}

	public void orderXRay(String study1, String study2) {
		openForm(formList.get("Order X-Ray"));

		xRayForm.fillForm(study1, study2);
	}

    public boolean verifyIfSuccessfulMessageIsDisplayed(){
        return (driver.findElement(By.className("icon-ok")) != null);
    }

    public boolean hasActiveVisit() {
		return driver.findElement(By.id("visit-details")).getText().contains(ACTIVE_VISIT_MESSAGE);
	}

	public void deleteFirstEncounter() {
        clickOn(deleteEncounter);
        clickOn(confirmDeleteEncounter);

        wait5seconds.until(invisibilityOfElementLocated(By.id("delete-encounter-dialog")));
	}

    public Integer countEncountersOfType(String encounterName) {
		int count = 0;
		List<WebElement> encounters = driver.findElements(By.cssSelector("span.encounter-name"));
		for (WebElement encounter : encounters) {
	        if (encounter.getText().equals(encounterName))
	        	count++;
	    }
		return count;
	}

	public void startVisit() {
        hoverOn(actions);
        clickOn(checkIn);
        clickOn(confirmStartVisit);

        wait5seconds.until(visibilityOfElementLocated(By.cssSelector(".visit-actions.active-visit")));
    }

	public void addConsultNoteWithDischarge() throws Exception {
		openForm(formList.get("Consult Note"));
		consultNoteForm.fillFormWithDischarge();
	}

	public String addConsultNoteWithAdmissionToLocation(int numbered) throws Exception {
		openForm(formList.get("Consult Note"));
		return consultNoteForm.fillFormWithAdmissionAndReturnLocation(numbered);
	}

	public String addConsultNoteWithTransferToLocation(int numbered) throws Exception {
		openForm(formList.get("Consult Note"));
		return consultNoteForm.fillFormWithTransferAndReturnLocation(numbered);
	}

	public void addConsultNoteWithDeath() throws Exception {
		openForm(formList.get("Consult Note"));
		consultNoteForm.fillFormWithDeath();
	}

    public void addEmergencyDepartmentNote() throws Exception {
		openForm(formList.get("ED Note"));
		eDNoteForm.fillFormWithDischarge();
	}

	public void openForm(By formIdentification) {
		clickOn(formIdentification);
	}

	public void requestRecord() {
		hoverOn(By.cssSelector(".actions"));
		clickOn(By.cssSelector("i.icon-folder-open"));
		clickOn(By.cssSelector("#request-paper-record-dialog .confirm"));
	}

	public String getDossierNumber() {
		List<WebElement> elements = driver.findElements(By.cssSelector(".identifiers span"));
		return elements.get(1).getText();
	}

	public boolean canRequestRecord() {
		hoverOn(By.cssSelector(".actions"));
		return driver.findElement(By.className("icon-folder-open")).isDisplayed();
	}

	public Boolean startVisitButtonIsVisible() {
        try {
            driver.findElement(By.id("noVisitShowVisitCreationDialog"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<WebElement> getVisits() {
        return driver.findElements(By.cssSelector(".menu-item.viewVisitDetails"));
    }

    public boolean isDead() {
        try {
            driver.findElement(By.className("death-message"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void createFormsMap() {
		formList = new HashMap<String, By>();
		formList.put("Consult Note", By.cssSelector("#visit-details a:nth-child(3) .icon-stethoscope"));
		formList.put("Surgical Note", By.cssSelector("#visit-details .icon-paste"));
		formList.put("Order X-Ray", By.className("icon-x-ray"));
		formList.put("ED Note", By.cssSelector("#visit-details a:nth-child(4) .icon-stethoscope"));
	}
}