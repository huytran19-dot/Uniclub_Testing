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
    
    // Locators - Page identification
    private final By pageTitle = By.xpath("//h1[contains(text(),'Thanh toán')] | //*[contains(text(),'Checkout')]");
    private final By loadingSpinner = By.cssSelector(".animate-spin");
    
    // Locators - Shipping Information Form (match AddressForm.jsx)
    private final By shippingFormSection = By.xpath("//h2[contains(text(),'Thông tin giao hàng')]");
    private final By fullNameInput = By.cssSelector("input[name='full_name'], input[placeholder*='Họ tên']");
    private final By emailInput = By.cssSelector("input[name='email'], input[type='email']");
    private final By phoneInput = By.cssSelector("input[name='phone'], input[placeholder*='Số điện thoại']");
    private final By addressInput = By.cssSelector("input[name='address'], textarea[name='address']");
    private final By provinceSelect = By.cssSelector("select[name='province']");
    private final By districtSelect = By.cssSelector("select[name='district']");
    private final By wardSelect = By.cssSelector("select[name='ward']");
    private final By noteTextarea = By.cssSelector("textarea[name='note'], input[name='note']");
    private final By useDefaultAddressCheckbox = By.cssSelector("input[type='checkbox'][name='useDefaultAddress']");
    private final By changeAddressButton = By.xpath("//button[contains(text(),'Thay đổi địa chỉ')]");
    
    // Locators - Payment Method
    private final By paymentMethodSection = By.xpath("//h2[contains(text(),'Phương thức thanh toán')]");
    private final By codRadio = By.xpath("//input[@type='radio'][@value='COD']");
    private final By vnpayRadio = By.xpath("//input[@type='radio'][@value='VNPay']");
    private final By codPaymentOption = By.xpath("//input[@type='radio'][@value='COD'] | //label[contains(text(),'COD')]");
    private final By vnpayPaymentOption = By.xpath("//input[@type='radio'][@value='VNPay'] | //label[contains(text(),'VNPay')]");
    
    // Locators - Order Summary (right panel)
    private final By orderSummary = By.xpath("//h3[contains(text(),'Đơn hàng của bạn')] | //h3[contains(text(),'Tóm tắt đơn hàng')]");
    private final By orderSummarySection = By.xpath("//h3[contains(text(),'Tóm tắt đơn hàng')] | //*[contains(text(),'Order Summary')]");
    private final By cartItemsList = By.cssSelector(".cart-item, [class*='checkout-item']");
    private final By subtotalValue = By.xpath("//span[contains(text(),'Tạm tính')]/following-sibling::*");
    private final By shippingValue = By.xpath("//span[contains(text(),'Phí vận chuyển')]/following-sibling::*");
    private final By totalAmount = By.xpath("//*[contains(text(),'Tổng cộng')]//following-sibling::*");
    private final By totalValue = By.xpath("//span[contains(text(),'Tổng cộng')]/following-sibling::span");
    
    // Locators - Action Buttons
    private final By placeOrderButton = By.xpath("//button[@type='submit'] | //button[contains(text(),'Đặt hàng')] | //button[contains(text(),'Place Order')]");
    private final By backToCartButton = By.xpath("//a[contains(@href,'/cart')] | //a[contains(text(),'Giỏ hàng')]");
    private final By backToCartLink = By.xpath("//a[@href='/cart'] | //a[contains(text(),'Giỏ hàng')]");
    
    // Locators - Messages
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'Giỏ hàng trống')]");
    private final By errorMessage = By.xpath("//div[contains(@class, 'bg-red')] | .error-message, [class*='error']");
    private final By successMessage = By.cssSelector(".success-message, [class*='success']");
    private final By validationError = By.xpath("//input:invalid");
    
    /**
     * Constructor
     */
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to checkout page
     */
    public void navigateToCheckout(String baseUrl) {
        driver.get(baseUrl + "/checkout");
        waitForPageLoad();
    }
    
    /**
     * Check if on checkout page
     */
    public boolean isOnCheckoutPage() {
        try {
            waitForPageLoad();
            String currentUrl = getCurrentUrl();
            return currentUrl.contains("/checkout") || isDisplayed(pageTitle);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for page to load (no loading spinner)
     */
    public void waitForCheckoutPageLoad() {
        try {
            Thread.sleep(2000); // Wait for data loading
        } catch (Exception e) {
            // Ignore
        }
    }
    
    /**
     * Wait for checkout page to load (legacy method)
     */
    public void waitForPageToLoad() {
        waitForCheckoutPageLoad();
    }
    
    /**
     * Check if showing empty cart message
     */
    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(emptyCartMessage);
    }
    
    /**
     * Check if cart is empty (redirected or shown message)
     */
    public boolean isCartEmpty() {
        try {
            return isDisplayed(emptyCartMessage);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if shipping form is displayed
     */
    public boolean isShippingFormDisplayed() {
        try {
            return isDisplayed(shippingFormSection);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Fill billing details - basic text fields only
     * Note: province/district/ward may be pre-populated from user profile
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
     * Fill shipping information
     */
    public void fillShippingInfo(String fullName, String phone, String email, String address) {
        try {
            if (fullName != null && !fullName.isEmpty()) {
                type(fullNameInput, fullName);
            }
            if (phone != null && !phone.isEmpty()) {
                type(phoneInput, phone);
            }
            if (email != null && !email.isEmpty()) {
                type(emailInput, email);
            }
            if (address != null && !address.isEmpty()) {
                type(addressInput, address);
            }
            System.out.println("✓ Filled shipping information");
        } catch (Exception e) {
            System.out.println("✗ Failed to fill shipping information: " + e.getMessage());
        }
    }
    
    /**
     * Select province from dropdown (using Select)
     */
    public void selectProvince(String provinceName) {
        try {
            WebElement selectElement = driver.findElement(provinceSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(provinceName);
            Thread.sleep(1000); // Wait for districts to load
        } catch (Exception e) {
            // Try alternative method using sendKeys
            try {
                driver.findElement(provinceSelect).sendKeys(provinceName);
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("⚠️ Could not select province: " + e.getMessage());
            }
        }
    }
    
    /**
     * Select district from dropdown (using Select)
     */
    public void selectDistrict(String districtName) {
        try {
            WebElement selectElement = driver.findElement(districtSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(districtName);
            Thread.sleep(1000); // Wait for wards to load
        } catch (Exception e) {
            // Try alternative method using sendKeys
            try {
                driver.findElement(districtSelect).sendKeys(districtName);
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("⚠️ Could not select district: " + e.getMessage());
            }
        }
    }
    
    /**
     * Select ward from dropdown (using Select)
     */
    public void selectWard(String wardName) {
        try {
            WebElement selectElement = driver.findElement(wardSelect);
            Select select = new Select(selectElement);
            select.selectByVisibleText(wardName);
            Thread.sleep(500);
        } catch (Exception e) {
            // Try alternative method using sendKeys
            try {
                driver.findElement(wardSelect).sendKeys(wardName);
                Thread.sleep(500);
            } catch (Exception ex) {
                System.out.println("⚠️ Could not select ward: " + e.getMessage());
            }
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
     * Fill complete address (province, district, ward, address)
     */
    public void fillCompleteAddress(String province, String district, String ward, String address) {
        try {
            selectProvince(province);
            selectDistrict(district);
            selectWard(ward);
            type(addressInput, address);
            System.out.println("✓ Filled complete address");
        } catch (Exception e) {
            System.out.println("✗ Failed to fill complete address: " + e.getMessage());
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
     * Add order note
     */
    public void addNote(String note) {
        try {
            type(noteTextarea, note);
        } catch (Exception e) {
            System.out.println("Failed to add note: " + e.getMessage());
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
     * Select payment method (COD or VNPay)
     */
    public void selectPaymentMethod(String method) {
        try {
            if ("COD".equalsIgnoreCase(method)) {
                click(codRadio);
                System.out.println("✓ Selected payment method: COD");
            } else if ("VnPay".equalsIgnoreCase(method) || "VNPay".equalsIgnoreCase(method)) {
                click(vnpayRadio);
                System.out.println("✓ Selected payment method: VNPay");
            }
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ Payment method " + method + " may already be selected or failed: " + e.getMessage());
        }
    }
    
    /**
     * Get current selected payment method
     */
    public String getSelectedPaymentMethod() {
        try {
            if (driver.findElement(codRadio).isSelected()) {
                return "COD";
            } else if (driver.findElement(vnpayRadio).isSelected()) {
                return "VNPay";
            }
        } catch (Exception e) {
            System.out.println("Failed to get selected payment method");
        }
        return "";
    }
    
    /**
     * Check if payment method section is displayed
     */
    public boolean isPaymentMethodSectionDisplayed() {
        try {
            return isDisplayed(paymentMethodSection);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if order summary is displayed
     */
    public boolean isOrderSummaryDisplayed() {
        try {
            return isDisplayed(orderSummarySection) || isDisplayed(orderSummary);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get subtotal from order summary
     */
    public String getSubtotal() {
        try {
            waitForCheckoutPageLoad();
            return getText(subtotalValue);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get shipping fee from order summary
     */
    public String getShippingFee() {
        try {
            waitForCheckoutPageLoad();
            return getText(shippingValue);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get total amount from order summary
     */
    public String getTotalAmount() {
        try {
            waitForCheckoutPageLoad();
            return getText(totalValue);
        } catch (Exception e) {
            // Try alternative locator
            try {
                return getText(totalAmount);
            } catch (Exception ex) {
                return "";
            }
        }
    }
    
    /**
     * Get order total (alias for getTotalAmount)
     */
    public String getOrderTotal() {
        return getTotalAmount();
    }
    
    /**
     * Check if place order button is displayed
     */
    public boolean isPlaceOrderButtonDisplayed() {
        try {
            return isDisplayed(placeOrderButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if place order button is enabled
     */
    public boolean isPlaceOrderButtonEnabled() {
        try {
            return driver.findElement(placeOrderButton).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Click place order button (regular click)
     */
    public void clickPlaceOrder() {
        try {
            scrollToElement(placeOrderButton);
            click(placeOrderButton);
            Thread.sleep(2000); // Wait for order processing
            System.out.println("✓ Clicked Place Order button");
        } catch (Exception e) {
            System.out.println("✗ Failed to click Place Order button: " + e.getMessage());
        }
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
     * Click back to cart link
     */
    public CartPage clickBackToCart() {
        try {
            click(backToCartLink);
            waitForPageLoad();
            return new CartPage(driver);
        } catch (Exception e) {
            System.out.println("Failed to click back to cart link");
            return null;
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
     * Complete checkout flow with full information
     * @param fullName Customer full name
     * @param phone Phone number
     * @param email Email address
     * @param province Province/City
     * @param district District
     * @param ward Ward
     * @param address Detailed address
     * @param paymentMethod Payment method (COD or VNPay)
     * @param note Optional order note
     */
    public void completeCheckout(String fullName, String phone, String email, 
                                 String province, String district, String ward, 
                                 String address, String paymentMethod, String note) {
        try {
            // Step 1: Fill shipping info
            fillShippingInfo(fullName, phone, email, null);
            
            // Step 2: Fill address
            fillCompleteAddress(province, district, ward, address);
            
            // Step 3: Add note if provided
            if (note != null && !note.isEmpty()) {
                addNote(note);
            }
            
            // Step 4: Select payment method
            selectPaymentMethod(paymentMethod);
            
            // Step 5: Click place order
            clickPlaceOrder();
            
            System.out.println("✓ Completed checkout process");
        } catch (Exception e) {
            System.out.println("✗ Failed to complete checkout: " + e.getMessage());
        }
    }
    
    /**
     * Get error message
     */
    public String getErrorMessage() {
        try {
            return getText(errorMessage);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get success message
     */
    public String getSuccessMessage() {
        try {
            return getText(successMessage);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return isDisplayed(errorMessage);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            return isDisplayed(successMessage);
        } catch (Exception e) {
            return false;
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
     * Verify checkout form has required fields
     */
    public boolean verifyRequiredFieldsPresent() {
        try {
            return isDisplayed(fullNameInput) 
                && isDisplayed(phoneInput) 
                && isDisplayed(emailInput)
                && isDisplayed(addressInput)
                && isDisplayed(placeOrderButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extract numeric value from price string
     */
    public int extractPrice(String priceText) {
        try {
            String cleaned = priceText.replace("₫", "")
                                     .replace(".", "")
                                     .replace(",", "")
                                     .trim();
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            System.out.println("Failed to parse price: " + priceText);
            return 0;
        }
    }
}