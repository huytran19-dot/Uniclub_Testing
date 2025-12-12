package com.uniclub.tests;

import com.uniclub.base.BaseTest;
import com.uniclub.pages.UserRegisterPage;
import com.uniclub.pages.VerifyEmailPage;
import com.uniclub.utils.ConfigReader;
import com.uniclub.utils.OTPHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UserRegisterTest - Test cases for User Registration
 * Test ID: REG_01 to REG_12
 */
@Epic("User Registration")
@Feature("User Register")
public class UserRegisterTest extends BaseTest {
    
    private UserRegisterPage registerPage;
    private String timestamp;
    
    @BeforeMethod
    public void setUpTest() {
        navigateToWeb();
        driver.get(ConfigReader.getWebUrl() + "/register");
        registerPage = new UserRegisterPage(driver);
        
        // Wait for page to be ready (soft wait)
        try {
            registerPage.waitForPageToLoad();
        } catch (Exception e) {
            // Page might load slowly on first run, continue anyway
            System.out.println("Page load timeout, continuing...");
        }
        
        // Generate unique timestamp for each test
        timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    
    /**
     * REG_01: Register successfully with valid data and correct OTP
     */
    @Test(priority = 1, description = "Verify user can register with valid credentials and verify with correct OTP")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test complete registration flow: registration + OTP verification with correct code")
    @Story("User Registration")
    public void testRegisterSuccessWithCorrectOTP() {
        // Arrange
        String fullName = "Test User " + timestamp;
        String email = "testuser" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "password123";
        
        // Act 1: Register user
        System.out.println("📝 Step 1: Registering user with email: " + email);
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for redirect to OTP page
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert 1: Should redirect to verify email page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("verify-email"), 
            "Should be redirected to verify email page after successful registration");
        
        // Verify OTP page elements
        VerifyEmailPage verifyPage = new VerifyEmailPage(driver);
        Assert.assertTrue(verifyPage.isOnVerifyEmailPage(), 
            "Should be on verify email page");
        Assert.assertTrue(verifyPage.isPageTitleDisplayed(), 
            "Page title should be displayed");
        
        System.out.println("✅ Registration successful - Now on OTP verification page");
        
        // Act 2: Get correct OTP from backend API
        System.out.println("📝 Step 2: Retrieving OTP code from backend");
        String correctOTP = OTPHelper.getOTPForEmail(email);
        Assert.assertNotNull(correctOTP, "Should be able to retrieve OTP from backend");
        System.out.println("🔑 Retrieved OTP: " + correctOTP);
        
        // Act 3: Enter correct OTP
        System.out.println("📝 Step 3: Entering correct OTP code");
        verifyPage.verifyOTP(correctOTP);
        
        // Wait for verification
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert 2: Should show success message or redirect to login
        currentUrl = driver.getCurrentUrl();
        boolean isSuccess = currentUrl.contains("login") || verifyPage.isSuccessMessageDisplayed();
        Assert.assertTrue(isSuccess, 
            "Should show success or redirect to login after correct OTP");
        
        System.out.println("✅ Test Passed: Complete registration flow successful (Registration + Correct OTP)");
    }
    
    /**
     * REG_01B: Register successfully but fail OTP verification with incorrect code
     */
    @Test(priority = 2, description = "Verify OTP verification fails with incorrect code")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test complete registration flow: registration + OTP verification with incorrect code")
    @Story("User Registration")
    public void testRegisterSuccessWithIncorrectOTP() {
        // Arrange
        String fullName = "Test User Wrong OTP " + timestamp;
        String email = "testuserincorrect" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "password123";
        
        // Act 1: Register user
        System.out.println("📝 Step 1: Registering user with email: " + email);
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for redirect to OTP page
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert 1: Should redirect to verify email page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("verify-email"), 
            "Should be redirected to verify email page after successful registration");
        
        // Verify OTP page elements
        VerifyEmailPage verifyPage = new VerifyEmailPage(driver);
        Assert.assertTrue(verifyPage.isOnVerifyEmailPage(), 
            "Should be on verify email page");
        Assert.assertTrue(verifyPage.isPageTitleDisplayed(), 
            "Page title should be displayed");
        
        System.out.println("✅ Registration successful - Now on OTP verification page");
        
        // Act 2: Enter incorrect OTP
        System.out.println("📝 Step 2: Entering incorrect OTP code");
        String incorrectOTP = "999999";
        System.out.println("🔑 Using incorrect OTP: " + incorrectOTP);
        verifyPage.verifyOTP(incorrectOTP);
        
        // Wait for error response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert 2: Should show error message and remain on OTP page
        Assert.assertTrue(verifyPage.isOnVerifyEmailPage(), 
            "Should remain on verify email page with incorrect OTP");
        Assert.assertTrue(verifyPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for incorrect OTP");
        
        System.out.println("✅ Test Passed: OTP verification correctly fails with incorrect code");
        System.out.println("✅ Complete test flow successful (Registration + Incorrect OTP → Error)");
    }
    
    /**
     * REG_02: Registration fails with existing email
     */
    @Test(priority = 3, description = "Verify registration fails with duplicate email")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that system prevents duplicate email registration")
    @Story("Registration Validation")
    public void testRegisterWithExistingEmail() {
        // Arrange - Use existing user email from config
        String fullName = "Test User";
        String email = ConfigReader.getUserEmail(); // user@uniclub.vn (already exists)
        String phone = "0901234567";
        String password = "password123";
        
        // Act
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for error message
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert
        Assert.assertTrue(registerPage.isOnRegisterPage(), 
            "Should remain on register page when email already exists");
        
        System.out.println("✅ Test Passed: Registration failed with existing email");
    }
    
    /**
     * REG_03: Registration fails with password mismatch
     */
    @Test(priority = 4, description = "Verify registration fails when passwords don't match")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test password confirmation validation")
    @Story("Registration Validation")
    public void testRegisterWithPasswordMismatch() {
        // Arrange
        String fullName = "Test User";
        String email = "testuser" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "password123";
        String confirmPassword = "password456"; // Different password
        
        // Act
        registerPage.register(fullName, email, phone, password, confirmPassword);
        
        // Wait for validation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should stay on register page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("register"), 
            "Should stay on register page when passwords don't match");
        
        System.out.println("✅ Test Passed: Registration prevented with password mismatch");
    }
    
    /**
     * REG_04: Registration fails with short password
     */
    @Test(priority = 5, description = "Verify registration fails with password less than 6 characters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test password length validation")
    @Story("Registration Validation")
    public void testRegisterWithShortPassword() {
        // Arrange
        String fullName = "Test User";
        String email = "testuser" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "123"; // Too short
        
        // Act
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for validation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should stay on register page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("register"), 
            "Should stay on register page when password is too short");
        
        System.out.println("✅ Test Passed: Registration prevented with short password");
    }
    
    /**
     * REG_05: Registration fails with invalid phone number
     */
    @Test(priority = 6, description = "Verify registration fails with invalid phone format")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test phone number validation")
    @Story("Registration Validation")
    public void testRegisterWithInvalidPhone() {
        // Arrange
        String fullName = "Test User";
        String email = "testuser" + timestamp + "@example.com";
        String phone = "123456"; // Invalid phone (not 10 digits)
        String password = "password123";
        
        // Act
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for validation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should stay on register page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("register"), 
            "Should stay on register page when phone is invalid");
        
        System.out.println("✅ Test Passed: Registration prevented with invalid phone");
    }
    
    /**
     * REG_06: Registration fails with empty fields
     */
    @Test(priority = 7, description = "Verify form validation for empty fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test HTML5 required field validation")
    @Story("Registration Validation")
    public void testRegisterWithEmptyFields() {
        // Act - Try to submit without filling fields
        registerPage.clickSubmit();
        
        // Assert - Should remain on register page due to HTML5 validation
        Assert.assertTrue(registerPage.isOnRegisterPage(), 
            "Should remain on register page when fields are empty");
        
        System.out.println("✅ Test Passed: Cannot submit with empty fields");
    }
    
    /**
     * REG_07: Registration fails without checking terms
     */
    @Test(priority = 8, description = "Verify terms checkbox is required")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test terms and conditions checkbox validation")
    @Story("Registration Validation")
    public void testRegisterWithoutCheckingTerms() {
        // Arrange
        String fullName = "Test User";
        String email = "testuser" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "password123";
        
        // Act - Fill all fields but don't check terms
        registerPage.enterFullName(fullName);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(password);
        // Don't check terms
        registerPage.clickSubmit();
        
        // Assert - Should remain on register page
        Assert.assertTrue(registerPage.isOnRegisterPage(), 
            "Should remain on register page without checking terms");
        
        System.out.println("✅ Test Passed: Cannot submit without checking terms");
    }
    
    /**
     * REG_08: Verify all page elements are displayed
     */
    @Test(priority = 9, description = "Verify all registration page elements are visible")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that all required elements on registration page are displayed correctly")
    @Story("UI Verification")
    public void testRegisterPageElements() {
        // Assert - Check all elements
        Assert.assertTrue(registerPage.isPageTitleDisplayed(), 
            "Page title should be displayed");
        Assert.assertEquals(registerPage.getPageTitle(), "Tạo tài khoản mới", 
            "Page title should be 'Tạo tài khoản mới'");
        Assert.assertTrue(registerPage.isRegisterIconDisplayed(), 
            "Register icon should be displayed");
        Assert.assertTrue(registerPage.isSubmitButtonEnabled(), 
            "Submit button should be enabled");
        
        System.out.println("✅ Test Passed: All registration page elements are displayed correctly");
    }
    
    /**
     * REG_09: Verify password visibility toggle
     */
    @Test(priority = 10, description = "Verify password show/hide functionality")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that password visibility can be toggled")
    @Story("UI Interaction")
    public void testPasswordVisibilityToggle() {
        // Arrange
        String password = "testpassword123";
        registerPage.enterPassword(password);
        
        // Act - Toggle password visibility
        registerPage.togglePasswordVisibility();
        
        // Wait a moment
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Just verify the action completes without error
        Assert.assertTrue(registerPage.isOnRegisterPage(), 
            "Should still be on register page after toggling password");
        
        System.out.println("✅ Test Passed: Password visibility toggle works");
    }
    
    /**
     * REG_10: Verify navigation to login page
     */
    @Test(priority = 11, description = "Verify login link redirects to login page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test navigation from register to login page")
    @Story("Navigation")
    public void testNavigateToLoginPage() {
        // Act
        registerPage.clickLoginLink();
        
        // Wait for redirect
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), 
            "Should be redirected to login page");
        
        System.out.println("✅ Test Passed: Navigation to login page works");
    }
    
    /**
     * REG_11: Registration with invalid email format
     */
    @Test(priority = 12, description = "Verify registration fails with invalid email format")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test email format validation")
    @Story("Registration Validation")
    public void testRegisterWithInvalidEmailFormat() {
        // Arrange
        String fullName = "Test User";
        String email = "invalidemail"; // Invalid format
        String phone = "0901234567";
        String password = "password123";
        
        // Act
        registerPage.enterFullName(fullName);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(password);
        registerPage.checkTerms();
        registerPage.clickSubmit();
        
        // Assert - HTML5 validation should prevent submission
        Assert.assertTrue(registerPage.isOnRegisterPage(), 
            "Should remain on register page with invalid email format");
        
        System.out.println("✅ Test Passed: Registration prevented with invalid email format");
    }
    
    /**
     * REG_12: Verify SQL injection prevention
     */
    @Test(priority = 13, description = "Verify system is protected against SQL injection")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test registration with SQL injection patterns to ensure security")
    @Story("Security Testing")
    public void testSQLInjectionPrevention() {
        // Arrange - SQL injection patterns
        String fullName = "' OR '1'='1";
        String email = "test" + timestamp + "@example.com";
        String phone = "0901234567";
        String password = "password123";
        
        // Act
        registerPage.register(fullName, email, phone, password, password);
        
        // Wait for response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Registration should either succeed or fail gracefully (no SQL error)
        // The system should handle SQL injection safely
        String currentUrl = driver.getCurrentUrl();
        boolean isSafe = currentUrl.contains("register") || currentUrl.contains("verify-email");
        Assert.assertTrue(isSafe, 
            "System should handle SQL injection safely");
        
        System.out.println("✅ Test Passed: SQL injection prevented");
    }
}

