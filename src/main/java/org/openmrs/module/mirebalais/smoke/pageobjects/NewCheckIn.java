package org.openmrs.module.mirebalais.smoke.pageobjects;

import java.util.List;

import org.openmrs.module.mirebalais.smoke.helper.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NewCheckIn extends AbstractPageObject {
	
	private static final String CONFIRM_TEXT = "Konfime";
	private static final String PAYMENT_EXEMPT = "Exempt";
	private static final String PAYMENT_50 = "50";
    private static final String PHARMACY_VISIT = "Famasi sèlman";
    private static final String NON_CLINIC_VISIT = "Nan klinik";
	
	public NewCheckIn(WebDriver driver) {
		super(driver);
	}
	
	public void checkInPatient(String patientIdentifier) throws Exception {
		findPatient(patientIdentifier);
		confirmRightPatient();
		clickOnPaymentOption(PAYMENT_50);
		confirmData();
		confirmPopup();
	}

	public void checkInPatientFillingTheFormTwice(String patientIdentifier) throws Exception {
		findPatient(patientIdentifier);
		confirmRightPatient();
        clickOn(By.cssSelector(".question-legend:nth-child(2)")); // skip date selection for now
        clickOnPaymentOption(PAYMENT_50);
        clickOnNoButton();
        clickOn(By.cssSelector(".question-legend:nth-child(2)")); // skip date selection for now
        clickOnPaymentOption(PAYMENT_EXEMPT);
		confirmData();
		confirmPopup();
	}
	
	private void findPatient(String patientIdentifier) throws Exception {
		super.findPatientById(patientIdentifier, "patient-search-field-search");
	}

	private void confirmRightPatient() {
		clickOn(By.className("icon-arrow-right"));
	}
	
	private void clickOnPaymentOption(String payment) throws Exception{
		clickOnOptionLookingForText(payment, By.cssSelector("#paymentAmount option"));
        //click on Confirm left menu
        WebElement element=driver.findElement(By.xpath("//*[@id=\"formBreadcrumb\"]/li[2]/span"));
        new Actions(driver).moveToElement(element).click().perform();
	}

    private void clickOnVisitTypeOption(String visitType) throws Exception{
        clickOnOptionLookingForText(visitType, By.cssSelector("#typeOfVisit option"));
        //click on Payment left menu
        WebElement element=driver.findElement(By.xpath("//*[@id=\"formBreadcrumb\"]/li[1]/ul/li[2]/span"));
        new Actions(driver).moveToElement(element).click().perform();
    }
	
	private void confirmData() {
		clickOnConfirmationTab();
		clickOn(By.cssSelector("#confirmationQuestion input.confirm"));
	}
	
	private void clickOnNoButton() {
		clickOnConfirmationTab();
		clickOn(By.cssSelector("#confirmationQuestion input.cancel"));
	}
	
	private void clickOnConfirmationTab() {
		List<WebElement> elements = driver.findElements(By.cssSelector("#formBreadcrumb span"));
		for (WebElement element : elements) {
	        if(element.getText().contains(CONFIRM_TEXT)) {
	        	element.click();
	        }
	    }
	}
	
	private void confirmPopup() {
		Waiter.waitForElementToDisplay(By.id("request-paper-record-dialog"), 20, driver);
		clickOn(By.cssSelector("#request-paper-record-dialog button"));
	}

	public boolean isPatientSearchDisplayed() {
		return driver.findElement(By.id("patient-search-field-search")).isDisplayed();
	}
	
}	