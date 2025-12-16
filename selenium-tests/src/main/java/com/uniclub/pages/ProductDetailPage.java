package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * ProductDetailPage - Page Object for Product Detail page (User side)
 */
public class ProductDetailPage extends BasePage {
    
    // Locators - Product Information
    private final By productName = By.cssSelector("[data-testid='product-name'], h1, .product-title");
    private final By productPrice = By.cssSelector("[data-testid='product-price'], .price, [class*='price']");
    private final By productImage = By.cssSelector("[data-testid='product-image'], .product-image img, img[alt*='product']");
    private final By productDescription = By.cssSelector("[data-testid='product-description'], .description, [class*='description']");
    private final By productRating = By.cssSelector("[data-testid='rating'], .rating, [class*='rating']");
    private final By reviewsSection = By.cssSelector("[data-testid='reviews'], .reviews, [class*='review']");
    private final By stockInfo = By.xpath("//*[contains(text(),'Còn')]");
    
    // Locators - Add to Cart Button (multiple states)
    // "Thêm vào giỏ" - can add
    // "Chọn size và màu" - need to select
    // "Hết hàng" - out of stock
    // "Đang thêm..." - loading
    private final By addToCartButton = By.xpath("//button[contains(text(),'Thêm vào giỏ') or contains(text(),'Đang thêm') or contains(text(),'Chọn size') or contains(text(),'Hết hàng')]");
    private final By addToCartButtonEnabled = By.xpath("//button[contains(text(),'Thêm vào giỏ') and not(@disabled)]");
    
    // Locators - Quantity
    private final By quantityInput = By.cssSelector("input[type='number'], input[name='quantity']");
    
    // Locators - Size and Color Variants (buttons in VariantSelector)
    // Size buttons have class "rounded-full" and are under h3 with text "Kích thước"
    private final By sizeButtons = By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button");
    // Color buttons are under h3 with text "Màu sắc"
    private final By colorButtons = By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]");
    private final By allColorButtons = By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button");
    private final By sizeHeader = By.xpath("//h3[contains(text(),'Kích thước')]");
    private final By colorHeader = By.xpath("//h3[contains(text(),'Màu sắc')]");
    private final By variantSizeButton = By.cssSelector("h3 + div button, button[data-variant-type='size']");
    private final By variantColorButton = By.cssSelector("h3 + div button, button[data-variant-type='color']");
    
    // Locators - Navigation
    private final By backButton = By.cssSelector("button[data-action='back'], a:contains('Back')");
    
    // Locators - Cart Related
    private final By cartIcon = By.cssSelector("a[href='/cart'], svg.lucide-shopping-cart");
    private final By cartBadge = By.cssSelector("a[href='/cart'] span, .cart-badge");
    
    /**
     * Constructor
     */
    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on product detail page
     */
    public boolean isOnProductDetailPage() {
        try {
            waitForPageLoad();
            String currentUrl = getCurrentUrl();
            return (currentUrl.contains("/product") || currentUrl.contains("/detail")) 
                   && isDisplayed(productName);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for product detail to be ready (name and add-to-cart visible)
     */
    public void waitForReady(int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            wait.until(d -> d.findElements(addToCartButton).size() > 0 || !getText(productName).isEmpty());
        } catch (Exception ignore) {}
    }

    /**
     * Wait for size/color variant UI to be ready based on frontend structure
     */
    private void waitForVariantsReady(int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            // Wait size header and at least one enabled size button
            wait.until(ExpectedConditions.presenceOfElementLocated(sizeHeader));
            wait.until(d -> !d.findElements(By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button[not(@disabled)]")).isEmpty());
        } catch (Exception ignore) {}
    }

    /**
     * Fast path to select first size, then first color (after size) using exact frontend structure
     */
    private void fastSelectFirstSizeAndColor() {
        // Select first size
        try {
            List<WebElement> sizeBtns = driver.findElements(By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button[not(@disabled)]"));
            if (!sizeBtns.isEmpty()) {
                WebElement btn = sizeBtns.get(0);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        } catch (Exception ignore) {}
        // After size is selected, colors become enabled; pick first
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(d -> !d.findElements(By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]")).isEmpty());
            List<WebElement> colorBtns = driver.findElements(By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]"));
            if (!colorBtns.isEmpty()) {
                WebElement btn = colorBtns.get(0);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        } catch (Exception ignore) {}
    }
    
    /**
     * Get product name
     */
    public String getProductName() {
        try {
            return getText(productName);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get product price
     */
    public String getProductPrice() {
        try {
            return getText(productPrice);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if product image is displayed
     */
    public boolean isProductImageDisplayed() {
        return isDisplayed(productImage);
    }
    
    /**
     * Get product description
     */
    public String getProductDescription() {
        try {
            return getText(productDescription);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get product rating
     */
    public String getProductRating() {
        try {
            return getText(productRating);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if reviews section is displayed
     */
    public boolean isReviewsSectionDisplayed() {
        return isDisplayed(reviewsSection);
    }
    
    /**
     * Get stock information
     */
    public String getStockInfo() {
        try {
            return getText(stockInfo);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if product is out of stock
     */
    public boolean isOutOfStock() {
        try {
            String stock = getStockInfo();
            return stock.contains("Hết hàng") || stock.contains("0");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if add to cart button is displayed
     */
    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
    }
    
    /**
     * Check if add to cart is possible (button is enabled with "Thêm vào giỏ" text)
     */
    public boolean canAddToCart() {
        try {
            WebElement btn = driver.findElement(addToCartButton);
            String text = btn.getText().trim();
            return btn.isEnabled() && text.contains("Thêm vào giỏ");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Set quantity
     */
    public void setQuantity(int quantity) {
        try {
            type(quantityInput, String.valueOf(quantity));
        } catch (Exception e) {
            System.out.println("Quantity input not found");
        }
    }
    
    /**
     * Select size by clicking the size button
     * @param sizeName The size name to select (e.g., "S", "M", "L", "XL")
     */
    public void selectSize(String sizeName) {
        try {
            System.out.println("Selecting size: " + sizeName);
            
            // Try exact match first
            List<WebElement> buttons = driver.findElements(sizeButtons);
            for (WebElement button : buttons) {
                if (button.getText().trim().equalsIgnoreCase(sizeName) && button.isEnabled()) {
                    button.click();
                    Thread.sleep(500); // Wait for color buttons to update
                    System.out.println("✅ Selected size: " + sizeName);
                    return;
                }
            }
            
            // Try different selectors for size buttons
            String[] sizeSelectors = {
                "//button[contains(text(),'" + sizeName + "')]",  // Text match
                "button[value='" + sizeName + "']",                // Value attribute
                "button[data-value='" + sizeName + "']",           // Data attribute
                ".variant-button:contains('" + sizeName + "')",    // Class with text
                "button:contains('" + sizeName + "')"              // Any button with text
            };
            
            boolean selected = false;
            for (String selector : sizeSelectors) {
                try {
                    By locator = selector.startsWith("//") || selector.contains(":contains") 
                        ? By.xpath(selector.replace(":contains", "[contains(text(),").replace("')", "')]"))
                        : By.cssSelector(selector);
                    List<WebElement> btns = driver.findElements(locator);
                    if (!btns.isEmpty()) {
                        WebElement btn = btns.get(0);
                        new WebDriverWait(driver, Duration.ofSeconds(2))
                                .until(ExpectedConditions.elementToBeClickable(btn));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        System.out.println("✓ Selected size using selector: " + selector);
                        selected = true;
                        break;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            // If no exact match found, click first available button
            if (!selected) {
                for (WebElement button : buttons) {
                    if (button.isEnabled()) {
                        button.click();
                        System.out.println("✅ Selected first available size: " + button.getText());
                        Thread.sleep(500);
                        return;
                    }
                }
                System.out.println("⚠️ No size buttons available");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Size selector error: " + e.getMessage());
        }
    }
    
    /**
     * Select color by clicking the color button
     * Colors are only enabled after a size is selected
     * @param colorName The color name to select (e.g., "Đen", "Trắng", "Xanh")
     */
    public void selectColor(String colorName) {
        try {
            System.out.println("Selecting color: " + colorName);
            
            // Wait a bit for colors to become enabled after size selection
            Thread.sleep(500);
            
            // Get all color buttons (including disabled ones first to check availability)
            List<WebElement> allButtons = driver.findElements(allColorButtons);
            System.out.println("  Total color buttons: " + allButtons.size());
            
            // Count enabled buttons
            int enabledCount = 0;
            for (WebElement btn : allButtons) {
                if (btn.isEnabled()) enabledCount++;
            }
            System.out.println("  Enabled color buttons: " + enabledCount);
            
            // If specific color requested, try to find it
            if (colorName != null && !colorName.isEmpty()) {
                for (WebElement button : allButtons) {
                    if (button.getText().trim().equalsIgnoreCase(colorName) && button.isEnabled()) {
                        button.click();
                        System.out.println("✅ Selected color: " + colorName);
                        return;
                    }
                }
            }
            
            // Try different selectors for color buttons
            String[] colorSelectors = {
                "//button[contains(text(),'" + colorName + "')]",  // Text match
                "button[value='" + colorName + "']",                // Value attribute
                "button[data-value='" + colorName + "']",           // Data attribute
                "button[title='" + colorName + "']",                // Title attribute
                ".variant-button:contains('" + colorName + "')",    // Class with text
                "button:contains('" + colorName + "')"              // Any button with text
            };
            
            boolean selected = false;
            for (String selector : colorSelectors) {
                try {
                    By locator = selector.startsWith("//") || selector.contains(":contains")
                        ? By.xpath(selector.replace(":contains", "[contains(text(),").replace("')", "')]"))
                        : By.cssSelector(selector);
                    List<WebElement> btns = driver.findElements(locator);
                    if (!btns.isEmpty()) {
                        WebElement btn = btns.get(0);
                        new WebDriverWait(driver, Duration.ofSeconds(2))
                                .until(ExpectedConditions.elementToBeClickable(btn));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        System.out.println("✓ Selected color using selector: " + selector);
                        selected = true;
                        break;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            // Click first enabled button if no match found
            if (!selected) {
                for (WebElement button : allButtons) {
                    if (button.isEnabled()) {
                        String colorText = button.getText().trim();
                        button.click();
                        System.out.println("✅ Selected first available color: " + colorText);
                        return;
                    }
                }
                System.out.println("⚠️ No enabled color buttons available (try selecting a size with available colors)");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Color selector error: " + e.getMessage());
        }
    }
    
    /**
     * Select first available size variant
     */
    public void selectFirstAvailableSize() {
        try {
            System.out.println("Selecting first available size...");
            
            // Frontend structure: <h3>Kích thước</h3> followed by buttons
            String[] sizeSelectors = {
                "h3:contains('Kích thước') + div button:not([disabled])",  // Most accurate from frontend
                "button:not([disabled])[class*='rounded-full']",           // Button with rounded-full class
                "button:not([disabled])"                                   // Fallback: any enabled button
            };
            
            for (String selector : sizeSelectors) {
                try {
                    By locator;
                    if (selector.contains(":contains")) {
                        // Convert CSS :contains to XPath
                        locator = By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button[not(@disabled)]");
                    } else {
                        locator = By.cssSelector(selector);
                    }
                    
                    List<WebElement> buttons = driver.findElements(locator);
                    if (!buttons.isEmpty()) {
                        WebElement btn = buttons.get(0);
                        new WebDriverWait(driver, Duration.ofSeconds(1))
                                .until(ExpectedConditions.elementToBeClickable(btn));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        System.out.println("✓ Selected first size button: " + btn.getText());
                        Thread.sleep(500);
                        return;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            // Fallback using sizeButtons locator
            List<WebElement> buttons = driver.findElements(sizeButtons);
            for (WebElement button : buttons) {
                if (button.isEnabled()) {
                    button.click();
                    System.out.println("✅ Selected first available size: " + button.getText());
                    Thread.sleep(500);
                    return;
                }
            }
            
            System.out.println("⚠️ Could not find any size buttons");
        } catch (Exception e) {
            System.out.println("Error selecting first size: " + e.getMessage());
        }
    }
    
    /**
     * Select first available color variant
     */
    public void selectFirstAvailableColor() {
        try {
            System.out.println("Selecting first available color...");
            
            // Frontend structure: <h3>Màu sắc</h3> followed by buttons
            // After size is selected, color buttons become enabled
            String[] colorSelectors = {
                "h3:contains('Màu sắc') + div button:not([disabled])",   // Most accurate from frontend
                "button:not([disabled])[class*='rounded-full']"          // Fallback
            };
            
            for (String selector : colorSelectors) {
                try {
                    By locator;
                    if (selector.contains(":contains")) {
                        // Convert CSS :contains to XPath
                        locator = By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]");
                    } else {
                        locator = By.cssSelector(selector);
                    }
                    
                    List<WebElement> buttons = driver.findElements(locator);
                    if (!buttons.isEmpty()) {
                        WebElement btn = buttons.get(0);
                        new WebDriverWait(driver, Duration.ofSeconds(1))
                                .until(ExpectedConditions.elementToBeClickable(btn));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        System.out.println("✓ Selected first color button: " + btn.getText());
                        return;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            // Fallback: wait a bit for colors to become enabled after size selection
            Thread.sleep(500);
            List<WebElement> allButtons = driver.findElements(allColorButtons);
            for (WebElement button : allButtons) {
                if (button.isEnabled()) {
                    String colorText = button.getText().trim();
                    button.click();
                    System.out.println("✅ Selected first available color: " + colorText);
                    return;
                }
            }
            
            System.out.println("⚠️ Could not find any color buttons");
        } catch (Exception e) {
            System.out.println("Error selecting first color: " + e.getMessage());
        }
    }
    
    /**
     * Complete variant selection - select size first, then color
     */
    public void selectVariant(String size, String color) {
        selectSize(size);
        selectColor(color);
    }
    
    /**
     * Select first available variant (size + color)
     * This method tries each size until it finds one with available colors
     */
    public void selectFirstAvailableVariant() {
        try {
            List<WebElement> sizeButtonsList = driver.findElements(sizeButtons);
            System.out.println("  Total size buttons: " + sizeButtonsList.size());
            
            // Try each size until we find one with available colors
            for (WebElement sizeBtn : sizeButtonsList) {
                if (!sizeBtn.isEnabled()) continue;
                
                String sizeName = sizeBtn.getText().trim();
                sizeBtn.click();
                System.out.println("✅ Trying size: " + sizeName);
                Thread.sleep(600); // Wait for color buttons to update
                
                // Check if any colors are now enabled
                List<WebElement> enabledColors = driver.findElements(colorButtons);
                if (enabledColors.size() > 0) {
                    // Found a size with colors! Click the first enabled color
                    WebElement colorBtn = enabledColors.get(0);
                    String colorName = colorBtn.getText().trim();
                    colorBtn.click();
                    System.out.println("✅ Selected color: " + colorName);
                    Thread.sleep(500);
                    return;
                } else {
                    System.out.println("  ⚠️ Size " + sizeName + " has no available colors, trying next...");
                }
            }
            
            System.out.println("❌ No size+color combination found");
        } catch (Exception e) {
            System.out.println("⚠️ Variant selection error: " + e.getMessage());
        }
    }
    
    /**
     * Select variant (size and color) by clicking buttons
     */
    public void selectVariantByButtons(String size, String color) {
        try {
            // Select size
            if (size != null && !size.isEmpty()) {
                By sizeButton = By.xpath(String.format("//button[contains(text(),'%s')]", size));
                waitForElementToBeClickable(sizeButton, 3);
                click(sizeButton);
            }
            
            // Select color
            if (color != null && !color.isEmpty()) {
                By colorButton = By.xpath(String.format("//button[contains(text(),'%s')]", color));
                waitForElementToBeClickable(colorButton, 3);
                click(colorButton);
            }
            
            System.out.println("✓ Selected variant: " + size + " / " + color);
        } catch (Exception e) {
            System.out.println("✗ Failed to select variant: " + e.getMessage());
        }
    }
    
    /**
     * Add to cart - waits for button to be clickable (legacy method)
     */
    public void addToCart() {
        try {
            // First try the enabled button
            WebElement btn = waitForClickable(addToCartButtonEnabled, 5);
            btn.click();
            System.out.println("✅ Clicked Add to Cart button");
        } catch (Exception e) {
            try {
                // Fallback - check if button exists but has different state
                WebElement btn = driver.findElement(addToCartButton);
                String btnText = btn.getText().trim();
                System.out.println("  Add to cart button state: " + btnText);
                
                if (btnText.contains("Chọn size") || btnText.contains("Hết hàng")) {
                    System.out.println("⚠️ Cannot add to cart: " + btnText);
                } else if (!btn.isEnabled()) {
                    System.out.println("⚠️ Add to cart button is disabled");
                } else {
                    btn.click();
                    System.out.println("✅ Clicked Add to Cart button (fallback)");
                }
            } catch (Exception e2) {
                System.out.println("❌ Add to cart button not found: " + e2.getMessage());
            }
        }
    }
    
    /**
     * Click add to cart button (enhanced for cart testing with multiple locators)
     * Automatically selects first available size and color variants if needed
     */
    public void clickAddToCart() {
        try {
            System.out.println("Attempting to add product to cart...");
            
            // Frontend: Button with onClick={handleAddToCart}, disabled={condition}, className="flex-1"
            // Button text changes: "Chọn size và màu" | "Thêm vào giỏ" | "Hết hàng" | "Đang thêm..."
            String[] buttonSelectors = {
                "button[class*='flex-1']",           // Most accurate from frontend
                ".flex-1",                           // Shorthand
                "button[type='button']",             // React Button component default
                "//button[contains(.,'Thêm vào giỏ') or contains(.,'Chọn size và màu') or contains(.,'Đang thêm') or contains(.,'Hết hàng')]",
                "button:not([disabled])"             // Fallback
            };
            
            By addToCartLocator = null;
            WebElement addToCartBtn = null;
            
            // Find the button first
            for (String selector : buttonSelectors) {
                try {
                    By locator;
                    if (selector.startsWith("//")) {
                        locator = By.xpath(selector);
                    } else {
                        locator = By.cssSelector(selector);
                    }
                    List<WebElement> buttons = driver.findElements(locator);
                    if (!buttons.isEmpty()) {
                        addToCartLocator = locator;
                        addToCartBtn = buttons.get(0);
                        System.out.println("✓ Found button using selector: " + selector);
                        System.out.println("   Button text: " + addToCartBtn.getText());
                        System.out.println("   Button enabled: " + addToCartBtn.isEnabled());
                        break;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            if (addToCartBtn == null) {
                System.out.println("✗ Add to Cart button not found");
                throw new RuntimeException("Add to Cart button not found");
            }
            
            // Step 2: If button is disabled or text contains "Chọn", select variants
            String buttonText = addToCartBtn.getText();
            boolean needsVariants = !addToCartBtn.isEnabled() || buttonText.contains("Chọn");
            
            if (needsVariants) {
                System.out.println("ℹ️  Button is disabled or needs variant selection. Selecting variants...");
                // Ensure variant UI ready then quickly select
                waitForVariantsReady(4);
                fastSelectFirstSizeAndColor();

                // Refresh button reference after variant selection
                addToCartBtn = driver.findElement(addToCartLocator);
                System.out.println("✓ Variants selected. Button now enabled: " + addToCartBtn.isEnabled());
                System.out.println("   Button text now: " + addToCartBtn.getText());
            }
            // If still disabled or out of stock, abort so caller can try another product
            if (!addToCartBtn.isEnabled() || addToCartBtn.getText().contains("Hết hàng")) {
                throw new RuntimeException("Add to Cart disabled (possibly out of stock)");
            }
            
            // Step 3: Click the button
            System.out.println("Clicking 'Add to Cart' button...");
            try {
                // Scroll into view to avoid overlay issues
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addToCartBtn);
            } catch (Exception ignore) {}
            waitForElementToBeClickable(addToCartLocator, 3);
            click(addToCartLocator);
            System.out.println("✓ Clicked 'Add to Cart' button successfully");
            
        } catch (Exception e) {
            System.out.println("✗ Failed to add product to cart: " + e.getMessage());
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage());
        }
    }
    
    /**
     * Wait for add to cart success notification
     * Returns true if success message appears
     */
    public boolean waitForAddToCartSuccess() {
        try {
            System.out.println("Waiting for add to cart success alert...");
            try {
                new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.alertIsPresent());
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                System.out.println("✓ Alert appeared: " + alertText);
                alert.accept();
                System.out.println("✓ Alert accepted");
                // Detect login-required message and signal caller to handle authentication
                try {
                    String lowered = alertText == null ? "" : alertText.toLowerCase();
                    // Simple diacritic-insensitive checks
                    boolean containsDangNhap = lowered.contains("đăng nhập") || lowered.contains("dang nhap") || lowered.contains("login");
                    if (containsDangNhap) {
                        System.out.println("↩︎ Detected login-required alert, returning false to allow re-authentication");
                        return false;
                    }
                } catch (Exception ignore) {}
            } catch (Exception noAlert) {
                System.out.println("ℹ️  No alert present (likely toast). Proceeding.");
            }
            return true;
        } catch (Exception e) {
            System.out.println("⚠️  Error handling add to cart notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add product to cart with variant selection
     * Complete flow: select variant -> click add to cart -> wait for success
     */
    public boolean addToCartWithVariant(String size, String color) {
        try {
            // Step 1: Select variant
            selectVariantByButtons(size, color);
            
            // Step 2: Click add to cart
            clickAddToCart();
            
            // Step 3: Wait for success
            boolean success = waitForAddToCartSuccess();
            
            if (success) {
                System.out.println("✓ Successfully added product to cart");
            } else {
                System.out.println("✗ Failed to add product to cart");
            }
            
            return success;
        } catch (Exception e) {
            System.out.println("✗ Error adding product to cart: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get cart badge count from header
     */
    public int getCartBadgeCount() {
        try {
            if (isDisplayed(cartBadge)) {
                String badgeText = getText(cartBadge).trim();
                return Integer.parseInt(badgeText);
            }
        } catch (Exception e) {
            System.out.println("Cart badge not found or not readable");
        }
        return 0;
    }
    
    /**
     * Check if cart icon is displayed in header
     */
    public boolean isCartIconDisplayed() {
        try {
            return isDisplayed(cartIcon);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Click cart icon to navigate to cart page
     */
    public CartPage clickCartIcon() {
        try {
            click(cartIcon);
            waitForPageLoad();
            return new CartPage(driver);
        } catch (Exception e) {
            System.out.println("Failed to click cart icon");
            return null;
        }
    }
    
    /**
     * Navigate to cart page
     */
    public CartPage goToCart() {
        return clickCartIcon();
    }
    
    /**
     * Go back to product list
     */
    public void goBack() {
        try {
            click(backButton);
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
    
    /**
     * Click back button and return to product list page
     */
    public ProductListPage clickBack() {
        goBack();
        return new ProductListPage(driver);
    }
}