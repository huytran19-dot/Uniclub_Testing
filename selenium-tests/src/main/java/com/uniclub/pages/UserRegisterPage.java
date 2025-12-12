package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * UserRegisterPage - Page Object for User Registration page
 * URL: http://localhost:5173/register
 */
public class UserRegisterPage extends BasePage {
    
    // Locators
    private final By fullNameInput = By.name("full_name");
    private final By emailInput = By.name("email");
    private final By phoneInput = By.name("phone");
    private final By passwordInput = By.name("password");
    private final By confirmPasswordInput = By.name("confirmPassword");
    private final By showPasswordButton = By.xpath("(//button[@type='button']//*[local-name()='svg'])[1]");
    private final By showConfirmPasswordButton = By.xpath("(//button[@type='button']//*[local-name()='svg'])[2]");
    private final By termsCheckbox = By.xpath("//input[@type='checkbox']");
    private final By submitButton = By.xpath("//button[@type='submit']");
    private final By errorMessage = By.xpath("//div[contains(@style, 'hsl(0 84.2% 60.2%')]");
    private final By pageTitle = By.xpath("//h1[contains(text(),'Tạo tài khoản mới')]");
    private final By registerIcon = By.xpath("//*[local-name()='svg' and contains(@class, 'lucide-user-plus')]");
    private final By loginLink = By.linkText("Đăng nhập ngay");
    private final By termsLink = By.linkText("Điều khoản dịch vụ");
    private final By privacyLink = By.linkText("Chính sách bảo mật");
    
    // Constructor
    public UserRegisterPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enter full name
     */
    public UserRegisterPage enterFullName(String fullName) {
        type(fullNameInput, fullName);
        return this;
    }
    
    /**
     * Enter email
     */
    public UserRegisterPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }
    
    /**
     * Enter phone
     */
    public UserRegisterPage enterPhone(String phone) {
        type(phoneInput, phone);
        return this;
    }
    
    /**
     * Enter password
     */
    public UserRegisterPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }
    
    /**
     * Enter confirm password
     */
    public UserRegisterPage enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
        return this;
    }
    
    /**
     * Toggle password visibility
     */
    public UserRegisterPage togglePasswordVisibility() {
        click(showPasswordButton);
        return this;
    }
    
    /**
     * Toggle confirm password visibility
     */
    public UserRegisterPage toggleConfirmPasswordVisibility() {
        click(showConfirmPasswordButton);
        return this;
    }
    
    /**
     * Check terms and conditions
     */
    public UserRegisterPage checkTerms() {
        click(termsCheckbox);
        return this;
    }
    
    /**
     * Click submit button
     */
    public void clickSubmit() {
        click(submitButton);
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
     * Check if register icon is displayed
     */
    public boolean isRegisterIconDisplayed() {
        return isDisplayed(registerIcon);
    }
    
    /**
     * Click login link
     */
    public void clickLoginLink() {
        scrollToElement(loginLink);
        click(loginLink);
    }
    
    /**
     * Complete registration flow (Method chaining)
     */
    public void register(String fullName, String email, String phone, String password, String confirmPassword) {
        enterFullName(fullName);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        // Terms checkbox is required by HTML5, Selenium will check it automatically
        try {
            checkTerms();
        } catch (Exception e) {
            System.out.println("Terms checkbox already checked or not needed");
        }
        clickSubmit();
    }
    
    /**
     * Check if on register page
     */
    public boolean isOnRegisterPage() {
        return getCurrentUrl().contains("localhost:5173/register") && isPageTitleDisplayed();
    }
    
    /**
     * Wait for page to load
     */
    public UserRegisterPage waitForPageToLoad() {
        waitForPageLoad();
        waitUtils.waitForElementVisible(pageTitle);
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
     * Check if terms checkbox is checked
     */
    public boolean isTermsChecked() {
        try {
            return driver.findElement(termsCheckbox).isSelected();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get password input type (to verify show/hide)
     */
    public String getPasswordInputType() {
        return getAttribute(passwordInput, "type");
    }
    
    /**
     * Clear all form fields
     */
    public UserRegisterPage clearAllFields() {
        driver.findElement(fullNameInput).clear();
        driver.findElement(emailInput).clear();
        driver.findElement(phoneInput).clear();
        driver.findElement(passwordInput).clear();
        driver.findElement(confirmPasswordInput).clear();
        return this;
    }
}
