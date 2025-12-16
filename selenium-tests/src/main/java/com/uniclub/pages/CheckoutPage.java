package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * CheckoutPage - Page Object for Checkout page
 * URL: http://localhost:5173/checkout
 */
public class CheckoutPage extends BasePage {
    
    // Locators - Billing Details (match AddressForm.jsx)
    private final By fullNameInput = By.name("full_name");
    private final By emailInput = By.name("email");
    private final By phoneInput = By.name("phone");
    private final By addressInput = By.name("address");
    private final By provinceSelect = By.name("province");
    private final By districtSelect = By.name("district");
    private final By wardSelect = By.name("ward");
    private final By noteTextarea = By.name("note");
    
    // Locators - Payment Method
    private final By codRadio = By.xpath("//input[@type='radio' and @value='COD']");
    private final By vnpayRadio = By.xpath("//input[@type='radio' and @value='VNPay']");
    
    // Locators - Buttons
    private final By placeOrderButton = By.xpath("//button[@type='submit']");
    private final By backToCartButton = By.xpath("//a[contains(@href,'/cart')]");
    
    // Locators - Empty Cart Message
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'Giỏ hàng trống')]");
    
    // Locators - Order Summary
    private final By orderSummary = By.xpath("//h3[contains(text(),'Đơn hàng của bạn')]");
    private final By totalAmount = By.xpath("//*[contains(text(),'Tổng cộng')]//following-sibling::*");
    
    // Locators - Error/Validation Messages
    private final By errorMessage = By.xpath("//div[contains(@class, 'bg-red')]");
    private final By validationError = By.xpath("//input:invalid");
    
    // Constructor
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on checkout page
     */
    public boolean isOnCheckoutPage() {
        return driver.getCurrentUrl().contains("/checkout");
    }
    
    /**
     * Check if showing empty cart message
     */
    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(emptyCartMessage);
    }
    
    /**
     * Fill billing details - note: province/district/ward are pre-populated from user profile
     * Only fills basic text fields
     */
    public void fillBillingDetails(String fullName, String email, String phone, String address) {
        waitForPageToLoad();
        if (fullName != null && !fullName.isEmpty()) {
            try {
                type(fullNameInput, fullName);
            } catch (Exception e) {
                System.out.println("⚠️ Could not fill fullName - may be pre-populated");
            }
        }
        if (email != null && !email.isEmpty()) {
            try {
                type(emailInput, email);
            } catch (Exception e) {
                System.out.println("⚠️ Could not fill email - may be pre-populated");
            }
        }
        if (phone != null && !phone.isEmpty()) {
            try {
                type(phoneInput, phone);
            } catch (Exception e) {
                System.out.println("⚠️ Could not fill phone - may be pre-populated");
            }
        }
        if (address != null && !address.isEmpty()) {
            try {
                type(addressInput, address);
            } catch (Exception e) {
                System.out.println("⚠️ Could not fill address - may be pre-populated");
            }
        }
    }
    
    /**
     * Select province from dropdown
     */
    public void selectProvince(String provinceName) {
        try {
            WebElement selectElement = driver.findElement(provinceSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(provinceName);
            Thread.sleep(1000); // Wait for districts to load
        } catch (Exception e) {
            System.out.println("⚠️ Could not select province: " + e.getMessage());
        }
    }
    
    /**
     * Select district from dropdown
     */
    public void selectDistrict(String districtName) {
        try {
            WebElement selectElement = driver.findElement(districtSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(districtName);
            Thread.sleep(1000); // Wait for wards to load
        } catch (Exception e) {
            System.out.println("⚠️ Could not select district: " + e.getMessage());
        }
    }
    
    /**
     * Select ward from dropdown
     */
    public void selectWard(String wardName) {
        try {
            WebElement selectElement = driver.findElement(wardSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(wardName);
        } catch (Exception e) {
            System.out.println("⚠️ Could not select ward: " + e.getMessage());
        }
    }
    
    /**
     * Select first available province (index 1 - skip placeholder)
     * @return true if selection successful
     */
    public boolean selectFirstAvailableProvince() {
        try {
            WebElement selectElement = driver.findElement(provinceSelect);
            Select select = new Select(selectElement);
            
            // Wait for options to load (provinces are loaded async from API)
            for (int i = 0; i < 10; i++) {
                if (select.getOptions().size() > 1) break;
                Thread.sleep(500);
            }
            
            java.util.List<WebElement> options = select.getOptions();
            if (options.size() > 1) {
                // Select index 1 (skip "Chọn tỉnh/thành phố" placeholder at index 0)
                select.selectByIndex(1);
                String selectedText = select.getFirstSelectedOption().getText();
                System.out.println("✅ Selected province: " + selectedText);
                Thread.sleep(1500); // Wait for districts to load
                return true;
            } else {
                System.out.println("⚠️ No provinces available to select");
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not select province: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Select first available district
     * @return true if selection successful
     */
    public boolean selectFirstAvailableDistrict() {
        try {
            WebElement selectElement = driver.findElement(districtSelect);
            
            // Wait for element to be enabled (dependent on province selection)
            for (int i = 0; i < 10; i++) {
                if (selectElement.isEnabled()) break;
                Thread.sleep(500);
                selectElement = driver.findElement(districtSelect);
            }
            
            if (!selectElement.isEnabled()) {
                System.out.println("⚠️ District select is disabled");
                return false;
            }
            
            Select select = new Select(selectElement);
            
            // Wait for options to load
            for (int i = 0; i < 10; i++) {
                if (select.getOptions().size() > 1) break;
                Thread.sleep(500);
            }
            
            java.util.List<WebElement> options = select.getOptions();
            if (options.size() > 1) {
                select.selectByIndex(1);
                String selectedText = select.getFirstSelectedOption().getText();
                System.out.println("✅ Selected district: " + selectedText);
                Thread.sleep(1500); // Wait for wards to load
                return true;
            } else {
                System.out.println("⚠️ No districts available to select");
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not select district: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Select first available ward
     * @return true if selection successful
     */
    public boolean selectFirstAvailableWard() {
        try {
            WebElement selectElement = driver.findElement(wardSelect);
            
            // Wait for element to be enabled (dependent on district selection)
            for (int i = 0; i < 10; i++) {
                if (selectElement.isEnabled()) break;
                Thread.sleep(500);
                selectElement = driver.findElement(wardSelect);
            }
            
            if (!selectElement.isEnabled()) {
                System.out.println("⚠️ Ward select is disabled");
                return false;
            }
            
            Select select = new Select(selectElement);
            
            // Wait for options to load
            for (int i = 0; i < 10; i++) {
                if (select.getOptions().size() > 1) break;
                Thread.sleep(500);
            }
            
            java.util.List<WebElement> options = select.getOptions();
            if (options.size() > 1) {
                select.selectByIndex(1);
                String selectedText = select.getFirstSelectedOption().getText();
                System.out.println("✅ Selected ward: " + selectedText);
                return true;
            } else {
                System.out.println("⚠️ No wards available to select");
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not select ward: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Fill all required address fields (unconditionally)
     * This ensures all required fields have values for form submission
     */
    public void fillAllRequiredFields(String phone, String address) {
        waitForPageToLoad();
        
        // Always fill phone if provided
        try {
            WebElement phoneField = driver.findElement(phoneInput);
            String currentPhone = phoneField.getAttribute("value");
            if (currentPhone == null || currentPhone.isEmpty()) {
                phoneField.clear();
                phoneField.sendKeys(phone);
                System.out.println("✅ Filled phone: " + phone);
            } else {
                System.out.println("ℹ️ Phone already has value: " + currentPhone);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not fill phone: " + e.getMessage());
        }
        
        // Always fill address if provided
        try {
            WebElement addressField = driver.findElement(addressInput);
            String currentAddress = addressField.getAttribute("value");
            if (currentAddress == null || currentAddress.isEmpty()) {
                addressField.clear();
                addressField.sendKeys(address);
                System.out.println("✅ Filled address: " + address);
            } else {
                System.out.println("ℹ️ Address already has value: " + currentAddress);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not fill address: " + e.getMessage());
        }
        
        // Check and select province/district/ward if not already selected
        try {
            WebElement provinceElement = driver.findElement(provinceSelect);
            String currentProvince = provinceElement.getAttribute("value");
            if (currentProvince == null || currentProvince.isEmpty()) {
                System.out.println("📍 Selecting address dropdowns...");
                selectFirstAvailableProvince();
                selectFirstAvailableDistrict();
                selectFirstAvailableWard();
            } else {
                System.out.println("ℹ️ Province already selected: " + currentProvince);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not check/select address dropdowns: " + e.getMessage());
        }
    }
    
    /**
     * Check if form is pre-populated (user has default address)
     */
    public boolean isFormPrePopulated() {
        try {
            WebElement fullName = driver.findElement(fullNameInput);
            return fullName.getAttribute("value") != null && !fullName.getAttribute("value").isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Select payment method
     */
    public void selectPaymentMethod(String method) {
        try {
            if ("COD".equalsIgnoreCase(method)) {
                click(codRadio);
            } else if ("VnPay".equalsIgnoreCase(method) || "VNPay".equalsIgnoreCase(method)) {
                click(vnpayRadio);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Payment method " + method + " may already be selected");
        }
    }
    
    /**
     * Click place order button
     */
    public void clickPlaceOrder() {
        scrollToElement(placeOrderButton);
        click(placeOrderButton);
    }
    
    /**
     * Click place order using JavaScript (bypasses potential overlay issues)
     */
    public void clickPlaceOrderJS() {
        try {
            WebElement button = driver.findElement(placeOrderButton);
            scrollToElement(placeOrderButton);
            Thread.sleep(500);
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);
            System.out.println("✅ Clicked place order button using JavaScript");
        } catch (Exception e) {
            System.out.println("⚠️ Could not click using JS, trying regular click: " + e.getMessage());
            click(placeOrderButton);
        }
    }
    
    /**
     * Debug: Log form field values
     */
    public void logFormState() {
        try {
            WebElement fullName = driver.findElement(fullNameInput);
            WebElement phone = driver.findElement(phoneInput);
            WebElement address = driver.findElement(addressInput);
            WebElement province = driver.findElement(provinceSelect);
            WebElement district = driver.findElement(districtSelect);
            WebElement ward = driver.findElement(wardSelect);
            
            System.out.println("📋 Form state:");
            System.out.println("   - Full name: '" + fullName.getAttribute("value") + "'");
            System.out.println("   - Phone: '" + phone.getAttribute("value") + "'");
            System.out.println("   - Address: '" + address.getAttribute("value") + "'");
            System.out.println("   - Province: '" + province.getAttribute("value") + "' (disabled=" + !province.isEnabled() + ")");
            System.out.println("   - District: '" + district.getAttribute("value") + "' (disabled=" + !district.isEnabled() + ")");
            System.out.println("   - Ward: '" + ward.getAttribute("value") + "' (disabled=" + !ward.isEnabled() + ")");
        } catch (Exception e) {
            System.out.println("⚠️ Could not log form state: " + e.getMessage());
        }
    }
    
    /**
     * Click place order and handle success alert
     * Returns true if order was successful (alert appeared)
     */
    public boolean clickPlaceOrderAndHandleAlert() {
        // Log form state for debugging
        logFormState();
        
        // Scroll and use JavaScript click for reliability
        scrollToElement(placeOrderButton);
        clickPlaceOrderJS();
        
        // Wait for possible alert
        try {
            Thread.sleep(3000);
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("📢 Alert received: " + alertText);
            alert.accept();
            Thread.sleep(1000); // Wait for redirect
            return alertText.contains("thành công") || alertText.contains("success");
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            System.out.println("⚠️ No alert appeared after clicking place order");
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Error handling alert: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Complete checkout - uses pre-populated data if available, only fills missing fields
     * Returns true if order was successful
     */
    public boolean completeCheckout(String paymentMethod) {
        // Just select payment method and submit (form should be pre-populated)
        selectPaymentMethod(paymentMethod);
        return clickPlaceOrderAndHandleAlert();
    }
    
    /**
     * Complete checkout with custom billing info
     */
    public void completeCheckoutWithDetails(String fullName, String email, String phone,
                                            String address, String paymentMethod) {
        fillBillingDetails(fullName, email, phone, address);
        selectPaymentMethod(paymentMethod);
        clickPlaceOrder();
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
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
     * Check if validation error is displayed
     */
    public boolean hasValidationError() {
        return isDisplayed(validationError);
    }
    
    /**
     * Get validation error text
     */
    public String getValidationError() {
        try {
            return getText(validationError);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if place order button is enabled
     */
    public boolean isPlaceOrderButtonEnabled() {
        return isEnabled(placeOrderButton);
    }
    
    /**
     * Get total amount
     */
    public String getTotalAmount() {
        return getText(totalAmount);
    }
    
    /**
     * Wait for checkout page to load
     */
    public void waitForPageToLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
