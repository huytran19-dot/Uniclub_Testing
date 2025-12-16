package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * CheckoutPage - Page Object for Checkout page (User side)
 */
public class CheckoutPage extends BasePage {
    
    // Locators
    private final By pageTitle = By.xpath("//h1[contains(text(),'Thanh toán')] | //*[contains(text(),'Checkout')]");
    private final By loadingSpinner = By.cssSelector(".animate-spin");
    
    // Shipping Information Form
    private final By shippingFormSection = By.xpath("//h2[contains(text(),'Thông tin giao hàng')]");
    private final By fullNameInput = By.cssSelector("input[name='full_name'], input[placeholder*='Họ tên']");
    private final By phoneInput = By.cssSelector("input[name='phone'], input[placeholder*='Số điện thoại']");
    private final By emailInput = By.cssSelector("input[name='email'], input[type='email']");
    private final By addressInput = By.cssSelector("input[name='address'], textarea[name='address']");
    private final By provinceSelect = By.cssSelector("select[name='province']");
    private final By districtSelect = By.cssSelector("select[name='district']");
    private final By wardSelect = By.cssSelector("select[name='ward']");
    private final By noteTextarea = By.cssSelector("textarea[name='note'], input[name='note']");
    private final By useDefaultAddressCheckbox = By.cssSelector("input[type='checkbox'][name='useDefaultAddress']");
    private final By changeAddressButton = By.xpath("//button[contains(text(),'Thay đổi địa chỉ')]");
    
    // Payment Method
    private final By paymentMethodSection = By.xpath("//h2[contains(text(),'Phương thức thanh toán')]");
    private final By codPaymentOption = By.xpath("//input[@type='radio'][@value='COD'] | //label[contains(text(),'COD')]");
    private final By vnpayPaymentOption = By.xpath("//input[@type='radio'][@value='VNPay'] | //label[contains(text(),'VNPay')]");
    
    // Order Summary (right panel)
    private final By orderSummarySection = By.xpath("//h3[contains(text(),'Tóm tắt đơn hàng')] | //*[contains(text(),'Order Summary')]");
    private final By cartItemsList = By.cssSelector(".cart-item, [class*='checkout-item']");
    private final By subtotalValue = By.xpath("//span[contains(text(),'Tạm tính')]/following-sibling::*");
    private final By shippingValue = By.xpath("//span[contains(text(),'Phí vận chuyển')]/following-sibling::*");
    private final By totalValue = By.xpath("//span[contains(text(),'Tổng cộng')]/following-sibling::span");
    
    // Action buttons
    private final By placeOrderButton = By.xpath("//button[contains(text(),'Đặt hàng')] | //button[contains(text(),'Place Order')]");
    private final By backToCartLink = By.xpath("//a[@href='/cart'] | //a[contains(text(),'Giỏ hàng')]");
    
    // Messages
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'Giỏ hàng trống')]");
    private final By errorMessage = By.cssSelector(".error-message, [class*='error']");
    private final By successMessage = By.cssSelector(".success-message, [class*='success']");
    
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
            return currentUrl.contains("/checkout");
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
     * Select province
     */
    public void selectProvince(String province) {
        try {
            driver.findElement(provinceSelect).sendKeys(province);
            Thread.sleep(1000); // Wait for district to load
        } catch (Exception e) {
            System.out.println("Failed to select province: " + e.getMessage());
        }
    }
    
    /**
     * Select district
     */
    public void selectDistrict(String district) {
        try {
            driver.findElement(districtSelect).sendKeys(district);
            Thread.sleep(1000); // Wait for ward to load
        } catch (Exception e) {
            System.out.println("Failed to select district: " + e.getMessage());
        }
    }
    
    /**
     * Select ward
     */
    public void selectWard(String ward) {
        try {
            driver.findElement(wardSelect).sendKeys(ward);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Failed to select ward: " + e.getMessage());
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
     * Select payment method
     */
    public void selectPaymentMethod(String method) {
        try {
            if (method.equalsIgnoreCase("COD")) {
                click(codPaymentOption);
            } else if (method.equalsIgnoreCase("VNPay")) {
                click(vnpayPaymentOption);
            }
            Thread.sleep(500);
            System.out.println("✓ Selected payment method: " + method);
        } catch (Exception e) {
            System.out.println("✗ Failed to select payment method: " + e.getMessage());
        }
    }
    
    /**
     * Get current selected payment method
     */
    public String getSelectedPaymentMethod() {
        try {
            if (driver.findElement(codPaymentOption).isSelected()) {
                return "COD";
            } else if (driver.findElement(vnpayPaymentOption).isSelected()) {
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
            return isDisplayed(orderSummarySection);
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
            return "";
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
     * Click place order button
     */
    public void clickPlaceOrder() {
        try {
            click(placeOrderButton);
            Thread.sleep(2000); // Wait for order processing
            System.out.println("✓ Clicked Place Order button");
        } catch (Exception e) {
            System.out.println("✗ Failed to click Place Order button: " + e.getMessage());
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
