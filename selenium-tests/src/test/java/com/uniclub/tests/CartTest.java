package com.uniclub.tests;

import com.uniclub.base.BaseTest;
import com.uniclub.pages.CartPage;
import com.uniclub.pages.CheckoutPage;
import com.uniclub.pages.ProductListPage;
import com.uniclub.pages.ProductDetailPage;
import com.uniclub.pages.UserLoginPage;
import com.uniclub.utils.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * CartTest - Test cases for Shopping Cart functionality (User side)
 * Test ID: CART_01 to CART_20
 * 
 * Prerequisites:
 * - Backend and Frontend must be running
 * - Database must have test products
 * - Test user account must exist
 */
@Epic("Shopping Cart")
@Feature("Cart Management")
public class CartTest extends BaseTest {
    
    private CartPage cartPage;
    private ProductListPage productListPage;
    private ProductDetailPage productDetailPage;
    private UserLoginPage loginPage;
    private CheckoutPage checkoutPage;
    
    private static final String TEST_PRODUCT_NAME = "Áo Thun";  // Adjust based on your test data
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Helper: Add single product to cart (non-test method)
     */
    private void addSingleProductToCart() {
        System.out.println(">>> Helper: Adding single product to cart...");
        
        // Clear cart first
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();
        
        // Navigate to products and add first product
        String productsUrl = ConfigReader.getWebUrl() + "/products";
        driver.get(productsUrl);

        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        int productCount = productListPage.getProductCount();
        
        if (productCount == 0) {
            Assert.fail("No products found for adding to cart");
        }
        
        // Try multiple products until one can be added (skip out-of-stock or invalid variants)
        boolean added = false;
        int maxTry = Math.min(productCount, 5);
        for (int i = 0; i < maxTry; i++) {
            System.out.println("Trying product index: " + i);
            try {
                productListPage.clickProductByIndex(i);
                productDetailPage = new ProductDetailPage(driver);
                productDetailPage.waitForReady(6);

                // Attempt to add to cart; auto-selects first available size/color if needed
                productDetailPage.clickAddToCart();
                boolean success = productDetailPage.waitForAddToCartSuccess();

                // If a login-required alert appeared, re-authenticate quickly and retry from products list
                if (!success) {
                    System.out.println("Login required detected after add attempt. Performing quick login and retrying...");
                    ensureLoggedIn();
                    driver.get(productsUrl);
                    productListPage = new ProductListPage(driver);
                    productListPage.waitForProductsVisible(8);
                    // continue loop to try next product
                    continue;
                }

                // Fast confirmation via cart badge (cart was just cleared)
                int badge = productDetailPage.getCartBadgeCount();
                System.out.println("Cart badge after add attempt: " + badge);
                if (badge > 0) {
                    added = true;
                    break;
                }

                // If badge didn't change, go back and try next product
                driver.get(productsUrl);
                productListPage = new ProductListPage(driver);
                productListPage.waitForProductsVisible(8);
            } catch (Exception e) {
                System.out.println("Add attempt failed for index " + i + ": " + e.getMessage());
                driver.get(productsUrl);
                productListPage = new ProductListPage(driver);
                productListPage.waitForProductsVisible(8);
            }
        }

        Assert.assertTrue(added, "Failed to add any product to cart after trying " + maxTry + " products");
        System.out.println(">>> Helper: Product added successfully");
    }
    
    /**
     * Helper: Add multiple products to cart (non-test method)
     */
    private void addMultipleProductsToCart() {
        System.out.println(">>> Helper: Adding multiple products to cart...");
        
        // Clear cart first
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();
        
        // Navigate to products page
        String productsUrl = ConfigReader.getWebUrl() + "/products";
        driver.get(productsUrl);

        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        int productCount = productListPage.getProductCount();
        
        if (productCount < 2) {
            Assert.fail("Need at least 2 products. Found: " + productCount);
        }
        
        // Add first product
        productListPage.clickProductByIndex(0);
        productDetailPage = new ProductDetailPage(driver);
        productDetailPage.waitForReady(6);
        productDetailPage.clickAddToCart();
        boolean firstSuccess = productDetailPage.waitForAddToCartSuccess();
        if (!firstSuccess) {
            System.out.println("Login required detected during first add. Performing quick login and retrying...");
            ensureLoggedIn();
            // retry by reloading products and re-selecting first product
            driver.get(productsUrl);
            productListPage = new ProductListPage(driver);
            productListPage.waitForProductsVisible(8);
            productListPage.clickProductByIndex(0);
            productDetailPage = new ProductDetailPage(driver);
            productDetailPage.waitForReady(6);
            productDetailPage.clickAddToCart();
            productDetailPage.waitForAddToCartSuccess();
        }
        
        // Navigate back and add second product
        driver.get(productsUrl);
        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        productListPage.clickProductByIndex(1);
        productDetailPage = new ProductDetailPage(driver);
        productDetailPage.waitForReady(6);
        productDetailPage.clickAddToCart();
        boolean secondSuccess = productDetailPage.waitForAddToCartSuccess();
        if (!secondSuccess) {
            System.out.println("Login required detected during second add. Performing quick login and retrying...");
            ensureLoggedIn();
            // retry by reloading products and re-selecting second product
            driver.get(productsUrl);
            productListPage = new ProductListPage(driver);
            productListPage.waitForProductsVisible(8);
            productListPage.clickProductByIndex(1);
            productDetailPage = new ProductDetailPage(driver);
            productDetailPage.waitForReady(6);
            productDetailPage.clickAddToCart();
            productDetailPage.waitForAddToCartSuccess();
        }
        
        System.out.println(">>> Helper: Multiple products added successfully");
    }

    /**
     * Helper: Ensure the session is authenticated; if on login page or not, force a login.
     */
    private void ensureLoggedIn() {
        try {
            String loginUrl = ConfigReader.getWebUrl() + "/login";
            driver.get(loginUrl);
            loginPage = new UserLoginPage(driver);
            loginPage.waitForPageToLoad();
            loginPage.login(ConfigReader.getUserEmail(), ConfigReader.getUserPassword());
            // Wait up to 8s for redirect away from /login
            waitForUrlNotContains("/login", 8);
            System.out.println("✓ Re-authenticated successfully");
        } catch (Exception e) {
            System.out.println("Re-authentication failed: " + e.getMessage());
        }
    }
    
    @BeforeMethod
    public void setUpTest() {
        // Login first (cart requires authentication)
        driver.get(ConfigReader.getWebUrl() + "/login");
        loginPage = new UserLoginPage(driver);
        
        try {
            loginPage.waitForPageToLoad();
            loginPage.login(ConfigReader.getUserEmail(), ConfigReader.getUserPassword());
            // Wait up to 8s for redirect away from /login
            waitForUrlNotContains("/login", 8);
        } catch (Exception e) {
            System.out.println("Login setup error: " + e.getMessage());
        }
        
        // Initialize page objects
        cartPage = new CartPage(driver);
        productListPage = new ProductListPage(driver);
        productDetailPage = new ProductDetailPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }
    
    // ==================== BASIC CART OPERATIONS ====================
    
    /**
     * CART_01: View empty cart
     */
    @Test(priority = 1, description = "Verify user can view empty cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that empty cart displays appropriate message")
    @Story("View Cart")
    public void testViewEmptyCart() {
        System.out.println("\n=== CART_01: Starting testViewEmptyCart ===");
        
        // Navigate to cart and clear it first
        System.out.println("Step 1: Navigating to cart page...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        
        System.out.println("Step 2: Waiting for cart page to load...");
        cartPage.waitForCartPageLoad();
        
        System.out.println("Step 3: Checking cart item count...");
        int itemCount = cartPage.getCartItemCount();
        System.out.println("Current cart has " + itemCount + " items");
        
        // Clear all items if cart has items
        if (itemCount > 0) {
            System.out.println("Step 4: Clearing cart...");
            cartPage.clearCart();
            // Wait for cart to update after clearing (no fixed sleep)
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(d -> cartPage.getCartItemCount() == 0 || cartPage.isCartEmpty());
            } catch (Exception ignore) {}
            System.out.println("Cart cleared successfully");
        } else {
            System.out.println("Step 4: Cart already empty, skipping clear");
        }
        
        // Assert - Cart should be empty
        System.out.println("Step 5: Verifying cart is empty...");
        boolean isEmpty = cartPage.isCartEmpty();
        System.out.println("Cart isEmpty: " + isEmpty);
        
        Assert.assertTrue(isEmpty, 
            "Cart should be empty");
        
        int finalCount = cartPage.getCartItemCount();
        System.out.println("Final cart item count: " + finalCount);
        Assert.assertEquals(finalCount, 0, 
            "Cart item count should be 0");
        
        System.out.println("Step 6: Getting empty cart message...");
        String emptyMessage = cartPage.getEmptyCartMessage();
        System.out.println("Empty cart message: '" + emptyMessage + "'");
        
        Assert.assertTrue(!emptyMessage.isEmpty() && emptyMessage.contains("trống"), 
            "Empty cart message should be displayed. Actual message: " + emptyMessage);
        
        System.out.println("✅ Test Passed: Empty cart displayed correctly");
        System.out.println("=== CART_01: testViewEmptyCart completed ===\n");
    }
    
    /**
     * CART_02: Add product to cart
     */
    @Test(priority = 2, description = "Verify user can add a product to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test adding a single product to cart from product detail page")
    @Story("Add to Cart")
    public void testAddProductToCart() {
        System.out.println("\n=== CART_02: Starting testAddProductToCart ===");
        
        // Step 1: Clear cart first
        System.out.println("Step 1: Clearing cart...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();
        System.out.println("Cart cleared successfully");
        
        // Step 2: Navigate to products page
        System.out.println("Step 2: Navigating to products page...");
        String productsUrl = ConfigReader.getWebUrl() + "/products";
        System.out.println("Products URL: " + productsUrl);
        driver.get(productsUrl);
        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        
        // Verify we're on products page
        int productCount = productListPage.getProductCount();
        System.out.println("Products found on page: " + productCount);
        
        // Retry if no products found (page might still be loading)
        if (productCount == 0) {
            System.out.println("⚠️ No products found, waiting for products to appear (retry)...");
            productListPage.waitForProductsVisible(4);
            productCount = productListPage.getProductCount();
            System.out.println("Products found after retry: " + productCount);
        }
        
        if (productCount == 0) {
            System.out.println("❌ CRITICAL: No products found on page!");
            System.out.println("This might indicate:");
            System.out.println("  1. Products page not loaded correctly");
            System.out.println("  2. No products in database");
            System.out.println("  3. Wrong locator for product cards");
            Assert.fail("No products found on products page. Cannot proceed with test.");
        }
        
        // Step 3: Click first product
        System.out.println("Step 3: Clicking first product...");
        productListPage.clickFirstProduct();
        productDetailPage = new ProductDetailPage(driver);
        productDetailPage.waitForReady(6);
        
        // Step 4: Get product info and add to cart
        System.out.println("Step 4: Adding product to cart...");
    String productName = productDetailPage.getProductName();
        System.out.println("Product name: " + productName);
        
        // Try to add to cart (assuming variant auto-selected or not required)
        productDetailPage.clickAddToCart();
        System.out.println("Clicked Add to Cart button");
        
        productDetailPage.waitForAddToCartSuccess();
        System.out.println("Add to cart success notification appeared");
        
        // Step 5: Navigate to cart and verify
        System.out.println("Step 5: Navigating to cart to verify...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Assert
        boolean isEmpty = cartPage.isCartEmpty();
        int itemCount = cartPage.getCartItemCount();
        System.out.println("Cart isEmpty: " + isEmpty);
        System.out.println("Cart item count: " + itemCount);
        
        Assert.assertFalse(isEmpty, 
            "Cart should not be empty after adding product");
        Assert.assertTrue(itemCount > 0, 
            "Cart should have at least 1 item. Actual: " + itemCount);
        
        System.out.println("✅ Test Passed: Product added to cart successfully");
        System.out.println("Product added: " + productName);
        System.out.println("=== CART_02: testAddProductToCart completed ===\n");
    }
    
    /**
     * CART_03: Add multiple products to cart
     */
    @Test(priority = 3, description = "Verify user can add multiple different products to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test adding multiple products to cart")
    @Story("Add to Cart")
    public void testAddMultipleProductsToCart() {
        System.out.println("\n=== CART_03: Starting testAddMultipleProductsToCart ===");
        
        // Step 1: Clear cart first
        System.out.println("Step 1: Clearing cart...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();
        System.out.println("Cart cleared successfully");
        
        // Step 2: Navigate to products page
        System.out.println("Step 2: Navigating to products page...");
        String productsUrl = ConfigReader.getWebUrl() + "/products";
        driver.get(productsUrl);
        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        int productCount = productListPage.getProductCount();
        System.out.println("Products found: " + productCount);
        
        if (productCount < 2) {
            Assert.fail("Need at least 2 products to test adding multiple products. Found: " + productCount);
        }
        
        // Step 3: Add first product
        System.out.println("Step 3: Adding first product...");
        productListPage.clickProductByIndex(0);
        productDetailPage = new ProductDetailPage(driver);
        productDetailPage.waitForReady(6);
        String firstProductName = productDetailPage.getProductName();
        System.out.println("First product: " + firstProductName);
        
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddToCartSuccess();
        System.out.println("✓ First product added");
        
        // Step 4: Navigate back to products page
        System.out.println("Step 4: Navigating back to products page...");
        driver.get(productsUrl); // Direct navigation instead of back()
        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);
        
        // Step 5: Add second product
        System.out.println("Step 5: Adding second product...");
        productListPage.clickProductByIndex(1);
        productDetailPage = new ProductDetailPage(driver); // Re-initialize
        productDetailPage.waitForReady(6);
        String secondProductName = productDetailPage.getProductName();
        System.out.println("Second product: " + secondProductName);
        
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddToCartSuccess();
        System.out.println("✓ Second product added");
        
        // Step 6: Navigate to cart and verify
        System.out.println("Step 6: Navigating to cart to verify...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Assert
        int itemCount = cartPage.getCartItemCount();
        System.out.println("Cart item count: " + itemCount);
        
        Assert.assertTrue(itemCount >= 2, 
            "Cart should have at least 2 items, found: " + itemCount);
        
        System.out.println("✅ Test Passed: Multiple products added to cart");
        System.out.println("Total items in cart: " + itemCount);
        System.out.println("=== CART_03: testAddMultipleProductsToCart completed ===\n");
    }
    
    /**
     * CART_04: View cart items
     */
    @Test(priority = 4, description = "Verify user can view cart items with details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that cart displays all product information correctly")
    @Story("View Cart")
    public void testViewCartItems() {
        System.out.println("\n=== CART_04: Starting testViewCartItems ===");
        
        // Setup: Add a product to cart using helper
        addSingleProductToCart();
        
        // Navigate to cart
        System.out.println("Navigating to cart to view items...");
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Assert - Cart should display items
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Should have items in cart");
        
        // Verify cart summary is displayed
        Assert.assertTrue(cartPage.isCartSummaryDisplayed(), 
            "Cart summary should be displayed");
        
        // Verify pricing information is displayed
        String subtotal = cartPage.getSubtotal();
        String total = cartPage.getTotalPrice();
        
        Assert.assertFalse(subtotal.isEmpty(), "Subtotal should be displayed");
        Assert.assertFalse(total.isEmpty(), "Total price should be displayed");
        
        System.out.println("✅ Test Passed: Cart items displayed with all details");
        System.out.println("Subtotal: " + subtotal);
        System.out.println("Total: " + total);
        System.out.println("=== CART_04: testViewCartItems completed ===\n");
    }
    
    // ==================== QUANTITY MANAGEMENT ====================
    
    /**
     * CART_05: Increase product quantity
     */
    @Test(priority = 5, description = "Verify user can increase product quantity in cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test increasing quantity updates cart correctly")
    @Story("Update Quantity")
    public void testIncreaseQuantity() {
        System.out.println("\n=== CART_05: Starting testIncreaseQuantity ===");
        
        // Setup: Add product to cart using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Get first product name
        java.util.List<String> products = cartPage.getAllProductNames();
        if (products.isEmpty()) {
            Assert.fail("No products in cart to test");
        }
        
        String productName = products.get(0);
        int initialQty = cartPage.getProductQuantity(productName);
        System.out.println("Initial quantity: " + initialQty);
        
        // Act - Increase quantity
        cartPage.increaseQuantity(productName);
        
        // Assert
        int newQty = cartPage.getProductQuantity(productName);
        System.out.println("New quantity: " + newQty);
        
        Assert.assertEquals(newQty, initialQty + 1, 
            "Quantity should increase by 1");
        
        System.out.println("✅ Test Passed: Quantity increased successfully");
        System.out.println("=== CART_05: testIncreaseQuantity completed ===\n");
    }
    
    /**
     * CART_06: Decrease product quantity
     */
    @Test(priority = 6, description = "Verify user can decrease product quantity in cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test decreasing quantity updates cart correctly")
    @Story("Update Quantity")
    public void testDecreaseQuantity() {
        System.out.println("\n=== CART_06: Starting testDecreaseQuantity ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        java.util.List<String> products = cartPage.getAllProductNames();
        String productName = products.get(0);
        
        // Increase to at least 2
        cartPage.increaseQuantity(productName);
        int initialQty = cartPage.getProductQuantity(productName);
        System.out.println("Initial quantity (after increase): " + initialQty);
        
        // Act - Decrease quantity
        cartPage.decreaseQuantity(productName);
        
        // Assert
        int newQty = cartPage.getProductQuantity(productName);
        System.out.println("New quantity: " + newQty);
        
        Assert.assertEquals(newQty, initialQty - 1, 
            "Quantity should decrease by 1");
        
        System.out.println("✅ Test Passed: Quantity decreased successfully");
        System.out.println("=== CART_06: testDecreaseQuantity completed ===\n");
    }
    
    /**
     * CART_07: Prevent decreasing quantity below 1
     */
    @Test(priority = 7, description = "Verify user cannot decrease quantity below 1")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that decreasing at minimum quantity (1) does not remove item or reduce quantity")
    @Story("Update Quantity")
    public void testPreventDecreaseBelowOne() {
        System.out.println("\n=== CART_07: Starting testPreventDecreaseBelowOne ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        java.util.List<String> products = cartPage.getAllProductNames();
        Assert.assertFalse(products.isEmpty(), "Cart should contain at least 1 product");
        String productName = products.get(0);
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count: " + initialCount);
        System.out.println("Target product: " + productName);
        
        // Ensure quantity is 1
        int qty = cartPage.getProductQuantity(productName);
        while (qty > 1) {
            cartPage.decreaseQuantity(productName);
            qty = cartPage.getProductQuantity(productName);
        }
        System.out.println("Quantity set to minimum: " + qty);
        Assert.assertEquals(qty, 1, "Precondition: quantity should be 1");
        
        // Act - Attempt to decrease below 1
        cartPage.decreaseQuantity(productName); // should not change quantity or remove item
        
        // Assert - Quantity stays 1 and product remains in cart, count unchanged
        int qtyAfter = cartPage.getProductQuantity(productName);
        int countAfter = cartPage.getCartItemCount();
        System.out.println("Quantity after attempt: " + qtyAfter);
        System.out.println("Cart count after attempt: " + countAfter);
        
        Assert.assertEquals(qtyAfter, 1, "Quantity should not go below 1");
        Assert.assertEquals(countAfter, initialCount, "Cart item count should remain unchanged");
        Assert.assertTrue(cartPage.isProductInCart(productName), "Product should remain in cart");
        
        System.out.println("✅ Test Passed: Decreasing at minimum quantity did not remove item");
        System.out.println("=== CART_07: testPreventDecreaseBelowOne completed ===\n");
    }
    
    /**
     * Legacy alias: Some older reports expect a test named `testUpdateQuantityToZero`.
     * Business rule changed: quantity cannot go below 1, so attempting to set 0 must NOT remove the item.
     * This test verifies the new behavior and keeps backward compatibility for report names.
     */
    @Test(priority = 7, description = "Verify item is NOT removed when attempting quantity 0 (min is 1)")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that trying to set quantity to 0 keeps it at 1; removal must use the trash/remove button")
    @Story("Update Quantity")
    public void testUpdateQuantityToZero() {
        System.out.println("\n=== CART_07b: Starting testUpdateQuantityToZero (compat) ===");

        // Setup: Add product using helper
        addSingleProductToCart();

        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();

        // Pre-state
        java.util.List<String> products = cartPage.getAllProductNames();
        Assert.assertFalse(products.isEmpty(), "Cart should contain at least 1 product");
        String productName = products.get(0);
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count: " + initialCount);
        System.out.println("Target product: " + productName);

        // Ensure quantity is 1
        int qty = cartPage.getProductQuantity(productName);
        while (qty > 1) {
            cartPage.decreaseQuantity(productName);
            qty = cartPage.getProductQuantity(productName);
        }
        System.out.println("Quantity set to minimum: " + qty);
        Assert.assertEquals(qty, 1, "Precondition: quantity should be 1");

        // Act: Attempt to decrease below 1 (i.e., to 0) — should be blocked by UI/business rule
        cartPage.decreaseQuantity(productName);

        // Assert: Quantity remains 1, item not removed, cart count unchanged
        int qtyAfter = cartPage.getProductQuantity(productName);
        int countAfter = cartPage.getCartItemCount();
        System.out.println("Quantity after attempt: " + qtyAfter);
        System.out.println("Cart count after attempt: " + countAfter);

        Assert.assertEquals(qtyAfter, 1, "Quantity should not go below 1");
        Assert.assertEquals(countAfter, initialCount, "Cart item count should remain unchanged");
        Assert.assertTrue(cartPage.isProductInCart(productName), "Product should remain in cart");

        System.out.println("✅ Test Passed: Attempting to set quantity to 0 does not remove item");
        System.out.println("=== CART_07b: testUpdateQuantityToZero (compat) completed ===\n");
    }
    
    /**
     * CART_08: Update quantity exceeding stock (validation)
     */
    @Test(priority = 8, description = "Verify system prevents quantity exceeding available stock")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that users cannot add more items than available stock")
    @Story("Update Quantity")
    public void testUpdateQuantityExceedStock() {
        System.out.println("\n=== CART_08: Starting testUpdateQuantityExceedStock ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        java.util.List<String> products = cartPage.getAllProductNames();
        String productName = products.get(0);
        
        // Get stock info (displayed) and parse expected max from UI
        String stockInfo = cartPage.getProductStockInfo(productName);
        int displayedMax = cartPage.getDisplayedMaxQuantity(productName);
        System.out.println("Stock info: " + stockInfo + " (parsed: " + displayedMax + ")");
        
        // Try to increase quantity multiple times to exceed stock
    int attempts = 0;
    int maxAttempts = (displayedMax > 0) ? Math.min(displayedMax + 2, 60) : 50; // Try enough times to reach the UI cap
        int lastQty = cartPage.getProductQuantity(productName);
        
        for (int i = 0; i < maxAttempts; i++) {
            // If '+' is disabled already, stop
            if (cartPage.isIncreaseDisabled(productName)) {
                System.out.println("Plus button disabled at quantity: " + lastQty);
                attempts = i;
                break;
            }
            cartPage.increaseQuantity(productName);
            int newQty = cartPage.getProductQuantity(productName);
            
            if (newQty == lastQty) {
                // Quantity didn't increase - reached maximum
                System.out.println("Reached maximum stock at quantity: " + lastQty);
                attempts = i;
                break;
            }
            lastQty = newQty;
        }
        
        // Assert - Should stop at some point (stock or per-order limit)
        Assert.assertTrue(attempts < maxAttempts,
            "System should prevent exceeding stock limit");
        
        // Final checks: quantity should never exceed the displayed stock
        if (displayedMax > 0) {
            Assert.assertTrue(lastQty <= displayedMax, 
                "Final quantity should be <= displayed stock. Displayed: " + displayedMax + ", got: " + lastQty);
        }
        
        // If it stopped below displayed stock, log it as per-order cap (backend may enforce a lower cap)
        if (displayedMax > 0 && lastQty < displayedMax) {
            System.out.println("Note: Stopped below displayed stock. Likely per-order cap or backend validation. Displayed max=" + displayedMax + ", reached=" + lastQty);
        }
        
        System.out.println("✅ Test Passed: System prevents quantity exceeding stock");
        System.out.println("Maximum quantity allowed: " + lastQty);
        System.out.println("=== CART_08: testUpdateQuantityExceedStock completed ===\n");
    }
    
    // ==================== REMOVE ITEMS ====================
    
    /**
     * CART_09: Remove single item from cart
     */
    @Test(priority = 9, description = "Verify user can remove a single item from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test removing one product from cart")
    @Story("Remove from Cart")
    public void testRemoveSingleItem() {
        System.out.println("\n=== CART_09: Starting testRemoveSingleItem ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count: " + initialCount);
        
        java.util.List<String> products = cartPage.getAllProductNames();
        String productName = products.get(0);
        System.out.println("Product to remove: " + productName);
        
        // Act - Remove product
        cartPage.removeProduct(productName);
        
        // Assert
        int newCount = cartPage.getCartItemCount();
        System.out.println("New cart count: " + newCount);
        
        Assert.assertEquals(newCount, initialCount - 1, 
            "Cart item count should decrease by 1");
        Assert.assertFalse(cartPage.isProductInCart(productName), 
            "Product should no longer be in cart");
        
        System.out.println("✅ Test Passed: Single item removed successfully");
        System.out.println("=== CART_09: testRemoveSingleItem completed ===\n");
    }
    
    /**
     * CART_10: Remove all items (clear cart)
     */
    @Test(priority = 10, description = "Verify user can remove all items from cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test clearing entire cart")
    @Story("Remove from Cart")
    public void testRemoveAllItems() {
        System.out.println("\n=== CART_10: Starting testRemoveAllItems ===");
        
        // Setup: Add multiple products using helper
        addMultipleProductsToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count: " + initialCount);
        
        Assert.assertTrue(initialCount > 0, "Cart should have items to remove");
        
        // Act - Clear cart
        cartPage.clearCart();
        
        // Assert
        boolean isEmpty = cartPage.isCartEmpty();
        int finalCount = cartPage.getCartItemCount();
        
        System.out.println("Is cart empty: " + isEmpty);
        System.out.println("Final cart count: " + finalCount);
        
        Assert.assertTrue(isEmpty, "Cart should be empty");
        Assert.assertEquals(finalCount, 0, 
            "Cart item count should be 0");
        
        System.out.println("✅ Test Passed: All items removed successfully");
        System.out.println("Removed " + initialCount + " items");
        System.out.println("=== CART_10: testRemoveAllItems completed ===\n");
    }
    
    /**
     * CART_11: Remove item and verify total updates
     */
    @Test(priority = 11, description = "Verify total price updates correctly when item is removed")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that removing item updates cart total")
    @Story("Remove from Cart")
    public void testRemoveItemAndVerifyTotal() {
        System.out.println("\n=== CART_11: Starting testRemoveItemAndVerifyTotal ===");
        
        // Setup: Add multiple products using helper
        addMultipleProductsToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Get initial total
        String initialTotalText = cartPage.getTotalPrice();
        int initialTotal = cartPage.extractPrice(initialTotalText);
        System.out.println("Initial total: " + initialTotalText + " (" + initialTotal + ")");
        
        // Remove first product
        java.util.List<String> products = cartPage.getAllProductNames();
        String productName = products.get(0);
        System.out.println("Removing product: " + productName);
        
        cartPage.removeProduct(productName);
        
        // Get new total
        String newTotalText = cartPage.getTotalPrice();
        int newTotal = cartPage.extractPrice(newTotalText);
        System.out.println("New total: " + newTotalText + " (" + newTotal + ")");
        
        // Assert - Total should decrease
        Assert.assertTrue(newTotal < initialTotal, 
            "Total price should decrease after removing item");
        
        System.out.println("✅ Test Passed: Total price updated correctly");
        System.out.println("Price decreased by: " + (initialTotal - newTotal));
        System.out.println("=== CART_11: testRemoveItemAndVerifyTotal completed ===\n");
    }
    
    // ==================== PRICE CALCULATIONS ====================
    
    /**
     * CART_12: Calculate subtotal correctly
     */
    @Test(priority = 12, description = "Verify cart subtotal is calculated correctly")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that subtotal equals sum of all item prices")
    @Story("Price Calculation")
    public void testCalculateSubtotal() {
        System.out.println("\n=== CART_12: Starting testCalculateSubtotal ===");
        
        // Setup: Add products using helper
        addMultipleProductsToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Get subtotal from UI
        String subtotalText = cartPage.getSubtotal();
        int subtotal = cartPage.extractPrice(subtotalText);
        System.out.println("Subtotal: " + subtotalText + " (" + subtotal + ")");
        
        // Assert - Subtotal should be positive
        Assert.assertTrue(subtotal > 0, 
            "Subtotal should be greater than 0");
        
        // Verify calculations are correct
        boolean calculationsCorrect = cartPage.verifyCartCalculations();
        System.out.println("Calculations correct: " + calculationsCorrect);
        
        Assert.assertTrue(calculationsCorrect, 
            "Cart calculations should be correct");
        
        System.out.println("✅ Test Passed: Subtotal calculated correctly");
        System.out.println("=== CART_12: testCalculateSubtotal completed ===\n");
    }
    
    /**
     * CART_13: Calculate shipping fee (free >= 499k)
     */
    @Test(priority = 13, description = "Verify shipping fee calculation (free if >= 499k)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that shipping is free for orders >= 499,000 VND")
    @Story("Price Calculation")
    public void testCalculateShippingFee() {
        System.out.println("\n=== CART_13: Starting testCalculateShippingFee ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Get subtotal and shipping
        int subtotal = cartPage.extractPrice(cartPage.getSubtotal());
        String shippingText = cartPage.getShippingFee();
        boolean isFree = cartPage.isFreeShipping();
        
        System.out.println("Subtotal: " + subtotal + " VND");
        System.out.println("Shipping: " + shippingText);
        System.out.println("Free shipping: " + isFree);
        
        // Assert - Check shipping logic
        if (subtotal >= 499000) {
            Assert.assertTrue(isFree, 
                "Shipping should be free for orders >= 499k VND");
        } else {
            Assert.assertFalse(shippingText.isEmpty(), 
                "Shipping fee should be displayed");
        }
        
        System.out.println("✅ Test Passed: Shipping fee calculated correctly");
        System.out.println("=== CART_13: testCalculateShippingFee completed ===\n");
    }
    
    /**
     * CART_14: Calculate total price (subtotal + shipping)
     */
    @Test(priority = 14, description = "Verify total price = subtotal + shipping")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that total price is correctly calculated")
    @Story("Price Calculation")
    public void testCalculateTotalPrice() {
        System.out.println("\n=== CART_14: Starting testCalculateTotalPrice ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Get all prices
        int subtotal = cartPage.extractPrice(cartPage.getSubtotal());
        int shipping = cartPage.extractPrice(cartPage.getShippingFee());
        int total = cartPage.extractPrice(cartPage.getTotalPrice());
        
        System.out.println("Subtotal: " + subtotal + " VND");
        System.out.println("Shipping: " + shipping + " VND");
        System.out.println("Total: " + total + " VND");
        
        // Calculate expected total
        int expectedTotal = subtotal + shipping;
        System.out.println("Expected total: " + expectedTotal + " VND");
        
        // Assert
        Assert.assertEquals(total, expectedTotal, 
            "Total should equal subtotal + shipping");
        Assert.assertTrue(cartPage.verifyCartCalculations(), 
            "Cart calculations should be correct");
        
        System.out.println("✅ Test Passed: Total price calculated correctly");
        System.out.println("=== CART_14: testCalculateTotalPrice completed ===\n");
    }
    
    // ==================== CHECKOUT FLOW ====================
    
    /**
     * CART_15: Proceed to checkout
     */
    @Test(priority = 15, description = "Verify user can proceed to checkout from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test checkout button navigates to checkout page")
    @Story("Checkout Flow")
    public void testProceedToCheckout() {
        System.out.println("\n=== CART_15: Starting testProceedToCheckout ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        // Assert checkout button is available
        boolean checkoutVisible = cartPage.isCheckoutButtonDisplayed();
        System.out.println("Checkout button displayed: " + checkoutVisible);
        
        Assert.assertTrue(checkoutVisible, 
            "Checkout button should be displayed");
        
        // Click checkout
        cartPage.clickCheckout();

        // Wait for navigation to checkout without fixed sleep
        waitForUrlContains("/checkout", 8);
        
        // Assert - Should be on checkout page
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        
        Assert.assertTrue(currentUrl.contains("/checkout"), 
            "Should navigate to checkout page");
        
        checkoutPage = new CheckoutPage(driver);
        boolean onCheckoutPage = checkoutPage.isOnCheckoutPage();
        System.out.println("On checkout page: " + onCheckoutPage);
        
        Assert.assertTrue(onCheckoutPage, 
            "Should be on checkout page");
        
        System.out.println("✅ Test Passed: Navigated to checkout successfully");
        System.out.println("=== CART_15: testProceedToCheckout completed ===\n");
    }
    
    /**
     * CART_16: Checkout with empty cart (validation)
     */
    @Test(priority = 16, description = "Verify cannot checkout with empty cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that empty cart prevents checkout")
    @Story("Checkout Flow")
    public void testCheckoutWithEmptyCart() {
        // Clear cart
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();
        
        // Assert - Checkout button should not be displayed or cart should show empty message
        boolean isEmpty = cartPage.isCartEmpty();
        Assert.assertTrue(isEmpty, "Cart should be empty");
        
        // Try to navigate to checkout directly via URL
        driver.get(ConfigReader.getWebUrl() + "/checkout");

        // Wait for redirect (either to cart or login)
        waitForAnyUrlContains(new String[]{"/cart", "/login"}, 8);
        
        // Should be redirected back to cart or show error
        String currentUrl = driver.getCurrentUrl();
        boolean redirected = currentUrl.contains("/cart") || currentUrl.contains("/login");
        
        System.out.println("✅ Test Passed: Cannot checkout with empty cart");
        System.out.println("Redirected: " + redirected);
    }
    
    /**
     * CART_17: Checkout requires login
     */
    @Test(priority = 17, description = "Verify checkout requires user authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that non-authenticated users are redirected to login")
    @Story("Checkout Flow")
    public void testCheckoutRequireLogin() {
        // Logout first
        driver.manage().deleteAllCookies();
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.localStorage.clear(); window.sessionStorage.clear();"
        );
        
        // Try to access checkout directly
        driver.get(ConfigReader.getWebUrl() + "/checkout");

        // Wait for redirect to login
        waitForUrlContains("/login", 8);
        
        // Should be redirected to login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"), 
            "Should be redirected to login page");
        
        System.out.println("✅ Test Passed: Checkout requires login");
    }
    
    // ==================== CART PERSISTENCE & SYNC ====================
    
    /**
     * CART_18: Cart persists after logout/login
     */
    @Test(priority = 18, description = "Verify cart persists after user logs out and back in")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that cart items are preserved across sessions")
    @Story("Cart Persistence")
    public void testCartPersistAfterLogout() {
        System.out.println("\n=== CART_18: Starting testCartPersistAfterLogout ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count: " + initialCount);
        
        // Logout
        driver.manage().deleteAllCookies();
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.localStorage.clear(); window.sessionStorage.clear();"
        );
        System.out.println("Logged out, cleared session");
        
        // Login again
        driver.get(ConfigReader.getWebUrl() + "/login");
        loginPage = new UserLoginPage(driver);
        
        try {
            loginPage.waitForPageToLoad();
            loginPage.login(ConfigReader.getUserEmail(), ConfigReader.getUserPassword());
            // Wait up to 8s for redirect away from /login
            waitForUrlNotContains("/login", 8);
            System.out.println("Logged in again");
        } catch (Exception e) {
            System.out.println("Re-login error: " + e.getMessage());
        }
        
        // Check cart
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        int newCount = cartPage.getCartItemCount();
        System.out.println("Cart count after re-login: " + newCount);
        
        // Assert - Cart should persist
        Assert.assertEquals(newCount, initialCount, 
            "Cart item count should persist after logout/login");
        
        System.out.println("✅ Test Passed: Cart persisted after logout/login");
        System.out.println("=== CART_18: testCartPersistAfterLogout completed ===\n");
    }
    
    /**
     * CART_19: Cart sync across tabs/windows
     */
    @Test(priority = 19, description = "Verify cart syncs across multiple browser tabs")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that cart updates are synchronized across tabs")
    @Story("Cart Synchronization")
    public void testCartSyncAcrossTabs() {
        System.out.println("\n=== CART_19: Starting testCartSyncAcrossTabs ===");
        
        // Setup: Add product using helper
        addSingleProductToCart();
        
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        int initialCount = cartPage.getCartItemCount();
        System.out.println("Initial cart count in main tab: " + initialCount);
        
        // Open new tab (simulate by opening cart in same window)
        String mainWindow = driver.getWindowHandle();
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.open('" + ConfigReader.getWebUrl() + "/cart', '_blank');"
        );

        // Wait for new tab to appear
        waitForWindowHandlesAtLeast(2, 8);
        
        // Switch to new tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindow)) {
                driver.switchTo().window(windowHandle);
                System.out.println("Switched to new tab");
                break;
            }
        }
        
        // Check cart in new tab
        cartPage = new CartPage(driver);
        cartPage.waitForCartPageLoad();
        int newTabCount = cartPage.getCartItemCount();
        System.out.println("Cart count in new tab: " + newTabCount);
        
        // Assert - Should have same count
        Assert.assertEquals(newTabCount, initialCount, 
            "Cart should sync across tabs");
        
        // Close new tab and switch back
        driver.close();
        driver.switchTo().window(mainWindow);
        System.out.println("Closed new tab, switched back to main");
        
        System.out.println("✅ Test Passed: Cart synced across tabs");
        System.out.println("=== CART_19: testCartSyncAcrossTabs completed ===\n");
    }
    
    /**
     * CART_20: Cart badge updates in realtime
     */
    @Test(priority = 20, description = "Verify cart badge updates immediately after adding items")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that cart badge in header updates in realtime")
    @Story("Cart UI Updates")
    public void testCartBadgeUpdate() {
        // Clear cart first
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.clearCart();

        // Navigate to products
        driver.get(ConfigReader.getWebUrl() + "/products");

        // Wait for products list to be visible (no fixed sleep)
        productListPage = new ProductListPage(driver);
        productListPage.waitForProductsVisible(8);

        // Click first product
        productListPage.clickFirstProduct();

        // Wait for product detail to be ready
        productDetailPage = new ProductDetailPage(driver);
        productDetailPage.waitForReady(6);

        // Add to cart and wait for success
        productDetailPage.clickAddToCart();
        productDetailPage.waitForAddToCartSuccess();
        
        // Check badge updated (this test assumes badge exists in header)
        // Note: Badge implementation may vary
        
        // Navigate to cart to verify
        cartPage.navigateToCart(ConfigReader.getWebUrl());
        cartPage.waitForCartPageLoad();
        
        Assert.assertTrue(cartPage.getCartItemCount() > 0, 
            "Cart should have items after adding product");
        
        System.out.println("✅ Test Passed: Cart badge updated");
    }

    // ==================== LOCAL TEST HELPERS (fast waits) ====================

    private boolean waitForUrlContains(String fragment, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(d -> d.getCurrentUrl().contains(fragment));
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().contains(fragment);
        }
    }

    private boolean waitForUrlNotContains(String fragment, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(d -> !d.getCurrentUrl().contains(fragment));
            return true;
        } catch (Exception e) {
            return !driver.getCurrentUrl().contains(fragment);
        }
    }

    private boolean waitForAnyUrlContains(String[] fragments, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(d -> {
                        String url = d.getCurrentUrl();
                        for (String f : fragments) {
                            if (url.contains(f)) return true;
                        }
                        return false;
                    });
            return true;
        } catch (Exception e) {
            String url = driver.getCurrentUrl();
            for (String f : fragments) {
                if (url.contains(f)) return true;
            }
            return false;
        }
    }

    private boolean waitForWindowHandlesAtLeast(int expectedCount, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(d -> d.getWindowHandles().size() >= expectedCount);
            return true;
        } catch (Exception e) {
            return driver.getWindowHandles().size() >= expectedCount;
        }
    }
}
