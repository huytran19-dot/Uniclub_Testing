package com.uniclub.listeners;

import com.uniclub.base.BaseTest;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * Allure TestNG Listener - Captures screenshots ONLY on test failure
 * This prevents disk space waste from unnecessary passing test screenshots
 * 
 * @author Senior QA Automation Engineer
 * @version 2.0 - Optimized for disk space efficiency
 */
public class AllureTestListener implements ITestListener {

    /**
     * Called when a test fails - captures screenshot and attaches to Allure report
     */
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ Test FAILED: " + result.getName());
        
        // Get the WebDriver instance from the test class
        Object testClass = result.getInstance();
        WebDriver driver = null;
        
        // Extract driver from BaseTest (handles both instance and static driver)
        if (testClass instanceof BaseTest) {
            try {
                // Access the static driver field from BaseTest
                java.lang.reflect.Field driverField = BaseTest.class.getDeclaredField("driver");
                driverField.setAccessible(true);
                Object driverObj = driverField.get(null);
                if (driverObj instanceof WebDriver) {
                    driver = (WebDriver) driverObj;
                }
            } catch (Exception e) {
                System.err.println("⚠️ Failed to get driver from BaseTest: " + e.getMessage());
            }
        }
        
        // Take screenshot ONLY if driver is available
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                
                // Attach to Allure report with descriptive name
                Allure.addAttachment(
                    "🔴 Failure Screenshot: " + result.getName(), 
                    "image/png", 
                    new ByteArrayInputStream(screenshot), 
                    "png"
                );
                
                System.out.println("📸 Screenshot captured and attached to Allure report");
                
            } catch (Exception e) {
                System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            System.err.println("⚠️ Cannot take screenshot - WebDriver is null");
        }
    }
    
    /**
     * Called when a test passes - Captures screenshot for documentation
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test PASSED: " + result.getName());
        
        // Get the WebDriver instance from the test class
        Object testClass = result.getInstance();
        WebDriver driver = null;
        
        // Extract driver from BaseTest
        if (testClass instanceof BaseTest) {
            try {
                java.lang.reflect.Field driverField = BaseTest.class.getDeclaredField("driver");
                driverField.setAccessible(true);
                Object driverObj = driverField.get(null);
                if (driverObj instanceof WebDriver) {
                    driver = (WebDriver) driverObj;
                }
            } catch (Exception e) {
                System.err.println("⚠️ Failed to get driver from BaseTest: " + e.getMessage());
            }
        }
        
        // Take screenshot for successful test
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                
                // Attach to Allure report with success indicator
                Allure.addAttachment(
                    "✅ Success Screenshot: " + result.getName(), 
                    "image/png", 
                    new ByteArrayInputStream(screenshot), 
                    "png"
                );
                
                System.out.println("📸 Success screenshot captured and attached to Allure report");
                
            } catch (Exception e) {
                System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
    
    /**
     * Called when a test is skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️ Test SKIPPED: " + result.getName());
    }
    
    /**
     * Called before any test starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶️ Starting test: " + result.getName());
    }
    
    /**
     * Called when test execution finishes
     */
    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 TEST EXECUTION SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("✅ Passed:  " + passed);
        System.out.println("❌ Failed:  " + failed);
        System.out.println("⏭️ Skipped: " + skipped);
        System.out.println("📁 Total:   " + (passed + failed + skipped));
        System.out.println("=".repeat(60));
        System.out.println("📸 Screenshots: Only " + failed + " captured (failures only)");
        System.out.println("💾 Disk space saved by not capturing " + passed + " passing test screenshots");
        System.out.println("=".repeat(60) + "\n");
    }
}
