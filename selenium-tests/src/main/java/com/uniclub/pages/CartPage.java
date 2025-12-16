package com.uniclub.pages;

import com.uniclub.base.BasePage;
import com.uniclub.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * CartPage - Page Object for Shopping Cart page (User side)
 * URL: http://localhost:5173/cart
 */
public class CartPage extends BasePage {
    
    // Locators
    private final By pageTitle = By.xpath("//h1[contains(text(),'Giỏ hàng')]");
    private final By loadingSpinner = By.cssSelector(".animate-spin");
    
    // Empty cart state
    private final By emptyCartIcon = By.xpath("//*[name()='svg' and contains(@class,'w-16') and contains(@class,'h-16') and contains(@class,'text-muted-foreground')]");
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'Giỏ hàng trống')]");
    private final By exploreProductsButton = By.xpath("//button[contains(text(),'Khám phá sản phẩm')]");
    
    // Cart items
    private final By cartItems = By.xpath("//div[contains(@class,'lg:col-span-2')]//div[contains(@class,'card') and contains(@class,'p-4') and contains(@class,'flex')]");
    private final By cartItemContainer = By.xpath("//div[contains(@class,'lg:col-span-2')]");
    
    // Item details (relative to cart item)
    private final By itemName = By.cssSelector("a.font-medium");
    private final By itemSize = By.cssSelector(".text-sm.text-muted-foreground");
    private final By itemUnitPrice = By.xpath(".//div[contains(text(),'Đơn giá:')]");
    private final By itemSubtotal = By.xpath(".//div[contains(text(),'Thành tiền')]/following-sibling::div");
    private final By itemStockInfo = By.xpath(".//span[contains(text(),'Còn lại:')]/following-sibling::span");
    
    // Quantity controls (relative to cart item)
    private final By quantityDisplay = By.cssSelector("span.text-sm.font-semibold, span.font-semibold");
    private final By qtyContainer = By.xpath(".//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'gap-1')]");
    private final By removeButton = By.xpath(".//button[contains(@class,'flex-shrink-0')]");
    
    // Cart summary (right panel)
    private final By cartSummaryCard = By.xpath("//div[contains(@class,'lg:col-span-1')]//div[contains(@class,'card')]");
    private final By summaryTitle = By.xpath("//h3[contains(text(),'Tóm tắt đơn hàng')]");
    private final By subtotalLabel = By.xpath("//span[contains(text(),'Tạm tính')]");
    private final By subtotalValue = By.xpath("//span[contains(text(),'Tạm tính')]/following-sibling::*");
    private final By shippingLabel = By.xpath("//span[contains(text(),'Phí vận chuyển')]");
    private final By shippingValue = By.xpath("//span[contains(text(),'Phí vận chuyển')]/following-sibling::*");
    private final By totalLabel = By.xpath("//span[contains(text(),'Tổng cộng')]");
    private final By totalValue = By.xpath("//span[contains(text(),'Tổng cộng')]/following-sibling::span");
    private final By checkoutButton = By.xpath("//button[contains(text(),'Thanh toán')]");
    private final By freeShippingNote = By.xpath("//*[contains(text(),'Miễn phí vận chuyển')]");
    
    /**
     * Constructor
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to cart page
     */
    public void navigateToCart(String baseUrl) {
        String cartUrl = baseUrl + "/cart";
        System.out.println("Navigating to cart: " + cartUrl);
        driver.get(cartUrl);
        waitForPageLoad();
        System.out.println("Current URL after navigation: " + getCurrentUrl());
    }
    
    /**
     * Check if on cart page
     */
    public boolean isOnCartPage() {
        try {
            waitForPageLoad();
            String currentUrl = getCurrentUrl();
            return currentUrl.contains("/cart") || isDisplayed(pageTitle);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for page to load (no loading spinner)
     */
    public void waitForCartPageLoad() {
        try {
            waitForPageLoad();
            
            try {
                List<WebElement> spinners = driver.findElements(loadingSpinner);
                if (!spinners.isEmpty() && spinners.get(0).isDisplayed()) {
                    waitForElementToBeInvisible(loadingSpinner, 2);
                }
            } catch (Exception e) {
                // Spinner not found or already gone - that's fine
            }
            
        } catch (Exception e) {
            System.out.println("Cart page load wait completed with exception: " + e.getMessage());
        }
    }
    
    /**
     * Check if cart is empty (without waiting again)
     */
    public boolean isCartEmpty() {
        int originalImplicit = ConfigReader.getImplicitWait();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);

            List<WebElement> emptyMsgs = driver.findElements(emptyCartMessage);
            for (WebElement el : emptyMsgs) {
                try {
                    if (el.isDisplayed()) return true;
                } catch (Exception ignore) { }
            }
            List<WebElement> emptyIcons = driver.findElements(emptyCartIcon);
            for (WebElement el : emptyIcons) {
                try {
                    if (el.isDisplayed()) return true;
                } catch (Exception ignore) { }
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore) {}
        }
    }
    
    /**
     * Check if empty cart message is displayed
     */
    public String getEmptyCartMessage() {
        try {
            WebElement messageElement = waitForElementToBeVisible(emptyCartMessage, 5);
            return messageElement.getText();
        } catch (Exception e) {
            try {
                return getText(emptyCartMessage);
            } catch (Exception ex) {
                return "";
            }
        }
    }
    
    /**
     * Click explore products button (from empty cart page)
     */
    public void clickExploreProducts() {
        try {
            click(exploreProductsButton);
            waitForPageLoad();
        } catch (Exception e) {
            System.out.println("Explore products button not found or not clickable");
        }
    }
    
    /**
     * Get number of items in cart (without redundant waiting)
     */
    public int getCartItemCount() {
        int originalImplicit = ConfigReader.getImplicitWait();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore) {}
        }
    }
    
    /**
     * Get cart item by product name (without redundant waiting)
     */
    public WebElement getCartItemByName(String productName) {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            for (WebElement item : items) {
                String name = item.findElement(itemName).getText();
                if (name != null && name.toLowerCase().contains(productName.toLowerCase())) {
                    return item;
                }
            }
        } catch (Exception e) {
            System.out.println("Item not found: " + productName);
        }
        return null;
    }
    
    /**
     * Check if product exists in cart
     */
    public boolean isProductInCart(String productName) {
        return getCartItemByName(productName) != null;
    }
    
    /**
     * Get product quantity in cart
     */
    public int getProductQuantity(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                return readQuantityFromItem(item);
            }
        } catch (Exception e) {
            System.out.println("Failed to get quantity for: " + productName);
        }
        return 0;
    }

    private int readQuantityFromItem(WebElement item) {
        int originalImplicit = ConfigReader.getImplicitWait();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            // 1) input[type=number] or named quantity input
            List<WebElement> inputs = item.findElements(By.cssSelector("input[type='number'], input[name='quantity']"));
            if (!inputs.isEmpty()) {
                String val = inputs.get(0).getAttribute("value");
                int v = parseDigits(val);
                if (v >= 0) return v;
            }
            // 2) span inside quantity container
            List<WebElement> spans = item.findElements(By.xpath(".//div[contains(@class,'items-center') and contains(@class,'gap-1')]//span[normalize-space(.)]"));
            for (WebElement s : spans) {
                int v = parseDigits(s.getText());
                if (v >= 0) return v;
            }
            // 3) any span with pure digits
            List<WebElement> allSpans = item.findElements(By.cssSelector("span"));
            for (WebElement s : allSpans) {
                String t = s.getText();
                if (t != null && t.trim().matches("^\\d+$")) {
                    return Integer.parseInt(t.trim());
                }
            }
        } catch (Exception ignore) {
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore2) {}
        }
        return 0;
    }

    private int parseDigits(String text) {
        try {
            if (text == null) return -1;
            String digits = text.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) return -1;
            return Integer.parseInt(digits);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Get product unit price
     */
    public String getProductUnitPrice(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                return item.findElement(itemUnitPrice).getText();
            }
        } catch (Exception e) {
            System.out.println("Failed to get unit price for: " + productName);
        }
        return "";
    }
    
    /**
     * Get product subtotal
     */
    public String getProductSubtotal(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                return item.findElement(itemSubtotal).getText();
            }
        } catch (Exception e) {
            System.out.println("Failed to get subtotal for: " + productName);
        }
        return "";
    }
    
    /**
     * Get product stock info
     */
    public String getProductStockInfo(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item == null) return "";
            By stockContainer = By.xpath(".//div[contains(@class,'text-xs') and contains(@class,'text-muted-foreground') and contains(.,'Còn lại')]");
            WebElement container = item.findElement(stockContainer);
            WebElement numberSpan = container.findElement(By.xpath(".//span"));
            String num = numberSpan.getText();
            if (num != null && !num.isEmpty()) return num;
            return container.getText();
        } catch (Exception e) {
            System.out.println("Failed to get stock info for: " + productName);
            return "";
        }
    }

    /**
     * Get displayed max quantity number from stock info
     */
    public int getDisplayedMaxQuantity(String productName) {
        try {
            String stock = getProductStockInfo(productName);
            if (stock == null) return -1;
            String digits = stock.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) return -1;
            return Integer.parseInt(digits);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Increase product quantity
     */
    public void increaseQuantity(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                int before = readQuantityFromItem(item);
                WebElement container = item.findElement(qtyContainer);
                WebElement plusBtn = container.findElements(By.xpath(".//button")).get(1);
                try {
                    String disabledAttr = plusBtn.getDomAttribute("disabled");
                    if (disabledAttr != null) {
                        return;
                    }
                } catch (Exception ignore) { }
                plusBtn.click();
                new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(d -> readQuantityFromItem(item) != before);
            }
        } catch (Exception e) {
            System.out.println("Failed to increase quantity for: " + productName);
        }
    }
    
    /**
     * Decrease product quantity
     */
    public void decreaseQuantity(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                int before = readQuantityFromItem(item);
                WebElement container = item.findElement(qtyContainer);
                WebElement minusBtn = container.findElements(By.xpath(".//button")).get(0);
                minusBtn.click();
                new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(d -> readQuantityFromItem(item) != before);
            }
        } catch (Exception e) {
            System.out.println("Failed to decrease quantity for: " + productName);
        }
    }

    /**
     * Check if increase button is disabled
     */
    public boolean isIncreaseDisabled(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item == null) return false;
            WebElement container = item.findElement(qtyContainer);
            WebElement plusBtn = container.findElements(By.xpath(".//button")).get(1);
            String disabledAttr = plusBtn.getDomAttribute("disabled");
            return disabledAttr != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Update product quantity to specific value
     */
    public void updateQuantity(String productName, int targetQuantity) {
        try {
            int currentQty = getProductQuantity(productName);
            int diff = targetQuantity - currentQty;
            
            if (diff > 0) {
                for (int i = 0; i < diff; i++) {
                    increaseQuantity(productName);
                }
            } else if (diff < 0) {
                for (int i = 0; i < Math.abs(diff); i++) {
                    decreaseQuantity(productName);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to update quantity for: " + productName);
        }
    }
    
    /**
     * Remove product from cart
     */
    public void removeProduct(String productName) {
        try {
            WebElement item = getCartItemByName(productName);
            if (item != null) {
                int before = getCartItemCountWithoutWait();
                WebElement trashBtn = item.findElement(removeButton);
                trashBtn.click();
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(d -> getCartItemCountWithoutWait() < before);
            }
        } catch (Exception e) {
            System.out.println("Failed to remove product: " + productName);
        }
    }
    
    /**
     * Get cart subtotal
     */
    public String getSubtotal() {
        try {
            if (getCartItemCountWithoutWait() == 0) return "0₫";
            return waitForElementToBeVisible(subtotalValue, 3).getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get shipping fee
     */
    public String getShippingFee() {
        try {
            if (getCartItemCountWithoutWait() == 0) return "0₫";
            String value = waitForElementToBeVisible(shippingValue, 3).getText();
            if (value == null || value.isEmpty()) return "";
            if (value.contains("Miễn phí")) return "0₫";
            return value;
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if shipping is free
     */
    public boolean isFreeShipping() {
        try {
            String shipping = getShippingFee();
            return shipping.contains("Miễn phí") || shipping.equals("0₫");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get total price
     */
    public String getTotalPrice() {
        try {
            if (getCartItemCountWithoutWait() == 0) return "0₫";
            return waitForElementToBeVisible(totalValue, 3).getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if checkout button is displayed
     */
    public boolean isCheckoutButtonDisplayed() {
        try {
            return isDisplayed(checkoutButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if checkout button is enabled
     */
    public boolean isCheckoutButtonEnabled() {
        try {
            return isEnabled(checkoutButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Click checkout button
     */
    public void clickCheckout() {
        try {
            click(checkoutButton);
            waitForPageLoad();
        } catch (Exception e) {
            System.out.println("Checkout button not found or not clickable");
        }
    }
    
    /**
     * Get free shipping note text
     */
    public String getFreeShippingNote() {
        try {
            return getText(freeShippingNote);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if cart summary is displayed
     */
    public boolean isCartSummaryDisplayed() {
        try {
            return isDisplayed(cartSummaryCard) && isDisplayed(summaryTitle);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get all product names in cart
     */
    public List<String> getAllProductNames() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            java.util.List<String> names = new java.util.ArrayList<>();
            for (WebElement item : items) {
                try {
                    String name = item.findElement(itemName).getText();
                    if (name != null && !name.isEmpty()) names.add(name);
                } catch (Exception ignore) { }
            }
            return names;
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * Clear all items from cart (OPTIMIZED)
     */
    public void clearCart() {
        try {
            waitForCartPageLoad();
            
            int itemCount = getCartItemCountWithoutWait();
            
            if (itemCount == 0) {
                System.out.println("Cart is already empty");
                return;
            }
            
            System.out.println("Clearing cart - " + itemCount + " items to remove");
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            int removedCount = 0;
            
            while (itemCount > 0) {
                List<String> productNames = getAllProductNamesWithoutWait();
                if (productNames.isEmpty()) {
                    break;
                }
                
                int currentCount = itemCount;
                String productToRemove = productNames.get(0);
                
                System.out.println("Removing item " + (++removedCount) + "/" + currentCount + ": " + productToRemove);
                removeProduct(productToRemove);
                
                try {
                    localWait.until(drv -> {
                        int newCount = getCartItemCountWithoutWait();
                        return newCount < currentCount;
                    });
                } catch (Exception e) {
                    System.out.println("⚠️ Timeout waiting for cart update, continuing...");
                }
                
                itemCount = getCartItemCountWithoutWait();
            }
            
            try {
                localWait.until(drv -> isCartEmptyWithoutWait());
                System.out.println("✅ Cart cleared successfully - " + removedCount + " items removed");
            } catch (Exception e) {
                System.out.println("⚠️ Cart may not be completely empty");
            }
            
        } catch (Exception e) {
            System.out.println("Failed to clear cart: " + e.getMessage());
        }
    }
    
    /**
     * Get cart item count WITHOUT waiting
     */
    private int getCartItemCountWithoutWait() {
        int originalImplicit = ConfigReader.getImplicitWait();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore) {}
        }
    }
    
    /**
     * Check if cart is empty WITHOUT waiting
     */
    private boolean isCartEmptyWithoutWait() {
        int originalImplicit = ConfigReader.getImplicitWait();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            List<WebElement> emptyMsgs = driver.findElements(emptyCartMessage);
            for (WebElement el : emptyMsgs) {
                try { if (el.isDisplayed()) return true; } catch (Exception ignore) {}
            }
            List<WebElement> emptyIcons = driver.findElements(emptyCartIcon);
            for (WebElement el : emptyIcons) {
                try { if (el.isDisplayed()) return true; } catch (Exception ignore) {}
            }
            return false;
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore) {}
        }
    }
    
    /**
     * Get all product names WITHOUT waiting
     */
    private List<String> getAllProductNamesWithoutWait() {
        int originalImplicit = ConfigReader.getImplicitWait();
        List<String> names = new java.util.ArrayList<>();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            List<WebElement> items = driver.findElements(cartItems);
            for (WebElement item : items) {
                try {
                    String name = item.findElement(itemName).getText();
                    names.add(name);
                } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            System.out.println("Error getting product names: " + e.getMessage());
        } finally {
            try { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicit)); } catch (Exception ignore) {}
        }
        return names;
    }
    
    /**
     * Extract numeric value from price string
     */
    public int extractPrice(String priceText) {
        try {
            String cleaned = priceText.replace("₫", "").replace(".", "").replace(",", "").trim();
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            System.out.println("Failed to parse price: " + priceText);
            return 0;
        }
    }

    /**
     * Verify cart calculations are correct
     */
    public boolean verifyCartCalculations() {
        try {
            int subtotal = extractPrice(getSubtotal());
            int shipping = extractPrice(getShippingFee());
            int total = extractPrice(getTotalPrice());
            int expectedTotal = subtotal + shipping;
            return total == expectedTotal;
        } catch (Exception e) {
            System.out.println("Failed to verify cart calculations");
            return false;
        }
    }
    
    /**
     * Wait for cart to load (legacy method for compatibility)
     */
    public void waitForCartToLoad() {
        waitForCartPageLoad();
    }
    
    /**
     * Click continue shopping (if button exists)
     */
    public void clickContinueShopping() {
        try {
            By continueButton = By.xpath("//a[contains(@href,'/products')] | //button[contains(text(),'Tiếp tục mua sắm')]");
            click(continueButton);
            waitForPageLoad();
        } catch (Exception e) {
            System.out.println("Continue shopping button not found");
        }
    }
    
    /**
     * Remove first item from cart (legacy method)
     */
    public void removeFirstItem() {
        try {
            List<String> names = getAllProductNames();
            if (!names.isEmpty()) {
                removeProduct(names.get(0));
            }
        } catch (Exception e) {
            System.out.println("Failed to remove first item");
        }
    }
}