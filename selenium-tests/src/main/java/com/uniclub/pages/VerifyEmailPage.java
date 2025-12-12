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
    private final By pageTitle = By.xpath("//h1[contains(text(),'Xác thực email')]");
    private final By emailMessage = By.xpath("//p[contains(text(),'Chúng tôi đã gửi')]");
    private final By otpInput1 = By.name("otp-0");
    private final By otpInput2 = By.name("otp-1");
    private final By otpInput3 = By.name("otp-2");
    private final By otpInput4 = By.name("otp-3");
    private final By otpInput5 = By.name("otp-4");
    private final By otpInput6 = By.name("otp-5");
    private final By verifyButton = By.xpath("//button[contains(text(),'Xác thực')]");
    private final By resendButton = By.xpath("//button[contains(text(),'Gửi lại')]");
    private final By errorMessage = By.xpath("//div[contains(@class, 'error') or contains(@style, 'color: red')]");
    private final By successMessage = By.xpath("//div[contains(@class, 'success') or contains(text(),'thành công')]");
    
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
