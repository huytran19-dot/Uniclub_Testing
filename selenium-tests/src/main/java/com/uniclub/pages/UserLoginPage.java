package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * UserLoginPage - Page Object for User Login page (Web)
 * URL: http://localhost:5173/login
 */
public class UserLoginPage extends BasePage {
    
    // Locators
    private final By emailInput = By.name("email");
    private final By passwordInput = By.name("password");
    private final By submitButton = By.xpath("//button[@type='submit']");
    private final By errorMessage = By.xpath("//div[contains(@style, 'hsl(0 84.2% 60.2%')]");
    private final By pageTitle = By.xpath("//h1[contains(text(),'Chào mừng trở lại')]");
    private final By loginIcon = By.xpath("//*[local-name()='svg' and contains(@class, 'lucide-log-in')]");
    private final By showPasswordButton = By.xpath("//button[@type='button']//*[local-name()='svg']");
    private final By forgotPasswordLink = By.linkText("Quên mật khẩu?");
    private final By registerLink = By.linkText("Đăng ký ngay");
    
    // Constructor
    public UserLoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enter email
     */
    public UserLoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }
    
    /**
     * Enter password
     */
    public UserLoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }
    
    /**
     * Click submit button
     */
    public UserHomePage clickSubmit() {
        try {
            WebElement btn = driver.findElement(submitButton);
            try {
                // Scroll into view to avoid sticky header/overlay interception
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", btn);
            } catch (Exception ignore) {}
            try {
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .until(ExpectedConditions.elementToBeClickable(btn));
            } catch (Exception ignore) {}

            try {
                btn.click();
            } catch (WebDriverException e) {
                // Fallback to JS click if intercepted by overlay or other click issues
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        } catch (Exception e) {
            // Last resort: locate again and JS click
            try {
                WebElement btn = driver.findElement(submitButton);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            } catch (Exception ignore) {}
        }
        return new UserHomePage(driver);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        try {
            return getText(errorMessage);
        } catch (Exception e) {
            return "";
        }
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
     * Check if login icon is displayed
     */
    public boolean isLoginIconDisplayed() {
        return isDisplayed(loginIcon);
    }
    
    /**
     * Toggle password visibility
     */
    public UserLoginPage togglePasswordVisibility() {
        click(showPasswordButton);
        return this;
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }
    
    /**
     * Click register link
     */
    public void clickRegisterLink() {
        click(registerLink);
    }
    
    /**
     * Complete login flow (Method chaining)
     */
    public UserHomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickSubmit();
    }
    
    /**
     * Check if on login page
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("localhost:5173/login") && isPageTitleDisplayed();
    }
    
    /**
     * Wait for page to load
     */
    public UserLoginPage waitForPageToLoad() {
    waitForPageLoad();
    try {
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        new WebDriverWait(driver, Duration.ofSeconds(8))
            .until(ExpectedConditions.presenceOfElementLocated(submitButton));
    } catch (Exception ignore) {}
    return this;
    }
    
    /**
     * Get submit button text
     */
    public String getSubmitButtonText() {
        return getText(submitButton);
    }
    
    /**
     * Check if submit button is enabled
     */
    public boolean isSubmitButtonEnabled() {
        return isEnabled(submitButton);
    }
    
    /**
     * Click login button (alias for clickSubmit)
     */
    public UserHomePage clickLogin() {
        return clickSubmit();
    }
    
    /**
     * Check if login button is enabled (alias for isSubmitButtonEnabled)
     */
    public boolean isLoginButtonEnabled() {
        return isSubmitButtonEnabled();
    }
}
