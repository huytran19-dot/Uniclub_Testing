package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductListPage - Page Object for Product List page (User side)
 */
public class ProductListPage extends BasePage {
    
    // Locators
    private final By productCards = By.cssSelector("[data-testid='product-card'], .product-card, [class*='product']");
    private final By firstProduct = By.cssSelector("[data-testid='product-card']:first-child, .product-card:first-child");
    private final By searchInput = By.cssSelector("input[type='search'], input[placeholder*='Search'], input[name='search']");
    private final By searchButton = By.cssSelector("button[type='submit'], button[aria-label*='Search']");
    private final By noResultsMessage = By.xpath("//*[contains(text(), 'No products') or contains(text(), 'No results') or contains(text(), 'not found')]");
    
    // Filter locators
    private final By categoryFilter = By.cssSelector("select[name='category'], [data-filter='category']");
    private final By brandFilter = By.cssSelector("select[name='brand'], [data-filter='brand']");
    private final By colorFilter = By.cssSelector("select[name='color'], [data-filter='color']");
    private final By sizeFilter = By.cssSelector("select[name='size'], [data-filter='size']");
    private final By clearFiltersButton = By.cssSelector("button[data-action='clear-filters'], button:contains('Clear')");
    
    // Sort locators
    private final By sortDropdown = By.cssSelector("select[name='sort'], [data-sort]");
    
    // Pagination locators
    private final By nextButton = By.cssSelector("button[data-pagination='next'], [aria-label*='Next']");
    private final By prevButton = By.cssSelector("button[data-pagination='prev'], [aria-label*='Previous']");
    
    /**
     * Constructor
     */
    public ProductListPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on product list page
     */
    public boolean isOnProductListPage() {
        try {
            waitForPageLoad();
            String currentUrl = getCurrentUrl();
            return currentUrl.contains("/products") || currentUrl.contains("/catalog");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get product count
     */
    public int getProductCount() {
        try {
            // Wait for page to fully load
            waitForPageLoad();
            
            // Fast path: real DOM uses a link to /products/:id inside card
            List<WebElement> fast = driver.findElements(By.cssSelector(".card.card-hover a[href*='/products/'], a[href*='/products/']"));
            if (!fast.isEmpty()) return fast.size();

            // Fallback minimal probing
            List<WebElement> fallback = driver.findElements(By.cssSelector("[data-testid='product-card'], .product-card"));
            return fallback.size();
        } catch (Exception e) {
            System.out.println("Error getting product count: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Click on first product
     */
    public ProductDetailPage clickFirstProduct() {
        try {
            waitForProductsVisible(5);

            // Prefer clicking the anchor inside the card to avoid overlay
            By firstLink = By.cssSelector(".card.card-hover a[href*='/products/'], a[href*='/products/']");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(firstLink));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            return new ProductDetailPage(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click first product: " + e.getMessage());
        }
    }
    
    /**
     * Click on product by index (0-based)
     */
    public ProductDetailPage clickProductByIndex(int index) {
        try {
            waitForProductsVisible(5);

            List<WebElement> products = driver.findElements(By.cssSelector(".card.card-hover a[href*='/products/'], a[href*='/products/']"));
            if (products != null && index >= 0 && index < products.size()) {
                WebElement target = products.get(index);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", target);
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(target));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
                return new ProductDetailPage(driver);
            } else {
                throw new IndexOutOfBoundsException("Product index " + index + " is out of range. Total products: " + (products != null ? products.size() : 0));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to click product at index " + index + ": " + e.getMessage());
        }
    }
    
    /**
     * Helper method to find products using multiple selectors
     */
    private List<WebElement> findProducts() {
        // Use exact selector from frontend: ProductCard has .card.card-hover class
        String[] possibleSelectors = {
            ".card.card-hover",          // ProductCard main class (EXACT from frontend)
            "a[href*='/products/']",     // Link wrapper of ProductCard
            "[class*='product']"         // Fallback
        };
        
        for (String selector : possibleSelectors) {
            try {
                List<WebElement> products = driver.findElements(By.cssSelector(selector));
                if (products != null && !products.isEmpty()) {
                    // keep logs minimal to avoid noise and slowdowns
                    return products;
                }
            } catch (Exception e) {
                // Try next selector
            }
        }
        
        System.out.println("⚠️ No products found with any selector");
        return new ArrayList<>();
    }

    /**
     * Wait until at least one product card is visible
     */
    public void waitForProductsVisible(int timeoutSeconds) {
        By anyProduct = By.cssSelector(".card.card-hover a[href*='/products/'], a[href*='/products/'], .card.card-hover");
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> d.findElements(anyProduct).size() > 0);
    }
    
    /**
     * Search for products
     */
    public void search(String keyword) {
        type(searchInput, keyword);
        try {
            click(searchButton);
        } catch (Exception e) {
            // If no search button, press Enter
            driver.findElement(searchInput).submit();
        }
    }
    
    /**
     * Check if no results message is displayed
     */
    public boolean isNoResultsDisplayed() {
        return isDisplayed(noResultsMessage);
    }
    
    /**
     * Filter by category
     */
    public void filterByCategory(String category) {
        try {
            WebElement categoryElement = driver.findElement(categoryFilter);
            categoryElement.sendKeys(category);
        } catch (Exception e) {
            // Category filter might not exist
            System.out.println("Category filter not found or not applicable");
        }
    }
    
    /**
     * Filter by brand
     */
    public void filterByBrand(String brand) {
        try {
            WebElement brandElement = driver.findElement(brandFilter);
            brandElement.sendKeys(brand);
        } catch (Exception e) {
            System.out.println("Brand filter not found or not applicable");
        }
    }
    
    /**
     * Filter by color
     */
    public void filterByColor(String color) {
        try {
            WebElement colorElement = driver.findElement(colorFilter);
            colorElement.sendKeys(color);
        } catch (Exception e) {
            System.out.println("Color filter not found or not applicable");
        }
    }
    
    /**
     * Filter by size
     */
    public void filterBySize(String size) {
        try {
            WebElement sizeElement = driver.findElement(sizeFilter);
            sizeElement.sendKeys(size);
        } catch (Exception e) {
            System.out.println("Size filter not found or not applicable");
        }
    }
    
    /**
     * Clear all filters
     */
    public void clearFilters() {
        try {
            click(clearFiltersButton);
        } catch (Exception e) {
            System.out.println("Clear filters button not found");
        }
    }
    
    /**
     * Sort products
     */
    public void sortBy(String sortOption) {
        try {
            WebElement sortElement = driver.findElement(sortDropdown);
            sortElement.sendKeys(sortOption);
        } catch (Exception e) {
            System.out.println("Sort dropdown not found");
        }
    }
    
    /**
     * Go to next page
     */
    public void goToNextPage() {
        try {
            click(nextButton);
        } catch (Exception e) {
            System.out.println("Next button not found or not clickable");
        }
    }
    
    /**
     * Go to previous page
     */
    public void goToPreviousPage() {
        try {
            click(prevButton);
        } catch (Exception e) {
            System.out.println("Previous button not found or not clickable");
        }
    }
    
    /**
     * Check if next button is enabled
     */
    public boolean isNextButtonEnabled() {
        return isEnabled(nextButton);
    }
    
    /**
     * Check if previous button is enabled
     */
    public boolean isPreviousButtonEnabled() {
        return isEnabled(prevButton);
    }
}
