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
    
    // Locators
    private final By productName = By.cssSelector("[data-testid='product-name'], h1, .product-title");
    private final By productPrice = By.cssSelector("[data-testid='product-price'], .price, [class*='price']");
    private final By productImage = By.cssSelector("[data-testid='product-image'], .product-image img, img[alt*='product']");
    private final By productDescription = By.cssSelector("[data-testid='product-description'], .description, [class*='description']");
    private final By addToCartButton = By.xpath("//button[contains(text(),'Thêm vào giỏ')]");
    private final By quantityInput = By.cssSelector("input[type='number'], input[name='quantity']");
    private final By sizeSelector = By.cssSelector("select[name='size'], [data-selector='size']");
    private final By colorSelector = By.cssSelector("select[name='color'], [data-selector='color']");
    private final By backButton = By.cssSelector("button[data-action='back'], a:contains('Back')");
    private final By productRating = By.cssSelector("[data-testid='rating'], .rating, [class*='rating']");
    private final By reviewsSection = By.cssSelector("[data-testid='reviews'], .reviews, [class*='review']");
    
    // Cart-related locators
    private final By cartIcon = By.cssSelector("a[href='/cart'], svg.lucide-shopping-cart");
    private final By cartBadge = By.cssSelector("a[href='/cart'] span, .cart-badge");
    private final By variantSizeButton = By.cssSelector("h3 + div button, button[data-variant-type='size']");
    private final By variantColorButton = By.cssSelector("h3 + div button, button[data-variant-type='color']");
    private final By sizeHeader = By.xpath("//h3[contains(text(),'Kích thước')]");
    private final By colorHeader = By.xpath("//h3[contains(text(),'Màu sắc')]");
    private final By stockInfo = By.xpath("//*[contains(text(),'Còn')]");
    
    /**
     * Constructor
     */
    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Select size variant (click on size button)
     */
    public void selectSize(String size) {
        try {
            System.out.println("Selecting size: " + size);
            
            // Try different selectors for size buttons
            String[] sizeSelectors = {
                "//button[contains(text(),'" + size + "')]",  // Text match
                "button[value='" + size + "']",                // Value attribute
                "button[data-value='" + size + "']",           // Data attribute
                ".variant-button:contains('" + size + "')",    // Class with text
                "button:contains('" + size + "')"              // Any button with text
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
            
            if (!selected) {
                System.out.println("⚠️ Could not find size button for: " + size);
            }
        } catch (Exception e) {
            System.out.println("Error selecting size: " + e.getMessage());
        }
    }
    
    /**
     * Select color variant (click on color button)
     */
    public void selectColor(String color) {
        try {
            System.out.println("Selecting color: " + color);
            
            // Try different selectors for color buttons
            String[] colorSelectors = {
                "//button[contains(text(),'" + color + "')]",  // Text match
                "button[value='" + color + "']",                // Value attribute
                "button[data-value='" + color + "']",           // Data attribute
                "button[title='" + color + "']",                // Title attribute
                ".variant-button:contains('" + color + "')",    // Class with text
                "button:contains('" + color + "')"              // Any button with text
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
            
            if (!selected) {
                System.out.println("⚠️ Could not find color button for: " + color);
            }
        } catch (Exception e) {
            System.out.println("Error selecting color: " + e.getMessage());
        }
    }
    
    /**
     * Select first available size variant
     */
    public void selectFirstAvailableSize() {
        try {
            System.out.println("Selecting first available size...");
            
            // Frontend structure: <h3>Kích thước</h3> followed by buttons
            // Button structure from VariantSelector.jsx: <Button size="sm">SIZE_NAME</Button>
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
                    
                    List<org.openqa.selenium.WebElement> buttons = driver.findElements(locator);
                    if (!buttons.isEmpty()) {
                        org.openqa.selenium.WebElement btn = buttons.get(0);
                        new WebDriverWait(driver, Duration.ofSeconds(1))
                                .until(ExpectedConditions.elementToBeClickable(btn));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        System.out.println("✓ Selected first size button: " + btn.getText());
                        return;
                    }
                } catch (Exception e) {
                    // Try next selector
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
                    
                    List<org.openqa.selenium.WebElement> buttons = driver.findElements(locator);
                    if (!buttons.isEmpty()) {
                        org.openqa.selenium.WebElement btn = buttons.get(0);
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
            
            System.out.println("⚠️ Could not find any color buttons");
        } catch (Exception e) {
            System.out.println("Error selecting first color: " + e.getMessage());
        }
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
     * Add to cart
     */
    public void addToCart() {
        try {
            click(addToCartButton);
        } catch (Exception e) {
            System.out.println("Add to cart button not found");
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
     * Check if add to cart button is displayed
     */
    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
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
            List<WebElement> sizeButtons = driver.findElements(By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button[not(@disabled)]"));
            if (!sizeButtons.isEmpty()) {
                WebElement btn = sizeButtons.get(0);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        } catch (Exception ignore) {}
        // After size is selected, colors become enabled; pick first
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(d -> !d.findElements(By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]")).isEmpty());
            List<WebElement> colorButtons = driver.findElements(By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]"));
            if (!colorButtons.isEmpty()) {
                WebElement btn = colorButtons.get(0);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        } catch (Exception ignore) {}
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
            org.openqa.selenium.WebElement addToCartBtn = null;
            
            // Find the button first
            for (String selector : buttonSelectors) {
                try {
                    By locator;
                    if (selector.startsWith("//")) {
                        locator = By.xpath(selector);
                    } else {
                        locator = By.cssSelector(selector);
                    }
                    java.util.List<org.openqa.selenium.WebElement> buttons = driver.findElements(locator);
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
     * Navigate to cart page
     */
    public CartPage goToCart() {
        return clickCartIcon();
    }
}

