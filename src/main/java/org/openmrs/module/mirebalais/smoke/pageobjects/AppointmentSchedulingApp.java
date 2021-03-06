package org.openmrs.module.mirebalais.smoke.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The "appointment scheduling home" that contains links to the sub-apps
 */
public class AppointmentSchedulingApp extends AbstractPageObject {

    public static final String MANAGE_APPOINTMENTS = "appointmentschedulingui-manageAppointments-app";
    public static final String MANAGE_APPOINTMENT_TYPES = "appointmentschedulingui-manageAppointmentTypes-app";
    public static final String DAILY_APPOINTMENTS = "appointmentschedulingui-scheduledAppointments-app";
    public static final String SCHEDULE_PROVIDERS = "appointmentschedulingui-scheduleProviders-app";
    public static final String APPOINTMENT_REQUESTS = "appointmentschedulingui-appointmentRequests-app";

    public AppointmentSchedulingApp(WebDriver driver) {
        super(driver);
    }

    public void openManageAppointmentsApp() {
        openSubApp(MANAGE_APPOINTMENTS);
    }

    public void openDailyAppointmentsApp() {
        openSubApp(DAILY_APPOINTMENTS);
    }

    public void openManageAppointmentTypesApp() {
        openSubApp(MANAGE_APPOINTMENT_TYPES);
    }

    public void openScheduleProvidersApp() {
        openSubApp(SCHEDULE_PROVIDERS);
    }

    public void openAppointmentRequestsApp() { openSubApp(APPOINTMENT_REQUESTS); }

    private void openSubApp(String appIdentifier) {
        driver.findElement(By.id(appIdentifier)).click();
    }

}
