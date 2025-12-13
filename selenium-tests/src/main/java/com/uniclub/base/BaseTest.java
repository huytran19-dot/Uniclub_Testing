package com.uniclub.base;

import com.uniclub.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * BaseTest - Parent class for all Test classes
 * Contains setup and teardown methods
 */
public class BaseTest {
    
    protected static WebDriver driver;
    
    /**
     * SINGLETON PATTERN - Initialize driver ONCE for all tests in the class
     * This dramatically improves performance by avoiding repeated browser launches
     */
    @BeforeClass
    public void setUpClass() {
        System.out.println("=== Initializing browser session (ONCE for all tests) ===");
        
        // Initialize driver based on config
        driver = initializeDriver(ConfigReader.getBrowser());
        
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(ConfigReader.getPageLoadTimeout())
        );
        
        // Maximize window
        driver.manage().window().maximize();
    }
    
    /**
     * CLEAN STATE BETWEEN TESTS - Reset browser state without closing it
     * Clears cookies, local storage, and navigates to base URL
     */
    @AfterMethod
    public void cleanUpAfterTest() {
        if (driver != null) {
            try {
                // Add delay to see test result before cleanup (for visual debugging)
                Thread.sleep(1500);
                
                // Clear cookies and storage to ensure test isolation
                driver.manage().deleteAllCookies();
                
                // Clear local storage and session storage
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "window.localStorage.clear(); window.sessionStorage.clear();"
                );
                
                System.out.println("✓ Browser state cleaned (cookies/storage cleared)");
                System.out.println("⏭️  Preparing for next test...\n");
                
            } catch (Exception e) {
                System.err.println("Warning: Failed to clean browser state: " + e.getMessage());
            }
        }
    }
    
    /**
     * TEARDOWN - Close browser ONCE after ALL tests complete
     * This is called only once at the end of the test class execution
     */
    @AfterClass
    public void tearDownClass() {
        System.out.println("=== Closing browser session (test class completed) ===");
        
        if (driver != null) {
            try {
                // Step 1: Close all windows explicitly
                for (String windowHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
                
                // Step 2: Quit the WebDriver session completely
                driver.quit();
                
                // Step 3: Brief wait to ensure clean shutdown
                Thread.sleep(500);
                
                System.out.println("✓ Browser closed successfully");
                
            } catch (Exception e) {
                System.err.println("Error during teardown: " + e.getMessage());
                // Force quit if normal quit fails
                try {
                    driver.quit();
                } catch (Exception ex) {
                    System.err.println("Force quit also failed: " + ex.getMessage());
                }
            } finally {
                // Step 4: Nullify driver reference for garbage collection
                driver = null;
            }
        }
    }
    
    /**
     * Initialize WebDriver based on browser type
     */
    private WebDriver initializeDriver(String browser) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (ConfigReader.isHeadless()) {
                    chromeOptions.addArguments("--headless");
                }
                
                // Force kill all Chrome processes before starting
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-logging");
                chromeOptions.addArguments("--log-level=3");
                chromeOptions.addArguments("--silent");
                
                // Disable notifications and popups
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                
                // Disable automation info bars and password save prompts
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "enable-logging"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                
                // Disable "Chrome is being controlled" message
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                
                // Prevent multiple tabs/windows - CRITICAL FIX
                chromeOptions.addArguments("--no-first-run");
                chromeOptions.addArguments("--no-default-browser-check");
                chromeOptions.addArguments("--start-maximized");
                
                // ⚠️ CRITICAL: Use D drive for ALL Chrome data to prevent C drive from filling up
                // This stores user profiles, cache, temp files on D drive instead of C:\Users\...\AppData
                chromeOptions.addArguments("--user-data-dir=D:\\temp\\selenium-chrome-profile");
                chromeOptions.addArguments("--disk-cache-dir=D:\\temp\\chrome-cache");
                chromeOptions.addArguments("--force-device-scale-factor=1");
                
                // Set preferences to disable password manager
                chromeOptions.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
                    put("credentials_enable_service", false);
                    put("profile.password_manager_enabled", false);
                }});
                
                // Add small delay before creating driver to avoid connection reset on first run
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (ConfigReader.isHeadless()) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (ConfigReader.isHeadless()) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Browser '" + browser + "' is not supported");
        }
        
        return driver;
    }
    
    /**
     * Take screenshot
     */
    protected String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = ConfigReader.getScreenshotPath() + fileName;
        
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("Screenshot saved: " + filePath);
            return filePath;
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Navigate to Admin page
     */
    @Step("Navigate to Admin page: {0}")
    protected void navigateToAdmin() {
        String url = ConfigReader.getAdminUrl();
        driver.get(url);
        Allure.addAttachment("Admin URL", url);
    }
    
    /**
     * Navigate to Web page
     */
    @Step("Navigate to Web page: {0}")
    protected void navigateToWeb() {
        String url = ConfigReader.getWebUrl();
        driver.get(url);
        Allure.addAttachment("Web URL", url);
    }
}
