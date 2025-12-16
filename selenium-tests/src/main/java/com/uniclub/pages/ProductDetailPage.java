package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    // Add to cart button - can have multiple texts depending on state
    // "Thêm vào giỏ" - can add
    // "Chọn size và màu" - need to select
    // "Hết hàng" - out of stock
    // "Đang thêm..." - loading
    private final By addToCartButton = By.xpath("//button[contains(text(),'Thêm vào giỏ') or contains(text(),'Đang thêm') or contains(text(),'Chọn size') or contains(text(),'Hết hàng')]");
    private final By addToCartButtonEnabled = By.xpath("//button[contains(text(),'Thêm vào giỏ') and not(@disabled)]");
    private final By quantityInput = By.cssSelector("input[type='number'], input[name='quantity']");
    // Size and Color are buttons in VariantSelector, not selects
    // Size buttons have class "rounded-full" and are under h3 with text "Kích thước"
    private final By sizeButtons = By.xpath("//h3[contains(text(),'Kích thước')]/following-sibling::div//button");
    // Color buttons are under h3 with text "Màu sắc"
    private final By colorButtons = By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button[not(@disabled)]");
    private final By allColorButtons = By.xpath("//h3[contains(text(),'Màu sắc')]/following-sibling::div//button");
    private final By backButton = By.cssSelector("button[data-action='back'], a:contains('Back')");
    private final By productRating = By.cssSelector("[data-testid='rating'], .rating, [class*='rating']");
    private final By reviewsSection = By.cssSelector("[data-testid='reviews'], .reviews, [class*='review']");
    
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
     * Add to cart - waits for button to be clickable
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
            List<WebElement> buttons = driver.findElements(sizeButtons);
            for (WebElement button : buttons) {
                if (button.getText().trim().equalsIgnoreCase(sizeName) && button.isEnabled()) {
                    button.click();
                    Thread.sleep(500); // Wait for color buttons to update
                    System.out.println("✅ Selected size: " + sizeName);
                    return;
                }
            }
            // If exact match not found, click first available button
            for (WebElement button : buttons) {
                if (button.isEnabled()) {
                    button.click();
                    System.out.println("✅ Selected first available size: " + button.getText());
                    Thread.sleep(500);
                    return;
                }
            }
            System.out.println("⚠️ No size buttons available");
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
            
            // Click first enabled button
            for (WebElement button : allButtons) {
                if (button.isEnabled()) {
                    String colorText = button.getText().trim();
                    button.click();
                    System.out.println("✅ Selected first available color: " + colorText);
                    return;
                }
            }
            System.out.println("⚠️ No enabled color buttons available (try selecting a size with available colors)");
        } catch (Exception e) {
            System.out.println("⚠️ Color selector error: " + e.getMessage());
        }
    }
    
    /**
     * Select first available size
     */
    public void selectFirstAvailableSize() {
        selectSize("");
    }
    
    /**
     * Select first available color
     */
    public void selectFirstAvailableColor() {
        selectColor("");
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
     * Check if add to cart button is displayed
     */
    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
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
}
