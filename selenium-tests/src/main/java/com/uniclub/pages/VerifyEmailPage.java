package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * VerifyEmailPage - Page Object for Email OTP Verification page
 * URL: http://localhost:5173/verify-email
 */
public class VerifyEmailPage extends BasePage {
    
    // Locators
    private final By pageTitle = By.xpath("//h2[contains(text(),'Verify Your Email')]");
    private final By emailMessage = By.xpath("//p[contains(text(),'We have sent a 6-digit code to')]");
    private final By otpInput1 = By.id("code-0");
    private final By otpInput2 = By.id("code-1");
    private final By otpInput3 = By.id("code-2");
    private final By otpInput4 = By.id("code-3");
    private final By otpInput5 = By.id("code-4");
    private final By otpInput6 = By.id("code-5");
    private final By verifyButton = By.xpath("//button[contains(text(),'Verify Email')]");
    private final By resendButton = By.xpath("//button[contains(text(),'Gửi lại mã')]");
    private final By errorMessage = By.xpath("//div[contains(@class, 'bg-red-50')]//p");
    private final By successMessage = By.xpath("//div[contains(@class, 'bg-green-50')]//p");
    
    // Constructor
    public VerifyEmailPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on verify email page
     */
    public boolean isOnVerifyEmailPage() {
        try {
            return driver.getCurrentUrl().contains("verify-email");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if page title is displayed
     */
    public boolean isPageTitleDisplayed() {
        return isDisplayed(pageTitle);
    }
    
    /**
     * Check if email message is displayed
     */
    public boolean isEmailMessageDisplayed() {
        return isDisplayed(emailMessage);
    }
    
    /**
     * Enter OTP code (6 digits)
     */
    public VerifyEmailPage enterOTP(String otp) {
        if (otp == null || otp.isEmpty()) {
            return this;
        }
        
        // Enter each digit into corresponding input
        By[] otpInputs = {otpInput1, otpInput2, otpInput3, otpInput4, otpInput5, otpInput6};
        for (int i = 0; i < Math.min(otp.length(), 6); i++) {
            type(otpInputs[i], String.valueOf(otp.charAt(i)));
        }
        return this;
    }
    
    /**
     * Click verify button
     */
    public VerifyEmailPage clickVerify() {
        click(verifyButton);
        return this;
    }
    
    /**
     * Click resend button
     */
    public VerifyEmailPage clickResend() {
        click(resendButton);
        return this;
    }
    
    /**
     * Complete OTP verification (enter OTP and click verify)
     */
    public VerifyEmailPage verifyOTP(String otp) {
        enterOTP(otp);
        clickVerify();
        return this;
    }
    
    /**
     * Check if verify button is enabled
     */
    public boolean isVerifyButtonEnabled() {
        try {
            WebElement button = driver.findElement(verifyButton);
            return button.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if OTP input fields are visible
     */
    public boolean areOTPInputsVisible() {
        try {
            // Check if first OTP input is visible (if first is visible, others should be too)
            WebElement firstInput = driver.findElement(otpInput1);
            return firstInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if resend button is displayed
     */
    public boolean isResendButtonDisplayed() {
        return isDisplayed(resendButton);
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }
}
