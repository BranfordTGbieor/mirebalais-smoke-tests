package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class VitalsApp extends AbstractPageObject {

	public VitalsApp(WebDriver driver) {
		super(driver);
	}

	public void enterPatientIdentifier(String patientID) {
		setClearTextToField("patient-search-field-search", patientID);
		driver.findElement(By.id("ui-id-2")).click();
		driver.findElement(By.id("patient-search-field-search")).sendKeys(Keys.RETURN);
	}

	public void confirmPatient() {
		driver.findElement(By.className("icon-arrow-right")).click();
	}

	public void enterVitals() {
		setClearTextToFieldThruSpan("height", "1.5");
        hitEnterOnInchesField();
		setClearTextToFieldThruSpan("weight", "50");
        hitEnterOnLbsField();
		hitEnterOnBMI();
		setClearTextToFieldThruSpan("temperature_c", "36");
        hitEnterOnFahrenheitField();
		setClearTextToFieldThruSpan("heart_rate", "50");
		setClearTextToFieldThruSpan("respiratory_rate", "50");
		setClearTextToFieldThruSpan("bp_systolic", "120");
		setClearTextToFieldThruSpan("bp_diastolic", "80");
		setClearTextToFieldThruSpan("o2_sat", "50");
		
		driver.findElement(By.id("confirmationQuestion")).findElement(By.className("confirm")).click();
	}

	private void hitEnterOnBMI() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("document.getElementById('hidden-calculated-bmi').setAttribute('type', 'text');");
		driver.findElement(By.id("hidden-calculated-bmi")).clear();
		driver.findElement(By.id("hidden-calculated-bmi")).sendKeys(Keys.RETURN);
	}

    private void hitEnterOnFahrenheitField() {
        driver.findElement(By.id("temperature_f")).sendKeys(Keys.RETURN);
    }

    private void hitEnterOnLbsField() {
        driver.findElement(By.id("weight_lbs")).sendKeys(Keys.RETURN);
    }

    private void hitEnterOnInchesField() {
        driver.findElement(By.id("height_inches")).sendKeys(Keys.RETURN);
    }
	
}
