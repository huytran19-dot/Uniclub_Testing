package com.uniclub.base;

import com.uniclub.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Base class for all Page Objects
 * Contains common methods used across all pages
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WaitUtils waitUtils;
    
    /**
     * Constructor
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }
    
    /**
     * Type text into element
     */
    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Click on element
     */
    protected void click(By locator) {
        WebElement element = waitUtils.waitForElementClickable(locator);
        element.click();
    }
    
    /**
     * Get text from element
     */
    protected String getText(By locator) {
        WebElement element = waitUtils.waitForElementVisible(locator);
        return element.getText();
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = waitUtils.waitForElementVisible(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    protected boolean isEnabled(By locator) {
        try {
            WebElement element = waitUtils.waitForElementPresent(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get attribute value from element
     */
    protected String getAttribute(By locator, String attributeName) {
        WebElement element = waitUtils.waitForElementVisible(locator);
        return element.getAttribute(attributeName);
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for URL to contain specific text
     */
    protected void waitForUrlContains(String urlFragment) {
        waitUtils.waitForUrlContains(urlFragment);
    }
    
    /**
     * Wait for page to load completely
     */
    protected void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     */
    protected WebElement waitForClickable(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        WebElement element = waitUtils.waitForElementPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            Thread.sleep(500); // Wait for smooth scroll animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
