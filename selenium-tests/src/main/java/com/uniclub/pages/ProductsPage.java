package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * ProductsPage - Page Object for Products Listing page
 * URL: http://localhost:5173/products
 */
public class ProductsPage extends BasePage {
    
    // Locators
    private final By productCards = By.xpath("//div[contains(@class, 'product-card') or contains(@class, 'product-item')]");
    private final By productLinks = By.xpath("//a[contains(@href, '/products/')]");
    private final By searchInput = By.xpath("//input[@type='search' or @placeholder='Tìm kiếm']");
    private final By searchButton = By.xpath("//button[contains(text(),'Tìm') or @type='submit']");
    private final By filterCategory = By.xpath("//select[@name='category']");
    private final By noProductsMessage = By.xpath("//*[contains(text(),'Không tìm thấy sản phẩm') or contains(text(),'No products found')]");
    
    // Constructor
    public ProductsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on products page
     */
    public boolean isOnProductsPage() {
        return driver.getCurrentUrl().contains("/products");
    }
    
    /**
     * Get number of products displayed
     */
    public int getProductCount() {
        try {
            List<WebElement> products = driver.findElements(productCards);
            return products.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Click on first product
     */
    public void clickFirstProduct() {
        try {
            List<WebElement> productLinks = driver.findElements(this.productLinks);
            if (!productLinks.isEmpty()) {
                productLinks.get(0).click();
            }
        } catch (Exception e) {
            System.err.println("Error clicking first product: " + e.getMessage());
        }
    }
    
    /**
     * Click on product by index
     */
    public void clickProduct(int index) {
        try {
            List<WebElement> productLinks = driver.findElements(this.productLinks);
            if (index < productLinks.size()) {
                productLinks.get(index).click();
            }
        } catch (Exception e) {
            System.err.println("Error clicking product at index " + index + ": " + e.getMessage());
        }
    }
    
    /**
     * Search for products
     */
    public void searchProducts(String keyword) {
        type(searchInput, keyword);
        click(searchButton);
    }
    
    /**
     * Check if products are displayed
     */
    public boolean hasProducts() {
        return getProductCount() > 0;
    }
    
    /**
     * Wait for products to load
     */
    public void waitForProductsToLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
