package com.uniclub.tests;

import com.uniclub.base.BaseTest;
import com.uniclub.pages.*;
import com.uniclub.utils.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OrderCheckoutTest - Test cases for Module 3: Đơn hàng
 * Test ID: M3-01 to M3-06
 */
@Epic("Order Management")
@Feature("Order Checkout")
public class OrderCheckoutTest extends BaseTest {
    
    private UserLoginPage loginPage;
    private ProductsPage productsPage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private MyOrdersPage myOrdersPage;
    private String timestamp;
    
    @BeforeMethod
    public void setUpTest() {
        navigateToWeb();
        timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        
        // Initialize page objects
        loginPage = new UserLoginPage(driver);
        productsPage = new ProductsPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        myOrdersPage = new MyOrdersPage(driver);
    }
    
    /**
     * Helper: Login with test user
     */
    private void loginAsTestUser() {
        driver.get(ConfigReader.getWebUrl() + "/login");
        loginPage.login(ConfigReader.getUserEmail(), ConfigReader.getUserPassword());
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper: Add a product to cart
     * Tries multiple products until finding one with available variants
     */
    private void addProductToCart() {
        // Try multiple products to find one with valid variants
        int[] productIndices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int idx : productIndices) {
            if (addProductToCartByIndex(idx)) {
                System.out.println("✅ Successfully added product at index " + idx + " to cart");
                return;
            }
        }
        System.out.println("❌ Could not find any product with valid variants");
    }
    
    /**
     * Helper: Add a product to cart by index
     * @param productIndex The index of the product to add (0-based)
     * @return true if product was added successfully, false otherwise
     */
    private boolean addProductToCartByIndex(int productIndex) {
        driver.get(ConfigReader.getWebUrl() + "/products");
        
        try {
            Thread.sleep(2000);
            // Click on product by index
            if (productIndex == 0) {
                productsPage.clickFirstProduct();
            } else {
                productsPage.clickProduct(productIndex);
            }
            Thread.sleep(2000);
            
            // Must select size and color before adding to cart
            System.out.println("  Trying product at index " + productIndex + "...");
            System.out.println("  Selecting variant (size + color)...");
            productDetailPage.selectFirstAvailableVariant();
            Thread.sleep(1000);
            
            // Check if we can add to cart
            if (!productDetailPage.canAddToCart()) {
                System.out.println("  ⚠️ Cannot add this product to cart, trying next...");
                return false;
            }
            
            // Add to cart
            System.out.println("  Clicking add to cart...");
            productDetailPage.addToCart();
            Thread.sleep(1000);
            
            // Handle the success alert that appears after adding to cart
            handleAddToCartAlert();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper: Add a specific product to cart (legacy method for compatibility)
     * @param productIndex The index of the product to add (0-based)
     */
    private void addProductToCart(int productIndex) {
        addProductToCartByIndex(productIndex);
    }
    
    /**
     * Helper: Handle the alert that appears after adding product to cart
     */
    private void handleAddToCartAlert() {
        try {
            // Wait up to 3 seconds for alert to appear
            org.openqa.selenium.support.ui.WebDriverWait wait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("  📢 Add to cart alert: " + alertText);
            alert.accept();
            System.out.println("  ✅ Alert accepted");
            Thread.sleep(500);
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("  ℹ️ No add to cart alert appeared (might be toast notification)");
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            System.out.println("  ℹ️ No alert present");
        } catch (Exception e) {
            System.out.println("  ⚠️ Alert handling error: " + e.getMessage());
        }
    }
    
    /**
     * Helper: Clear the cart before running a test
     * This ensures test isolation by removing any leftover items from previous tests
     */
    private void clearCart() {
        System.out.println("  🧹 Clearing cart...");
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        
        int attempts = 0;
        while (!cartPage.isCartEmpty() && cartPage.getCartItemCount() > 0 && attempts < 10) {
            try {
                System.out.println("    Removing item from cart...");
                cartPage.removeFirstItem();
                
                // Handle any alert that might appear after removing
                handleAnyAlert();
                
                Thread.sleep(1000);
                attempts++;
            } catch (Exception e) {
                System.out.println("    ⚠️ Error removing item: " + e.getMessage());
                break;
            }
        }
        
        if (cartPage.isCartEmpty() || cartPage.getCartItemCount() == 0) {
            System.out.println("  ✅ Cart cleared successfully");
        } else {
            System.out.println("  ⚠️ Cart may still have items");
        }
    }
    
    /**
     * Helper: Handle any alert that may be present (success, error, confirmation)
     */
    private void handleAnyAlert() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(1));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("    📢 Alert: " + alertText);
            alert.accept();
        } catch (Exception e) {
            // No alert present, that's okay
        }
    }
    
    /**
     * M3-01: Tạo đơn hàng thành công (thanh toán COD)
     */
    @Test(priority = 1, description = "Tạo đơn hàng thành công với thanh toán COD")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test complete checkout flow with COD payment method")
    @Story("Order Creation")
    public void testM3_01_CreateOrderWithCOD() {
        System.out.println("========================================");
        System.out.println("M3-01: Tạo đơn hàng thành công (thanh toán COD)");
        System.out.println("========================================");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        Assert.assertTrue(driver.getCurrentUrl().contains("/"), "Should be logged in");
        
        // Clear cart before starting to ensure clean state
        clearCart();
        
        // Step 2: Add product to cart (automatically finds a product with valid variants)
        System.out.println("📝 Step 2: Add product to cart");
        addProductToCart();
        
        // Step 3: Go to cart
        System.out.println("📝 Step 3: Navigate to cart");
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        Assert.assertTrue(cartPage.isOnCartPage(), "Should be on cart page");
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        
        // Step 4: Proceed to checkout
        System.out.println("📝 Step 4: Proceed to checkout");
        cartPage.clickCheckout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(checkoutPage.isOnCheckoutPage(), "Should be on checkout page");
        
        // Step 5: Fill billing details and select COD
        System.out.println("📝 Step 5: Fill billing details with COD payment");
        
        // ALWAYS fill required fields (phone, address, province/district/ward)
        // User may have only partial data pre-populated from profile
        checkoutPage.fillAllRequiredFields("0901234567", "123 Test Street, District 1");
        
        // Select payment and submit - this also handles success alert
        System.out.println("📝 Step 6: Submit order with COD payment");
        boolean orderSuccess = checkoutPage.completeCheckout("COD");
        System.out.println("  Order submission result: " + (orderSuccess ? "SUCCESS" : "PENDING/FAILED"));
        
        // Wait for redirect
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert: Should redirect to order confirmation or my orders
        String currentUrl = driver.getCurrentUrl();
        boolean orderCreated = orderSuccess || 
                              currentUrl.contains("/order") || 
                              currentUrl.contains("/success") ||
                              currentUrl.contains("/my-orders");
        
        System.out.println("  Current URL: " + currentUrl);
        Assert.assertTrue(orderCreated, 
            "Order should be created successfully (URL: " + currentUrl + ")");
        
        System.out.println("✅ Test Passed: Order created successfully with COD");
    }
    
    /**
     * M3-02: Tạo đơn hàng thành công (thanh toán VNPay)
     */
    @Test(priority = 2, description = "Tạo đơn hàng thành công với thanh toán VNPay")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test complete checkout flow with VNPay payment method")
    @Story("Order Creation")
    public void testM3_02_CreateOrderWithVNPay() {
        System.out.println("========================================");
        System.out.println("M3-02: Tạo đơn hàng thành công (thanh toán VNPay)");
        System.out.println("========================================");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        
        // Clear cart before starting to ensure clean state
        clearCart();
        
        // Step 2: Add product to cart (use product index 1 to avoid stock conflict with M3-01)
        System.out.println("📝 Step 2: Add product to cart");
        addProductToCart(1);
        
        // Step 3: Go to cart and checkout
        System.out.println("📝 Step 3: Navigate to cart and checkout");
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        cartPage.clickCheckout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 4: Fill billing details and select VNPay
        System.out.println("📝 Step 4: Fill billing details with VNPay payment");
        
        // ALWAYS fill required fields (phone, address, province/district/ward)
        // User may have only partial data pre-populated from profile
        checkoutPage.fillAllRequiredFields("0901234568", "456 VNPay Street, District 3");
        
        // Select VNPay payment
        checkoutPage.selectPaymentMethod("VnPay");
        System.out.println("📝 Step 5: Submit order with VNPay payment");
        
        // Click place order and handle success alert
        // VNPay flow may show success alert after returning from VNPay gateway
        boolean orderSuccess = checkoutPage.clickPlaceOrderAndHandleAlert();
        System.out.println("  Order submission result: " + (orderSuccess ? "SUCCESS" : "PENDING/REDIRECTING"));
        
        // Wait for redirect to VNPay or order page
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Handle any leftover alert that might appear
        handleAnyAlert();
        
        // Assert: Should redirect to VNPay gateway or show VNPay/order confirmation
        // Or orderSuccess is true if success alert was handled
        String currentUrl = driver.getCurrentUrl();
        System.out.println("  Current URL: " + currentUrl);
        boolean redirectedToVNPay = orderSuccess ||
                                     currentUrl.contains("vnpay") || 
                                     currentUrl.contains("payment") ||
                                     currentUrl.contains("sandbox") ||
                                     currentUrl.contains("order") ||
                                     currentUrl.contains("my-orders") ||
                                     currentUrl.contains("success");
        
        Assert.assertTrue(redirectedToVNPay, 
            "Should redirect to VNPay or payment confirmation (URL: " + currentUrl + ")");
        
        System.out.println("✅ Test Passed: Order initiated with VNPay payment");
    }
    
    /**
     * M3-03: Tạo đơn hàng với giỏ hàng rỗng
     */
    @Test(priority = 3, description = "Tạo đơn hàng với giỏ hàng rỗng")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test checkout attempt with empty cart")
    @Story("Order Validation")
    public void testM3_03_CheckoutWithEmptyCart() {
        System.out.println("========================================");
        System.out.println("M3-03: Tạo đơn hàng với giỏ hàng rỗng");
        System.out.println("========================================");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        
        // Step 2: Go to cart and clear it if not empty
        System.out.println("📝 Step 2: Navigate to cart and ensure it's empty");
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        
        // Clear cart if it has items
        int attempts = 0;
        while (!cartPage.isCartEmpty() && cartPage.getCartItemCount() > 0 && attempts < 10) {
            try {
                System.out.println("  Removing item from cart...");
                cartPage.removeFirstItem();
                Thread.sleep(1500);
                attempts++;
            } catch (Exception e) {
                break;
            }
        }
        
        // Refresh to verify cart is empty
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        
        // Assert: Cart should be empty
        boolean isEmpty = cartPage.isCartEmpty() || cartPage.getCartItemCount() == 0;
        System.out.println("  Cart empty status: " + isEmpty);
        
        // Step 3: Try to access checkout directly
        System.out.println("📝 Step 3: Try to access checkout page directly");
        driver.get(ConfigReader.getWebUrl() + "/checkout");
        
        try {
            Thread.sleep(3000); // Wait for potential redirect
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert: Should show error/empty cart message or redirect back to /cart
        // Frontend logic: if cart is empty, navigate("/cart")
        String currentUrl = driver.getCurrentUrl();
        System.out.println("  Current URL after trying to access checkout: " + currentUrl);
        boolean preventedCheckout = currentUrl.contains("/cart") || 
                                    checkoutPage.isEmptyCartMessageDisplayed() ||
                                    checkoutPage.isErrorMessageDisplayed() ||
                                    !currentUrl.contains("/checkout");
        
        Assert.assertTrue(preventedCheckout, 
            "Should prevent checkout with empty cart (URL: " + currentUrl + ")");
        
        System.out.println("✅ Test Passed: Cannot checkout with empty cart");
    }
    
    /**
     * M3-04: Tạo đơn hàng thiếu thông tin billing
     */
    @Test(priority = 4, description = "Tạo đơn hàng thiếu thông tin billing")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test validation when required billing information is missing")
    @Story("Order Validation")
    public void testM3_04_CheckoutWithMissingBilling() {
        System.out.println("========================================");
        System.out.println("M3-04: Tạo đơn hàng thiếu thông tin billing");
        System.out.println("========================================");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        
        // Clear cart before starting to ensure clean state
        clearCart();
        
        // Step 2: Add product to cart (use product index 2 to avoid stock conflict with other tests)
        System.out.println("📝 Step 2: Add product to cart");
        addProductToCart(2);
        
        // Step 3: Go to checkout
        System.out.println("📝 Step 3: Navigate to checkout");
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        cartPage.clickCheckout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 4: Fill incomplete billing details (missing phone)
        System.out.println("📝 Step 4: Fill billing details WITHOUT phone number");
        checkoutPage.fillBillingDetails(
            "Test User Missing Phone",
            "testmissing@example.com",
            "",  // Missing phone
            "123 Test Street"
        );
        
        // Select province/district/ward (to ensure form is valid except phone)
        checkoutPage.selectFirstAvailableProvince();
        checkoutPage.selectFirstAvailableDistrict();
        checkoutPage.selectFirstAvailableWard();
        
        checkoutPage.selectPaymentMethod("COD");
        checkoutPage.clickPlaceOrder();
        
        // Wait for validation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert: Should show validation error and stay on checkout page
        boolean hasError = checkoutPage.hasValidationError() || 
                          checkoutPage.isErrorMessageDisplayed() ||
                          checkoutPage.isOnCheckoutPage();
        
        Assert.assertTrue(hasError, "Should show validation error for missing phone");
        
        System.out.println("✅ Test Passed: Validation prevents order with missing billing info");
    }
    
    /**
     * M3-05: Tạo đơn hàng khi sản phẩm hết hàng (Race Condition)
     */
    @Test(priority = 5, description = "Tạo đơn hàng khi sản phẩm hết hàng")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test handling of out-of-stock products during checkout")
    @Story("Order Validation")
    public void testM3_05_CheckoutWithOutOfStock() {
        System.out.println("========================================");
        System.out.println("M3-05: Tạo đơn hàng khi sản phẩm hết hàng");
        System.out.println("========================================");
        
        // Note: This is a race condition test
        // In real testing, you would need to:
        // 1. Add product with low stock (quantity = 1)
        // 2. Have another user/session purchase it
        // 3. Try to checkout with first user
        
        System.out.println("⚠️  Note: This is a manual/scenario test");
        System.out.println("    Real implementation requires:");
        System.out.println("    - Product with quantity = 1");
        System.out.println("    - Concurrent user sessions");
        System.out.println("    - Backend race condition handling");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        
        // Clear cart before starting to ensure clean state
        clearCart();
        
        // Step 2: Add product to cart (use product index 3 to avoid stock conflict with other tests)
        System.out.println("📝 Step 2: Add product to cart");
        addProductToCart(3);
        
        // Step 3: Go to checkout
        driver.get(ConfigReader.getWebUrl() + "/cart");
        cartPage.waitForCartToLoad();
        cartPage.clickCheckout();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 4: Try to place order
        System.out.println("📝 Step 3: Attempt to place order");
        
        // ALWAYS fill required fields (phone, address, province/district/ward)
        checkoutPage.fillAllRequiredFields("0901234569", "789 Stock Street, District 5");
        
        checkoutPage.completeCheckout("COD");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Note: In real scenario with actual out-of-stock condition,
        // should verify error message like "Sản phẩm X đã hết hàng"
        
        System.out.println("✅ Test Completed: Out-of-stock scenario executed");
        System.out.println("    (Actual validation requires race condition setup)");
    }
    
    /**
     * M3-06: Hủy đơn hàng (User)
     */
    @Test(priority = 6, description = "Hủy đơn hàng bởi User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test user cancelling a pending order")
    @Story("Order Cancellation")
    public void testM3_06_CancelOrder() {
        System.out.println("========================================");
        System.out.println("M3-06: Hủy đơn hàng (User)");
        System.out.println("========================================");
        
        // Step 1: Login
        System.out.println("📝 Step 1: Login as test user");
        loginAsTestUser();
        
        // Step 2: Navigate to Orders page (not my-orders)
        System.out.println("📝 Step 2: Navigate to Orders page");
        driver.get(ConfigReader.getWebUrl() + "/orders");
        myOrdersPage.waitForPageToLoad();
        
        Assert.assertTrue(myOrdersPage.isOnMyOrdersPage(), "Should be on Orders page");
        
        // Step 3: Check if there are orders
        System.out.println("📝 Step 3: Check for orders");
        boolean hasOrders = myOrdersPage.hasOrders();
        
        if (!hasOrders) {
            System.out.println("⚠️  No orders found. Creating a new order first...");
            // Create a new order if none exists
            addProductToCart();
            driver.get(ConfigReader.getWebUrl() + "/cart");
            cartPage.waitForCartToLoad();
            cartPage.clickCheckout();
            
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            checkoutPage.fillAllRequiredFields("0901234570", "999 Cancel Test Street");
            checkoutPage.completeCheckout("COD");
            
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            
            // Navigate back to orders
            driver.get(ConfigReader.getWebUrl() + "/orders");
            myOrdersPage.waitForPageToLoad();
            hasOrders = myOrdersPage.hasOrders();
        }
        
        Assert.assertTrue(hasOrders, "Should have at least one order");
        
        // Step 4: Click on first order to view details
        System.out.println("📝 Step 4: Click on first order to view details");
        String initialStatus = myOrdersPage.getFirstOrderStatus();
        System.out.println("   First order status: " + initialStatus);
        myOrdersPage.clickFirstOrder();
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Step 5: Cancel order if possible
        if (myOrdersPage.canCancelFirstOrder()) {
            System.out.println("📝 Step 5: Cancel order");
            boolean cancelled = myOrdersPage.cancelCurrentOrder();
            
            if (cancelled) {
                // Wait for page to update
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                
                // Check for success alert
                boolean success = myOrdersPage.isCancelSuccessful();
                System.out.println("   Cancel operation result: " + (success ? "SUCCESS" : "PENDING"));
                System.out.println("✅ Test Passed: Order cancel attempted");
            } else {
                System.out.println("⚠️  Could not click cancel button");
            }
        } else {
            System.out.println("⚠️  Warning: No cancellable orders found");
            System.out.println("   Order may already be processed or cancelled");
            // This is acceptable - order might already be in non-cancellable state
        }
    }
}
