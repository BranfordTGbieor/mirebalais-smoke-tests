package org.openmrs.module.mirebalais.smoke;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.reflections.Reflections;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SmokeTestsRunner {

    private static List<Failure> failureDescriptions = new LinkedList<Failure>();
    private static int run = 0;
    private static int failures = 0;
    private static int errors = 0;
    private static int skipped = 0;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newCachedThreadPool();

        Reflections reflections = new Reflections("org.openmrs.module.mirebalais.smoke");

        for (final Class<? extends BasicMirebalaisSmokeTest> test : reflections.getSubTypesOf(DbTest.class)) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Result result = new JUnitCore().run(test);
                    failureDescriptions.addAll(result.getFailures());
                    printTestResult(result, test.getCanonicalName());
                    return;
                }
            });
        }

        service.shutdown();
        boolean finished = service.awaitTermination(10, TimeUnit.MINUTES);

        printFinalResult();
    }

    private static void printTestResult(Result result, String testCanonicalName) {
        run += result.getRunCount();
        errors += result.getFailureCount();
        failures += result.getFailureCount();
        skipped += result.getIgnoreCount();

        System.out.println("Finished running: " + testCanonicalName);
        System.out.println("Tests run: " + result.getRunCount() +
                ", Failures: " + result.getFailureCount() +
                ", Errors: " + result.getFailureCount() +
                ", Skipped: " + result.getIgnoreCount() +
                ", Time elapsed: " + (float) result.getRunTime() / 1000 + " sec");

        for (Failure failure : result.getFailures()) {
            System.out.println("Failed test: " + failure.getTestHeader());
            System.out.println(failure.getTrace());
        }
    }

    private static void printFinalResult() {
        System.out.println("\nResults:\n");

        System.out.println("\nTests run: " + run +
                ", Failures: " + failures +
                ", Errors: " + errors +
                ", Skipped: " + skipped + "\n");

        if (failures > 0) {
            System.out.println("Failed tests:");

            for (Failure failure : failureDescriptions) {
                System.out.println("    " + failure.getTestHeader());
            }

            throw new RuntimeException("There were test failures");
        }
    }
}