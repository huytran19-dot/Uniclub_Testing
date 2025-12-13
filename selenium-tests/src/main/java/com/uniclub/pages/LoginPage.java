package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage - Page Object for Login page
 * URL: http://localhost:5174 (Admin Login)
 */
public class LoginPage extends BasePage {
    
    // Locators
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".text-red-600");
    private final By pageTitle = By.xpath("//h2[contains(text(),'Admin Login')]");
    private final By demoCredentials = By.xpath("//p[contains(text(),'Demo credentials')]");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enter email
     */
    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }
    
    /**
     * Click Login button
     */
    public DashboardPage clickLogin() {
        click(loginButton);
        return new DashboardPage(driver);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    /**
     * Check if page title is displayed
     */
    public boolean isPageTitleDisplayed() {
        return isDisplayed(pageTitle);
    }
    
    /**
     * Get page title text
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Check if demo credentials are displayed
     */
    public boolean isDemoCredentialsDisplayed() {
        return isDisplayed(demoCredentials);
    }
    
    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton);
    }
    
    /**
     * Get login button text
     */
    public String getLoginButtonText() {
        return getText(loginButton);
    }
    
    /**
     * Complete login flow (Method chaining)
     */
    public DashboardPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLogin();
    }
    
    /**
     * Clear email field
     */
    public LoginPage clearEmail() {
        driver.findElement(emailInput).clear();
        return this;
    }
    
    /**
     * Clear password field
     */
    public LoginPage clearPassword() {
        driver.findElement(passwordInput).clear();
        return this;
    }
    
    /**
     * Check if on login page
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("localhost:5174") && isPageTitleDisplayed();
    }
    
    /**
     * Get email input placeholder
     */
    public String getEmailPlaceholder() {
        return getAttribute(emailInput, "placeholder");
    }
    
    /**
     * Get password input placeholder
     */
    public String getPasswordPlaceholder() {
        return getAttribute(passwordInput, "placeholder");
    }
    
    /**
     * Wait for page to load
     */
    public LoginPage waitForPageToLoad() {
        waitForPageLoad();
        waitUtils.waitForElementVisible(pageTitle);
        return this;
    }
}
