package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * ProductDetailPage - Page Object for Product Detail page (User side)
 */
public class ProductDetailPage extends BasePage {
    
    // Locators
    private final By productName = By.cssSelector("[data-testid='product-name'], h1, .product-title");
    private final By productPrice = By.cssSelector("[data-testid='product-price'], .price, [class*='price']");
    private final By productImage = By.cssSelector("[data-testid='product-image'], .product-image img, img[alt*='product']");
    private final By productDescription = By.cssSelector("[data-testid='product-description'], .description, [class*='description']");
    private final By addToCartButton = By.cssSelector("button[data-action='add-to-cart'], button:contains('Add to Cart')");
    private final By quantityInput = By.cssSelector("input[type='number'], input[name='quantity']");
    private final By sizeSelector = By.cssSelector("select[name='size'], [data-selector='size']");
    private final By colorSelector = By.cssSelector("select[name='color'], [data-selector='color']");
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
     * Select size
     */
    public void selectSize(String size) {
        try {
            driver.findElement(sizeSelector).sendKeys(size);
        } catch (Exception e) {
            System.out.println("Size selector not found");
        }
    }
    
    /**
     * Select color
     */
    public void selectColor(String color) {
        try {
            driver.findElement(colorSelector).sendKeys(color);
        } catch (Exception e) {
            System.out.println("Color selector not found");
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
